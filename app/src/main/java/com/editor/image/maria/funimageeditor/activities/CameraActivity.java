package com.editor.image.maria.funimageeditor.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.editor.image.maria.funimageeditor.retained.fragments.CameraViewRetainedFragment;
import com.editor.image.maria.funimageeditor.utils.MyImageProcessing;
import com.editor.image.maria.funimageeditor.utils.MyJavaCameraView;
import com.editor.image.maria.funimageeditor.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class CameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int FLASH_MODE_OFF = 0;
    private static final int FLASH_MODE_ON = 1;
    private static final int FLASH_MODE_AUTO = 2;

    private MyJavaCameraView javaCameraView;
    private Mat mRgba;
    private String currentFilter = "Normal";
    private Integer redValue=0, greenValue = 0,blueValue = 0;
    private Float brightness = 1.0f;
    private int cameraId = 0;
    private int flashMode = FLASH_MODE_OFF;

    private static final String TAG_RETAINED_FRAGMENT = "CameraViewRetainedFragment";
    private CameraViewRetainedFragment mRetainedFragment;

    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        mRetainedFragment = (CameraViewRetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new CameraViewRetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
            mRetainedFragment.setFilter(currentFilter);
            mRetainedFragment.setRedValue(redValue);
            mRetainedFragment.setGreenValue(greenValue);
            mRetainedFragment.setBlueValue(blueValue);
            mRetainedFragment.setBrightness(brightness);
            mRetainedFragment.setCameraId(cameraId);
            mRetainedFragment.setFlashMode(flashMode);
        }

        currentFilter = mRetainedFragment.getFilter();
        redValue = mRetainedFragment.getRedValue();
        greenValue = mRetainedFragment.getGreenValue();
        blueValue = mRetainedFragment.getBlueValue();
        brightness = mRetainedFragment.getBrightness();
        cameraId = mRetainedFragment.getCameraId();
        flashMode = mRetainedFragment.getFlashMode();

        javaCameraView = findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.setCameraIndex(cameraId);
        ImageButton flashLightButton =  findViewById(R.id.flashlight);
        if(cameraId == 1)
            flashLightButton.setVisibility(View.GONE);
        else
            flashLightButton.setVisibility(View.VISIBLE);

        //setFlashModeImage();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_13, this, mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        changeFlashMode();
        javaCameraView.setCameraPictureSize();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        if(cameraId == 1)
            return   MyImageProcessing.processImage(MyImageProcessing.flipImageHorizontally(mRgba),currentFilter,redValue, greenValue, blueValue, brightness);
        return   MyImageProcessing.processImage(mRgba,currentFilter,redValue, greenValue, blueValue, brightness);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        redValue = intent.getIntExtra("red",0);
        greenValue = intent.getIntExtra("green",0);
        blueValue = intent.getIntExtra("blue",0);
        brightness =  intent.getFloatExtra("brightness",1.0f);

        mRetainedFragment.setRedValue(redValue);
        mRetainedFragment.setGreenValue(greenValue);
        mRetainedFragment.setBlueValue(blueValue);
        mRetainedFragment.setBrightness(brightness);

    }


    // Thake Picture
    public void onTakePictureClicked(View view) {
        javaCameraView.takePicture(this,currentFilter,redValue,greenValue,blueValue,brightness,cameraId);
    }


    // Settings activity
    public void onSettingsActivityClicked(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("red",redValue);
        intent.putExtra("green",greenValue);
        intent.putExtra("blue",blueValue);
        intent.putExtra("brightness",brightness);
        startActivityForResult(intent,0);

    }


    // Swap Camera
    public void onSwapCameraClicked(View view) {
        cameraId = cameraId^1;
        mRetainedFragment.setCameraId(cameraId);
        ImageButton flashLightButton = findViewById(R.id.flashlight);
        if(cameraId == 1)
            flashLightButton.setVisibility(View.GONE);
        else
            flashLightButton.setVisibility(View.VISIBLE);
        javaCameraView.disableView();
        javaCameraView.setCameraIndex(cameraId);
        javaCameraView.enableView();
    }


    // FlashLight
    public void onFlashlightClicked(View view) {
        flashMode  = (flashMode + 1)%3;
        mRetainedFragment.setFlashMode(flashMode);
        changeFlashMode();
    }


    //Filtres
    public void onNormalFilterClicked(View view) {
        changeFilter("Normal");
    }

    public void onSepiaFilterClicked(View view) {
        changeFilter("Sepia");
    }

    public void onGrayFilterClicked(View view) {
        changeFilter("Gray");
    }

    public void onNegativeFilterClicked(View view) {
        changeFilter("Negative");
    }

    public void onBinaryFilterClicked(View view) {
        changeFilter("Binary");
    }

    public void onSketchFilterClicked(View view) {
        changeFilter("Sketch");
    }

    public void onCannyFilterClicked(View view) {
        changeFilter("Canny");
    }

    public void onRedFilterClicked(View view) {
        changeFilter("Red");
    }

    public void onBlueFilterClicked(View view) {
        changeFilter("Blue");
    }

    public void onGreenFilterClicked(View view) {
        changeFilter("Green");
    }

    public void onMagentaFilterClicked(View view) {
        changeFilter("Magenta");
    }

    public void onPinkFilterClicked(View view) {
        changeFilter("Pink");
    }

    public void onHotFilterClicked(View view) {
        changeFilter("Hot");
    }

    public void onBoneFilterClicked(View view) {
        changeFilter("Bone");
    }

    public void onOceanFilterClicked(View view) {
        changeFilter("Ocean");
    }

    public void onHEqYFilterClicked(View view) {
        changeFilter("HEqY");
    }

    public void onHEqSFilterClicked(View view) {
        changeFilter("HEqS");
    }

    public void onHEqVFilterClicked(View view) {
        changeFilter("HEqV");
    }


    private void changeFilter(String filterName){
        currentFilter = filterName;
        mRetainedFragment.setFilter(currentFilter);
    }
    private void changeFlashMode(){
        if(cameraId == 0) {
            flashMode = mRetainedFragment.getFlashMode();
            ImageButton imageButton = findViewById(R.id.flashlight);
            if (flashMode == FLASH_MODE_OFF) {
                javaCameraView.turnTheFlashOff();
                imageButton.setImageResource(R.drawable.flash_light_off);
            }
            if (flashMode == FLASH_MODE_AUTO) {
                javaCameraView.turnTheFlashAuto();
                imageButton.setImageResource(R.drawable.flash_light_auto);
            }
            if (flashMode == FLASH_MODE_ON) {
                javaCameraView.turnTheFlashOn();
                imageButton.setImageResource(R.drawable.flash_light_on);
            }
        }
    }


}

