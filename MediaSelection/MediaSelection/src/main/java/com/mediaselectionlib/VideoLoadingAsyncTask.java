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

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class VideoLoadingAsyncTask extends AsyncTask<Void, Void, String> {
    private Context mContext;
    private Uri mUri;
    private ImageLoadingUtils mImageLoadingUtils;
    private VideoLoadingUtils mVideoLoadingUtils;
    private VideoLoaderListener mVideoLoaderListener;

    public VideoLoadingAsyncTask(Uri uri, Context context, ImageLoadingUtils imageLoadingUtils, VideoLoaderListener videoLoaderListener) {
        mContext = context;
        mUri = uri;
        mImageLoadingUtils = imageLoadingUtils;
        mVideoLoaderListener = videoLoaderListener;
        mVideoLoadingUtils = new VideoLoadingUtils(context);
    }

    @Override
    protected String doInBackground(Void... params) {
        return getPath(mUri);
    }

    @Override
    protected void onPostExecute(String path) {
        super.onPostExecute(path);
        mVideoLoaderListener.onLoadingComplete(path);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
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
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(mContext, contentUri, null, null);
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

                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(mContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            int currentApiVersion = Build.VERSION.SDK_INT;
            //TODO changes to solve gallery video issue
            if (currentApiVersion > Build.VERSION_CODES.M && uri.toString().contains(mContext.getString(R.string.app_provider))) {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (cursor.getString(column_index) != null) {
                        String state = Environment.getExternalStorageState();
                        File file;
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", cursor.getString(column_index));
                        } else {
                            file = new File(context.getFilesDir(), cursor.getString(column_index));
                        }
                        return file.getAbsolutePath();
                    } else {
                        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");

                        FileDescriptor fileDescriptor = null;
                        if (parcelFileDescriptor != null) {
                            fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            InputStream inputStream = new FileInputStream(fileDescriptor);

                            BufferedInputStream reader = new BufferedInputStream(inputStream);

                            // Create an output stream to a file that you want to save to
                            String path = mImageLoadingUtils.getImagePathFromUri(mVideoLoadingUtils.setUri(context));
                            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(path));
                            byte[] buffer = new byte[1024]; // Adjust if you want
                            int bytesRead;
                            while ((bytesRead = reader.read(buffer)) != -1) {
                                outStream.write(buffer, 0, bytesRead);
                            }
                            outStream.close();
                            parcelFileDescriptor.close();
                            return path;
                        }

                        return "";

                    }
                }
            } else {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    if (cursor.getString(column_index) != null)
                        return cursor.getString(column_index);
                    else {
                        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");

                        FileDescriptor fileDescriptor = null;
                        if (parcelFileDescriptor != null) {
                            fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            InputStream inputStream = new FileInputStream(fileDescriptor);

                            BufferedInputStream reader = new BufferedInputStream(inputStream);

                            // Create an output stream to a file that you want to save to
                            String path = mImageLoadingUtils.getImagePathFromUri(mVideoLoadingUtils.setUri(context));
                            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(path));
                            byte[] buffer = new byte[1024]; // Adjust if you want
                            int bytesRead;
                            while ((bytesRead = reader.read(buffer)) != -1) {
                                outStream.write(buffer, 0, bytesRead);
                            }
                            outStream.close();
                            parcelFileDescriptor.close();
                            return path;
                        }

                        return "";

                    }
                }
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
