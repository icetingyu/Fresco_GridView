package hsu.icesimon.facebookfresco;

/**
 * Created by Simon Hsu on 15/5/24.
 * Fragment with GridView which combined with Universal Image Loader and Picasso.
 * Picasso can't handle large image files, so I combined Universal Image Loader to show those large images.
 * (Slower but make sure all images can be displayed properly)
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalImagePicassoUILHybridFragment extends Fragment {
    View view;
    MyAdapter adapter;
    private int width;
    GridView gridView;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoadingListener mImageLoadingListenerImpl;
    private List<ImageItem> items = new ArrayList<ImageItem>();

    public static LocalImagePicassoUILHybridFragment newInstance() {
        LocalImagePicassoUILHybridFragment fragment = new LocalImagePicassoUILHybridFragment();
        return fragment;
    }

    public LocalImagePicassoUILHybridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mImageLoader = ImageLoader.getInstance();
        initImageLoader(getActivity());
        int defaultImageId = R.drawable.no_media;

        width = displaymetrics.widthPixels / 4;
        adapter = new MyAdapter(getActivity());
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 16;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .decodingOptions(opt)
                .cacheInMemory(true)
                .cacheOnDisk(true)  //Set the downloaded image be cached in SD card or not.
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT) //Set which encoding type will be used for displaying images.
                .bitmapConfig(Bitmap.Config.RGB_565) //Set which decoding type will be used for parsing images.
                .build();
        mImageLoadingListenerImpl = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_local_picasso_image, container, false);
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
                view = inflater.inflate(R.layout.gridview_imageview_item, viewGroup, false);
                holder = new ViewHolder();
                holder.imgQueue = (SquareImageView) view.findViewById(R.id.picture);
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

            String imagePath = "file://" + item.imagePath;

            // Use SimpleImageInfo class to check the width and height of images.
            SimpleImageInfo imageInfo = null;
            try {
                imageInfo = new SimpleImageInfo(new File(mpath));

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageInfo != null) {
                int _width = imageInfo.getWidth();
                int _height = imageInfo.getHeight();
                if (_width > 3500 || _height > 3500) {
                    // Log.d("image too large : " + _width + "," + _height + ": " + imagePath);
                    try {
                        mImageLoader.displayImage(
                                imagePath,
                                holder.imgQueue,
                                mDisplayImageOptions,
                                mImageLoadingListenerImpl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Picasso.with(getActivity())
                            .load(new File(item.imagePath))
                            .placeholder(R.drawable.no_media)
                            .transform(new BitmapTransform(width, width))
                            .resize(width, width)
                            .centerCrop()
                            .into(holder.imgQueue);
                }
            } else {
                Picasso.with(getActivity())
                        .load(new File(item.imagePath))
                        .placeholder(R.drawable.no_media)
                        .transform(new BitmapTransform(width, width))
                        .resize(width, width)
                        .centerCrop()
                        .into(holder.imgQueue);
            }

            return view;
        }
    }

    public class ViewHolder {
        SquareImageView imgQueue;
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
            final String[] columns = {Images.Media.DATA,
                    Images.Media._ID};
            final String orderBy = Images.Media._ID;

            Cursor imagecursor = getActivity().managedQuery(
                    Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(Images.Media.DATA);
                    items.add(new ImageItem(R.drawable.no_media, imagecursor.getString(dataColumnIndex)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(items);
        return items;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageLoader != null) {
            mImageLoader.clearMemoryCache();
            mImageLoader.clearDiscCache();
        }
    }
}
