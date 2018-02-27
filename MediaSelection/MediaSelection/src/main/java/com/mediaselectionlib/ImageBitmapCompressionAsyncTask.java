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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class ImageBitmapCompressionAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private String mSelectedImagePath;
    private ImageBitmapLoaderListener mImageBitmapLoaderListener;
    private String mResultedImageFormat = "";
    private int mImageCompressionRate = 100;
    private String mResultedImagePath = "";
    private int mScaleDownValue = 500;
    private ImageLoadingUtils mImageLoadingUtils;

    ImageBitmapCompressionAsyncTask(Context context, String path, ImageBitmapLoaderListener imageBitmapLoaderListener) {
        mImageLoadingUtils = new ImageLoadingUtils(context);
        mSelectedImagePath = path;
        mImageBitmapLoaderListener = imageBitmapLoaderListener;
    }

    ImageBitmapCompressionAsyncTask(Context context, String path, String resultedImageFormat, int imageCompressionRate,
                                    ImageBitmapLoaderListener imageBitmapLoaderListener) {
        mImageLoadingUtils = new ImageLoadingUtils(context);
        mSelectedImagePath = path;
        mImageBitmapLoaderListener = imageBitmapLoaderListener;
        if (imageCompressionRate > 0) {
            mImageCompressionRate = imageCompressionRate;
        }
        mResultedImageFormat = resultedImageFormat;
    }

    ImageBitmapCompressionAsyncTask(Context context, String path, String resultedImagePath,
                                    ImageBitmapLoaderListener imageBitmapLoaderListener) {

        mImageLoadingUtils = new ImageLoadingUtils(context);
        mSelectedImagePath = path;
        mImageBitmapLoaderListener = imageBitmapLoaderListener;
        mResultedImagePath = resultedImagePath;
    }

    ImageBitmapCompressionAsyncTask(Context context, String path, int imageCompressionRate, String resultedImagePath,
                                    ImageBitmapLoaderListener imageBitmapLoaderListener) {
        mImageLoadingUtils = new ImageLoadingUtils(context);
        mSelectedImagePath = path;
        mImageBitmapLoaderListener = imageBitmapLoaderListener;
        if (imageCompressionRate > 0) {
            mImageCompressionRate = imageCompressionRate;
        }
        mResultedImagePath = resultedImagePath;
    }

    ImageBitmapCompressionAsyncTask(Context context, String path, int imageCompressionRate, String resultedImagePath,
                                    int scaleDownValue, ImageBitmapLoaderListener imageBitmapLoaderListener) {
        mImageLoadingUtils = new ImageLoadingUtils(context);
        mSelectedImagePath = path;
        mImageBitmapLoaderListener = imageBitmapLoaderListener;
        if (imageCompressionRate > 0) {
            mImageCompressionRate = imageCompressionRate;
        }
        mResultedImagePath = resultedImagePath;
        if (scaleDownValue > 0) {
            mScaleDownValue = scaleDownValue;
        }
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return getRightAngleImage(mSelectedImagePath, mResultedImageFormat, mImageCompressionRate, mResultedImagePath, mScaleDownValue);
    }

    @Override
    protected void onPostExecute(final Bitmap result) {
        super.onPostExecute(result);
        mImageBitmapLoaderListener.onLoadingComplete(result);
    }


    /**
     * Get right Angle of image
     *
     * @param photoPath            path of original image
     * @param resultedImageFormat  image format
     * @param imageCompressionRate compression rate
     * @param resultedImagePath    image path after rotation to be saved
     * @param scaleDownValue       scale down value
     * @return resulted image path
     */

    private Bitmap getRightAngleImage(String photoPath, String resultedImageFormat,
                                      int imageCompressionRate, String resultedImagePath, int scaleDownValue) {
        if (photoPath == null) {
            Log.d(MediaSelectionUtil.TAG, "Image path is null");
            return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
        }
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }
            return rotateImage(degree, photoPath, resultedImageFormat, imageCompressionRate, resultedImagePath, scaleDownValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mImageLoadingUtils.getBitmap(photoPath);
    }

    /**
     * Get image with actual rotation with degree
     *
     * @param degree               degree to be rotated for image
     * @param imagePath            path of original image
     * @param resultedImageFormat  image format
     * @param imageCompressionRate compression rate
     * @param resultedImagePath    image path after rotation to be saved
     * @param scaleDownValue       scale down value
     * @return path of resulted image
     */

    private Bitmap rotateImage(int degree, String imagePath, String resultedImageFormat, int imageCompressionRate, String resultedImagePath, int scaleDownValue) {
        try {
            Bitmap b = mImageLoadingUtils.getBitmap(imagePath);
            if (b != null) {
                Matrix matrix = new Matrix();
                if (b.getWidth() > b.getHeight()) {
                    matrix.setRotate(degree);
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                }
                b = scaleDown(b, scaleDownValue, true);

                String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);
                FileOutputStream out;
                if (!resultedImagePath.equalsIgnoreCase("")) {
//                imageName = getResultedImagePath().substring(getResultedImagePath().lastIndexOf("/") + 1);
                    imageType = resultedImagePath.substring(resultedImagePath.lastIndexOf(".") + 1);

                    out = new FileOutputStream(resultedImagePath);
                    resultedImageFormat = imageType;

                    if (resultedImageFormat.equalsIgnoreCase("png")) {
                        b.compress(Bitmap.CompressFormat.PNG, imageCompressionRate, out);
                    } else if (resultedImageFormat.equalsIgnoreCase("jpeg") || resultedImageFormat.equalsIgnoreCase("jpg")) {
                        b.compress(Bitmap.CompressFormat.JPEG, imageCompressionRate, out);
                    }

                } else {
                    out = new FileOutputStream(imagePath);
                    if (resultedImageFormat != null && resultedImageFormat.equalsIgnoreCase("")) {
                        resultedImageFormat = imageType;
                    }

                    if (resultedImageFormat.equalsIgnoreCase("png")) {
                        b.compress(Bitmap.CompressFormat.PNG, imageCompressionRate, out);
                    } else if (resultedImageFormat.equalsIgnoreCase("jpeg") || resultedImageFormat.equalsIgnoreCase("jpg")) {
                        b.compress(Bitmap.CompressFormat.JPEG, imageCompressionRate, out);
                    }
                    File file = new File(imagePath);
                    file.renameTo(new File(imageName + resultedImageFormat));
                }
                out.flush();
                out.close();
                b.recycle();
            } else {
                Log.e(MediaSelectionUtil.TAG, "Not able to create bitmap from path.Please check for read/write permission");
                return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultedImagePath.equalsIgnoreCase("")) {
            return mImageLoadingUtils.getBitmap(imagePath);
        } else {
            return mImageLoadingUtils.getBitmap(resultedImagePath);
        }
    }

    /**
     * Scale down image with maxImageSize
     *
     * @param realImage    image to be scaled
     * @param maxImageSize max scale size of image
     * @param filter       true if wanted to filter image
     * @return bitmap
     */

    private Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        Bitmap newBitmap;
        if (realImage != null) {
            if (maxImageSize != 0 && (realImage.getWidth() > maxImageSize || realImage.getHeight() > maxImageSize)) {
                float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
                int width = Math.round(ratio * realImage.getWidth());
                int height = Math.round(ratio * realImage.getHeight());
                newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
            } else {
                newBitmap = Bitmap.createScaledBitmap(realImage, realImage.getWidth(), realImage.getHeight(), filter);
            }
            return newBitmap;
        } else {
            return null;
        }
    }
}
