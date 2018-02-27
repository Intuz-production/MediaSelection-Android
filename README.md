<h1>Introduction</h1>

INTUZ is presenting a Media Selection component, which allows you to capture or select image or video from the gallery and gives you a path when it is ready to use.
Please follow below steps to integrate this control in your next project.

<br>
<h1>Features</h1>

- An easy way to manage image/video capture or select from gallery.
- It also deals with the most common issue for Samsung devices that image configuration changes or image is rotated after capture.
- It also manages URI permission issue of Nought OS.
- It also provides a method to not only image selection but also let you add compression rate, image format (like jpg, jpeg or png), scale down value and path to store final image.


<br>
<img src="Screenshots/mediaselection.gif" width=500 alt="Screenshots/mediaselection.png">

<h1>Getting Started</h1>

- Import media selection module in your project
- Add android:configChanges="orientation" with your activity in your AndroidManifest.xml
- Please get all requested permission for read and write file in your device


> Set provider in AndroidManifest.xml for supporting new changes in nougat(7.0)


```
   <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="@string/app_provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
    </provider>
```

> Declare your app provider in string.xml file

```
    <string name="app_provider">com.demo.mediaselection.provider</string>
```

> Add below code in provider_paths.xml file in xml folder

```
    <?xml version="1.0" encoding="utf-8"?>
    <paths>
        <external-path
            name="external_files"
            path="." />
    </paths>

```

> Create instance of ImageLoadingUtils and VideoLoadingUtils class


```
    private ImageLoadingUtils mImageLoadingUtils = new ImageLoadingUtils(MainActivity.this);
    private VideoLoadingUtils mVideoLoadingUtils = new VideoLoadingUtils(MainActivity.this);

```

> Declare constants to handle result on onActivityResult

```
    private static final int REQUEST_CAMERA_IMAGE = 1;
    private static final int REQUEST_GALLEY_IMAGE = 2;
    private static final int REQUEST_CAMERA_VIDEO = 3;
    private static final int REQUEST_GALLERY_VIDEO = 4;
```
> To select image from gallery

```
    MIntent galleryIntent = new Intent();
    galleryIntent.setType("image/*");
    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),REQUEST_GALLEY_IMAGE);
```
> To capture image from camera

```
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageLoadingUtils.setUri(MainActivity.this));
    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE);
```
> To select video from gallery

```
    Intent galleryIntent = new Intent();
    galleryIntent.setType("video/*");
    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(galleryIntent, "Select Video"),REQUEST_GALLERY_VIDEO);
```
> To capture video from camera

```
    Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoLoadingUtils.setUri(MainActivity.this));
    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    startActivityForResult(cameraIntent, REQUEST_CAMERA_VIDEO);
```
> override onActivityResult

```
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
                Bitmap b=mImageLoadingUtils.getBitmap(path)
               
            }
        });
    }

    private void loadVideo(final Uri data) {
        mVideoLoadingUtils.getVideoPath(data, new VideoLoaderListener() {
            @Override
            public void onLoadingComplete(String path) {
                //path - gives the video path either selected from gallery or captured one
               
            }
        });
    }
```
> override onConfigurationChanged()

```
   @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

```
> If your image doesn't display after rotation please override onRestoreInstance()


```
     @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loadImage(mImageLoadingUtils.getUri());
        loadVideo(mVideoLoadingUtils.getUri());
    }

```
<h1>Bugs and Feedback</h1>

For bugs, questions and discussions please use the Github Issues.

<br>
<h1>License</h1>

Copyright (c) 2018 Intuz Solutions Pvt Ltd.
<br><br>
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
<br><br>
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

<h1></h1>
<a href="http://www.intuz.com">
<img src="Screenshots/logo.jpg">
</a>