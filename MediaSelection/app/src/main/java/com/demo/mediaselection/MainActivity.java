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

package com.demo.mediaselection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mediaselectionlib.ImageLoadingUtils;
import com.mediaselectionlib.ImagePathLoaderListener;
import com.mediaselectionlib.VideoLoaderListener;
import com.mediaselectionlib.VideoLoadingUtils;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_CAMERA_IMAGE = 2;
    private static final int REQUEST_GALLEY_IMAGE = 3;
    private static final int REQUEST_CAMERA_VIDEO = 4;
    private static final int REQUEST_GALLERY_VIDEO = 5;

    private ImageView imgDisplayImage;
    private VideoView vidDisplayVideo;
    private Button btnGalleryImage;
    private Button btnCameraImage;
    private Button btnGalleryVideo;
    private Button btnCameraVideo;

    private ImageLoadingUtils mImageLoadingUtils;
    private VideoLoadingUtils mVideoLoadingUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgDisplayImage = (ImageView) findViewById(R.id.imgDisplayImage);
        vidDisplayVideo = (VideoView) findViewById(R.id.vidDisplayVideo);
        btnGalleryImage = (Button) findViewById(R.id.btnGallaryImage);
        btnCameraImage = (Button) findViewById(R.id.btnCameraImage);
        btnGalleryVideo = (Button) findViewById(R.id.btnGallaryVideo);
        btnCameraVideo = (Button) findViewById(R.id.btnCameraVideo);

        //initialize imageLoadingUtil instance
        mImageLoadingUtils = new ImageLoadingUtils(this);

        //initialize videoLoadingUtil instance
        mVideoLoadingUtils = new VideoLoadingUtils(this);


        btnGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),
                            REQUEST_GALLEY_IMAGE);
                } else {
                    requestStoragePermission();
                }

            }
        });
        btnCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageLoadingUtils.setUri(MainActivity.this));
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE);

                } else {
                    requestStoragePermission();
                }
            }
        });
        btnGalleryVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("video/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Video"),
                            REQUEST_GALLERY_VIDEO);
                } else {
                    requestStoragePermission();
                }
            }
        });
        btnCameraVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermission()) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoLoadingUtils.setUri(MainActivity.this));
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA_VIDEO);
                } else {
                    requestStoragePermission();
                }
            }
        });


    }

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_ID_STORAGE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_STORAGE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission granted, Click again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLEY_IMAGE) {
            if (resultCode == RESULT_OK) {
                loadImage(data.getData());
            }
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                loadImage(mImageLoadingUtils.getUri());
            }
        } else if (requestCode == REQUEST_GALLERY_VIDEO) {
            if (resultCode == RESULT_OK) {
                loadVideo(data.getData());
            }
        } else if (requestCode == REQUEST_CAMERA_VIDEO) {
            if (resultCode == RESULT_OK) {
                loadVideo(mVideoLoadingUtils.getUri());
            }
        }
    }

    public void loadImage(Uri uri) {
        mImageLoadingUtils.getImagePath(uri, new ImagePathLoaderListener() {
            @Override
            public void onLoadingComplete(String path) {
                //path - gives the image path either selected from gallery or captured one
                imgDisplayImage.setVisibility(View.VISIBLE);
                vidDisplayVideo.setVisibility(View.GONE);
                imgDisplayImage.setImageBitmap(mImageLoadingUtils.getBitmap(path));
            }
        });
    }

    private void loadVideo(final Uri data) {
        mVideoLoadingUtils.getVideoPath(data, new VideoLoaderListener() {
            @Override
            public void onLoadingComplete(String path) {
                //path - gives the video path either selected from gallery or captured one
                vidDisplayVideo.setVisibility(View.VISIBLE);
                imgDisplayImage.setVisibility(View.GONE);
                vidDisplayVideo.setVideoPath(path);
                vidDisplayVideo.start();
            }
        });
    }

}
