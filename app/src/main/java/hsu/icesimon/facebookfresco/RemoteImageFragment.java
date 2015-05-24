package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 * Use Fresco for loading remote images.
 * - WikiMedia will return JPG files.
 * - OpenClipArt will return PNG files
 * Fresco 0.4.0 before has some problems on parsing PNG files.
 * (Lag on scrolling..)
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoteImageFragment extends Fragment {
    View view;
    private int width;
    MyAdapter adapter;
    GridView gridView;
    RelativeLayout loadingLayout;
    private List<ImageItem> items = new ArrayList<ImageItem>();
    private OkHttpClient client = new OkHttpClient();

    public static RemoteImageFragment newInstance() {
        RemoteImageFragment fragment = new RemoteImageFragment();
        return fragment;
    }

    public RemoteImageFragment() {
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
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remote_image, container, false);
        loadingLayout = (RelativeLayout)view.findViewById(R.id.loading);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
        getRemoteItemList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
// TODO: Rename method, update argument and hook method into UI event

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
                    Log.d("Path : "+ mpath);

                }
            });
            Uri uri = Uri.parse(item.imagePath);
            holder.imgQueue.setImageURI(uri);
            return view;
        }
    }

    public class ViewHolder {
        SquareSimpleDraweeView imgQueue;
    }

    public void getRemoteItemList() {

        // OpenClipArt will return PNG files
        // WikiMedia will return JPG files.

        String url = "https://openclipart.org/search/json/?query=apple&page=1&amount=50";
//        String url = "http://commons.wikimedia.org/w/api.php?action=query&prop=imageinfo&format=json&iiprop=url%7Csize%7Cmime&iiurlwidth=200&iiurlheight=200&redirects=&generator=search&gsrsearch=File:Blackberry&gsrnamespace=0&gsrredirects=&gsrlimit=40&gsroffset=120";
        Request request = new Request.Builder()
                .url(url)
                .build();
        final Handler mHandler = new Handler();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Request request, final IOException e) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                items.clear();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String result = response.body().string();
                try {

                    // For parsing wikipedia
//                    JSONObject resultObj = new JSONObject(result);
//                    JSONObject obj = (JSONObject)resultObj.get("query");
//                    JSONObject payloadArray = obj.getJSONObject("pages");
//                    items = new ArrayList<ImageItem>();
//                    Iterator<?> keys = payloadArray.keys();
//                    while( keys.hasNext() ) {
//                        String key = (String)keys.next();
//                        if ( payloadArray.get(key) instanceof JSONObject ) {
//                            JSONObject xx = (JSONObject)payloadArray.get(key);
//                            JSONArray itemSVG = (JSONArray)xx.get("imageinfo");
//                            String imageUrl = ((JSONObject)itemSVG.get(0)).getString("thumburl");
//                            Log.d("imageUrl: "+imageUrl);
//                            items.add(new ImageItem(R.drawable.no_media, imageUrl));
//                        }
//                    }


                    // For parsing openclipart
                    JSONObject resultObj = new JSONObject(result);
                    JSONArray payloadArray = resultObj.getJSONArray("payload");
                    items = new ArrayList<ImageItem>();
                    for (int i = 0 ; i < payloadArray.length(); i++)
                    {
                        JSONObject item = (JSONObject)payloadArray.get(i);
                        JSONObject itemSVG = (JSONObject)item.get("svg");
                        String imageUrl = itemSVG.getString("png_full_lossy");
                        items.add(new ImageItem(R.drawable.no_media, imageUrl));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        gridView.invalidateViews();
                        loadingLayout.setVisibility(View.GONE);

                    }
                });
            }
        });
    }
}
