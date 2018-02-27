//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.mediaselectionlib;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.Calendar;


public class VideoLoadingUtils {

    private Context mContext;
    private static Uri sVideoUri;
    private ImageLoadingUtils imageLoadingUtils;

    /**
     * Initialize class with context
     *
     * @param context context
     */
    public VideoLoadingUtils(Context context) {
        mContext = context;
        imageLoadingUtils = new ImageLoadingUtils(context);
    }

    /**
     * Method to get video path from selected uri or captured video uri
     *
     * @param uri                 uri of video
     * @param videoLoaderListener videoLoaderListener will notify after getting actual video path
     */

    public void getVideoPath(Uri uri, VideoLoaderListener videoLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new VideoLoadingAsyncTask(uri, mContext, imageLoadingUtils, videoLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * set video URI for video capture from camera
     *
     * @param context Context
     */
    public Uri setUri(Context context) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", context.getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".mp4");
            int currentApiVersion = Build.VERSION.SDK_INT;
            if (currentApiVersion > Build.VERSION_CODES.M) {
//                sVideoUri = FileProvider.getUriForFile(mContext,
//                        mContext.getApplicationContext().getPackageName() + ".provider", file);
                sVideoUri = FileProvider.getUriForFile(mContext,
                        mContext.getString(R.string.app_provider), file);
            } else {
                sVideoUri = Uri.fromFile(file);
            }
        } else {
            File file = new File(context.getFilesDir(), context.getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".mp4");
            int currentApiVersion = Build.VERSION.SDK_INT;
            if (currentApiVersion > Build.VERSION_CODES.M) {
//                sVideoUri = FileProvider.getUriForFile(mContext,
//                        mContext.getApplicationContext().getPackageName() + ".provider", file);
                sVideoUri = FileProvider.getUriForFile(mContext,
                        mContext.getString(R.string.app_provider), file);
            } else {
                sVideoUri = Uri.fromFile(file);
            }
        }
        return sVideoUri;
    }

    /**
     * set video URI with specific path for video capture from camera
     *
     * @param path full path of image
     * @return Uri from path
     */

    public Uri setUri(String path) {
        File file = new File(path);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.M) {
//            sVideoUri = FileProvider.getUriForFile(mContext,
//                    mContext.getApplicationContext().getPackageName() + ".provider", file);
            sVideoUri = FileProvider.getUriForFile(mContext,
                    mContext.getString(R.string.app_provider), file);
        } else {
            sVideoUri = Uri.fromFile(file);
        }
        return sVideoUri;
    }

    /**
     * Get video Uri set before video capture from Camera
     *
     * @return uri
     */

    public Uri getUri() {
        return sVideoUri;
    }

    /**
     * get Small thumbnail bitmap from video path
     *
     * @param path path of image
     * @return bitmap of thumb
     */

    public Bitmap getSmallVideoThumbNail(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
//        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);


    }


    public Bitmap getVideoThumb(String videoPath) {
        Bitmap bitmap = null;

        try {
            //   copyFiles();
            //thumb = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
            MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
            mMMR.setDataSource(videoPath);
            //api time unit is microseconds
            bitmap = mMMR.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }
    /**
     * get large thumbnail bitmap from video path
     *
     * @param path path of image
     * @return bitmap of thumb
     */
    public Bitmap getLargeVideoThumbNail(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
    }

    /**
     * get Small thumbnail bitmap from video uri
     *
     * @param uri uri of image
     * @return bitmap of thumb
     */
    public Bitmap getSmallVideoThumbNail(Uri uri) {
        return ThumbnailUtils.createVideoThumbnail(imageLoadingUtils.getImagePathFromUri(uri), MediaStore.Images.Thumbnails.MICRO_KIND);
    }

    /**
     * get large thumbnail bitmap from video uri
     *
     * @param uri uri of image
     * @return bitmap of thumb
     */
    public Bitmap getLargeVideoThumbNail(Uri uri) {
        return ThumbnailUtils.createVideoThumbnail(imageLoadingUtils.getImagePathFromUri(uri), MediaStore.Images.Thumbnails.MINI_KIND);
    }
}
