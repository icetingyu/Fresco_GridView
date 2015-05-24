package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 * Simply use Fresco to load local images.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalImageFragment extends Fragment{
    View view;
    MyAdapter adapter;
    private int width;
    GridView gridView;

    private List<ImageItem> items = new ArrayList<ImageItem>();

    public static LocalImageFragment newInstance() {
        LocalImageFragment fragment = new LocalImageFragment();
        return fragment;
    }

    public LocalImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels / 4;
        adapter = new MyAdapter(getActivity());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_local_image, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
        getItemList();
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.gridview_draweeview_item, viewGroup, false);
                holder = new ViewHolder();
                holder.imgQueue = (SquareSimpleDraweeView) view.findViewById(R.id.picture);
                holder.imgQueue.setMaxHeight(width);
                holder.imgQueue.setMaxWidth(width);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }

            ImageItem item = (ImageItem) getItem(i);
            final String mpath = item.imagePath;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Click on  Path : " + mpath);
                }
            });

            Uri uri = Uri.parse("file://" + item.imagePath);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width / 2, width / 2))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(holder.imgQueue.getController())
                    .build();
            holder.imgQueue.setController(controller);
            return view;
        }
    }

    public class ViewHolder {
        SquareSimpleDraweeView imgQueue;
    }

    public void allScan() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(Environment.getExternalStorageDirectory()); // out is your output file
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

        } else {
            getActivity().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
    }

    public void getItemList() {
        allScan();
        items = insertData();
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
    }

    private List<ImageItem> insertData() {
        List<ImageItem> items = new ArrayList<ImageItem>();
        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor imagecursor = getActivity().managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    items.add(new ImageItem(R.drawable.no_media, imagecursor.getString(dataColumnIndex)));

                }
//                Log.d("items : "+items.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(items);
        return items;
    }
}
