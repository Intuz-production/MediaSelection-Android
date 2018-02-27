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
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;


public class ImageLoadingUtils {

    private Context mContext;
    private static Uri sImgUri;

    /**
     * Initialize class with context
     *
     * @param context context
     */
    public ImageLoadingUtils(Context context) {
        mContext = context;
    }

    /**
     * Method to get image path from selected uri or captured image uri
     *
     * @param uri                     uri of image
     * @param imagePathLoaderListener imageLoadingListener will notify after getting actual image path
     */

    public void getImagePath(Uri uri, ImagePathLoaderListener imagePathLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImagePathCompressionAsyncTask(mContext, getPath(uri), imagePathLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image path from selected uri or captured image uri
     *
     * @param uri                     uri of image
     * @param imagePathLoaderListener imageLoadingListener will notify after getting actual image path
     * @param resultedImageFormat     imageFormat like jpg,jpeg or png
     * @param imageCompressionRate    compressionRate from 1 to 100
     */

    public void getImagePath(Uri uri,
                             String resultedImageFormat,
                             int imageCompressionRate,
                             ImagePathLoaderListener imagePathLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImagePathCompressionAsyncTask(mContext, getPath(uri), resultedImageFormat, imageCompressionRate, imagePathLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image path from selected uri or captured image uri
     *
     * @param uri                     uri of image
     * @param resultedImagePath       path to store final result , if path is given all other image format will be ignored
     * @param imagePathLoaderListener imageLoadingListener will notify after getting actual image path
     */

    public void getImagePath(Uri uri,
                             String resultedImagePath, ImagePathLoaderListener imagePathLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImagePathCompressionAsyncTask(mContext, getPath(uri), resultedImagePath, imagePathLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image path from selected uri or captured image uri
     *
     * @param uri                     uri of image
     * @param imageCompressionRate    compression rate from 1 to 100
     * @param resultedImagePath       path to store final result , if path is given all other image format will be ignored
     * @param imagePathLoaderListener imageLoadingListener will notify after getting actual image path
     */

    public void getImagePath(Uri uri, int imageCompressionRate, String resultedImagePath, ImagePathLoaderListener imagePathLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImagePathCompressionAsyncTask(mContext, getPath(uri), imageCompressionRate, resultedImagePath, imagePathLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image path from selected uri or captured image uri
     *
     * @param uri                     uri of image
     * @param imageCompressionRate    compression rate from 1 to 100
     * @param resultedImagePath       path to store final result , if path is given all other image format will be ignored
     * @param scaleDownValue          scale down value for bitmap
     * @param imagePathLoaderListener imageLoadingListener will notify after getting actual image path
     */

    public void getImagePath(Uri uri, int imageCompressionRate, String resultedImagePath, int scaleDownValue, ImagePathLoaderListener imagePathLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImagePathCompressionAsyncTask(mContext, getPath(uri), imageCompressionRate, resultedImagePath, scaleDownValue, imagePathLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image uri from selected uri or captured image uri
     *
     * @param uri                    uri of image
     * @param imageUriLoaderListener imageUriLoaderListener will notify after getting actual image uri
     */


    public void getImageUri(Uri uri, ImageUriLoaderListener imageUriLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageUriCompressionAsyncTask(mContext, getPath(uri), imageUriLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image uri from selected uri or captured image uri
     *
     * @param uri                    uri of image
     * @param imageUriLoaderListener imageUriLoaderListener will notify after getting actual image uri
     * @param resultedImageFormat    imageFormat like jpg,jpeg or png
     * @param imageCompressionRate   compressionRate from 1 to 100
     */

    public void getImageUri(Uri uri,
                            String resultedImageFormat,
                            int imageCompressionRate,
                            ImageUriLoaderListener imageUriLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageUriCompressionAsyncTask(mContext, getPath(uri), resultedImageFormat, imageCompressionRate, imageUriLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image uri from selected uri or captured image uri
     *
     * @param uri                    uri of image
     * @param resultedImagePath      path to store final result , if path is given all other image format will be ignored
     * @param imageUriLoaderListener imageUriLoaderListener will notify after getting actual image uri
     */

    public void getImageUri(Uri uri,
                            String resultedImagePath, ImageUriLoaderListener imageUriLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageUriCompressionAsyncTask(mContext, getPath(uri), resultedImagePath, imageUriLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image uri from selected uri or captured image uri
     *
     * @param uri                    uri of image
     * @param imageCompressionRate   compression rate from 1 to 100
     * @param resultedImagePath      path to store final result , if path is given all other image format will be ignored
     * @param imageUriLoaderListener imageUriLoaderListener will notify after getting actual image uri
     */

    public void getImageUri(Uri uri, int imageCompressionRate, String resultedImagePath,
                            ImageUriLoaderListener imageUriLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageUriCompressionAsyncTask(mContext, getPath(uri), imageCompressionRate, resultedImagePath, imageUriLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image uri from selected uri or captured image uri
     *
     * @param uri                    uri of image
     * @param imageCompressionRate   compression rate from 1 to 100
     * @param resultedImagePath      path to store final result , if path is given all other image format will be ignored
     * @param scaleDownValue         scale down value for bitmap
     * @param imageUriLoaderListener imageUriLoaderListener will notify after getting actual image uri
     */

    public void getImageUri(Uri uri, int imageCompressionRate, String resultedImagePath,
                            int scaleDownValue, ImageUriLoaderListener imageUriLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageUriCompressionAsyncTask(mContext, getPath(uri), imageCompressionRate, resultedImagePath, scaleDownValue, imageUriLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image bitmap from selected uri or captured image uri
     *
     * @param uri                       uri of image
     * @param imageBitmapLoaderListener imageLoadingListener will notify after getting actual image bitmap
     */


    public void getImageBitmap(Uri uri, ImageBitmapLoaderListener imageBitmapLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageBitmapCompressionAsyncTask(mContext, getPath(uri), imageBitmapLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image bitmap from selected uri or captured image uri
     *
     * @param uri                       uri of image
     * @param imageBitmapLoaderListener imageBitmapLoaderListener will notify after getting actual image bitmap
     * @param resultedImageFormat       imageFormat like jpg,jpeg or png
     * @param imageCompressionRate      compressionRate from 1 to 100
     */

    public void getImageBitmap(Uri uri,
                               String resultedImageFormat,
                               int imageCompressionRate,
                               ImageBitmapLoaderListener imageBitmapLoaderListener) {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageBitmapCompressionAsyncTask(mContext, getPath(uri), resultedImageFormat, imageCompressionRate, imageBitmapLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image bitmap from selected uri or captured image uri
     *
     * @param uri                       uri of image
     * @param resultedImagePath         path to store final result , if path is given all other image format will be ignored
     * @param imageBitmapLoaderListener imageBitmapLoaderListener will notify after getting actual image bitmap
     */

    public void getImageBitmap(Uri uri,
                               String resultedImagePath, ImageBitmapLoaderListener imageBitmapLoaderListener) {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageBitmapCompressionAsyncTask(mContext, getPath(uri), resultedImagePath, imageBitmapLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image bitmap from selected uri or captured image uri
     *
     * @param uri                       uri of image
     * @param imageCompressionRate      compression rate from 1 to 100
     * @param resultedImagePath         path to store final result , if path is given all other image format will be ignored
     * @param imageBitmapLoaderListener imageBitmapLoaderListener will notify after getting actual image bitmap
     */

    public void getImageBitmap(Uri uri, int imageCompressionRate, String resultedImagePath,
                               ImageBitmapLoaderListener imageBitmapLoaderListener) {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageBitmapCompressionAsyncTask(mContext, getPath(uri), imageCompressionRate, resultedImagePath, imageBitmapLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    /**
     * Method to get image bitmap from selected uri or captured image uri
     *
     * @param uri                       uri of image
     * @param imageCompressionRate      compression rate from 1 to 100
     * @param resultedImagePath         path to store final result , if path is given all other image format will be ignored
     * @param scaleDownValue            scale down value for bitmap
     * @param imageBitmapLoaderListener imageBitmapLoaderListener will notify after getting actual image bitmap
     */

    public void getImageBitmap(Uri uri, int imageCompressionRate, String resultedImagePath,
                               int scaleDownValue, ImageBitmapLoaderListener imageBitmapLoaderListener) {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new ImageBitmapCompressionAsyncTask(mContext, getPath(uri), imageCompressionRate, resultedImagePath, scaleDownValue, imageBitmapLoaderListener).execute();
        } else {
            Log.e(MediaSelectionUtil.TAG, "Permission is not granted for read/write from device");
        }
    }

    public String getPath(Uri uri) {
        return getImagePath(mContext, uri);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getImagePath(Context context, Uri uri) {
        boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    //if (uri.getLastPathSegment() == null)
                        return getDataColumn(context, getDataColumnWithAuthority(context, uri, null, null), null, null);
//                    else
//                        return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                if (uri.getLastPathSegment() == null)
                    return getDataColumn(context, getDataColumnWithAuthority(context, uri, null, null), null, null);
                else
                    return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private boolean isGooglePhotosUri(Uri uri) {
        //return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        return uri.getAuthority().contains("com.google.android.apps");
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;

        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        try {

            int currentApiVersion = Build.VERSION.SDK_INT;
            if (currentApiVersion > Build.VERSION_CODES.M && uri.toString().contains(mContext.getString(R.string.app_provider))) {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    String state = Environment.getExternalStorageState();
                    File file;
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", cursor.getString(column_index));
                    } else {
                        file = new File(context.getFilesDir(), cursor.getString(column_index));
                    }
                    return file.getAbsolutePath();
                }
            } else {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private Uri getDataColumnWithAuthority(Context context, Uri uri, String selection, String[] selectionArgs) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bmp = BitmapFactory.decodeStream(is);
            return getImageUri(context, bmp);
        }
        return null;
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * set Image URI for image capture from camera
     *
     * @param context Context
     */
    public Uri setUri(Context context) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", context.getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".jpeg");
            int currentApiVersion = Build.VERSION.SDK_INT;
            if (currentApiVersion > Build.VERSION_CODES.M) {
//                sImgUri = FileProvider.getUriForFile(mContext,
//                        mContext.getApplicationContext().getPackageName() + ".provider", file);
                sImgUri = FileProvider.getUriForFile(mContext,
                        mContext.getString(R.string.app_provider), file);
            } else {
                sImgUri = Uri.fromFile(file);
            }
        } else {
            File file = new File(context.getFilesDir(), context.getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".jpeg");
            int currentApiVersion = Build.VERSION.SDK_INT;
            if (currentApiVersion > Build.VERSION_CODES.M) {
//                sImgUri = FileProvider.getUriForFile(mContext,
//                        mContext.getApplicationContext().getPackageName() + ".provider", file);
                sImgUri = FileProvider.getUriForFile(mContext,
                        mContext.getString(R.string.app_provider), file);
            } else {
                sImgUri = Uri.fromFile(file);
            }
        }
        return sImgUri;
    }

    /**
     * set Image URI with specific path for image capture from camera
     *
     * @param path full path of image
     * @return Uri from path
     */

    public Uri setUri(String path) {
        File file = new File(path);
        Uri myImageUri;
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.M) {
//            myImageUri = FileProvider.getUriForFile(mContext,
//                    mContext.getApplicationContext().getPackageName() + ".provider", file);
            myImageUri = FileProvider.getUriForFile(mContext,
                    mContext.getString(R.string.app_provider), file);
        } else {
            myImageUri = Uri.fromFile(file);
        }
        return myImageUri;
    }

    /**
     * Get Image Uri set before image capture from Camera
     *
     * @return uri
     */

    public Uri getUri() {
        return sImgUri;
    }

    /**
     * Get Uri from path
     *
     * @param path path
     * @return Uri from path
     */

    public Uri getImageUriFromPath(String path) {
        File file = new File(path);
        Uri myImageUri;
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.M) {
//            myImageUri = FileProvider.getUriForFile(mContext,
//                    mContext.getApplicationContext().getPackageName() + ".provider", file);
            myImageUri = FileProvider.getUriForFile(mContext,
                    mContext.getString(R.string.app_provider), file);
        } else {
            myImageUri = Uri.fromFile(file);
        }
        return myImageUri;
    }

    /**
     * Get Image path from Uri
     *
     * @param uri uri
     * @return path from Uri
     */

    public String getImagePathFromUri(Uri uri) {
        return getImagePath(mContext, uri);
    }

    /**
     * Get bitmap from path with Config value of REG_565
     *
     * @param path path
     * @return bitmap
     */
    public Bitmap getBitmap(String path) {
        File f = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get bitmap from uri with Config value of REG_565
     *
     * @param uri uri
     * @return bitmap
     */

    public Bitmap getBitmap(Uri uri) {
        String path = getImagePath(mContext, uri);
        File f = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get Bitmap from specified path with Config value
     *
     * @param path   path
     * @param config Bitmap.Config value
     * @return bitmap
     */


    public Bitmap getBitmap(String path, Bitmap.Config config) {
        File f = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get bitmap from uri with specified Bitmap.Config value
     *
     * @param uri    uri
     * @param config Bitmap.Config value
     * @return bitmap
     */


    public Bitmap getBitmap(Uri uri, Bitmap.Config config) {
        String path = getImagePath(mContext, uri);
        File f = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get bitmap from resource with config value of RGB_565
     *
     * @param res resource id
     * @return bitmap
     */

    public Bitmap getBitmap(int res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeResource(mContext.getResources(), res, options);
    }

    /**
     * Get bitmap from resource with specified config
     *
     * @param res    resource id
     * @param config Bitmap.Config value
     * @return bitmap
     */

    public Bitmap getBitmap(int res, Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeResource(mContext.getResources(), res, options);
    }

    /**
     * Get thumbnail from image path
     *
     * @param imagePath image path
     * @param ThumbSize thumb size
     * @return bitmap
     */

    public Bitmap getImageThumbNail(String imagePath, int ThumbSize) {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), ThumbSize, ThumbSize);
    }
}
