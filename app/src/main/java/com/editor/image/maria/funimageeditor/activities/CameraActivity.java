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
        setFlashModeImage();
        javaCameraView.setCameraPictureSize();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        switch (currentFilter) {
            case "Sepia":
                MyImageProcessing.sepiaFilter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr());
                break;
            case "Red":
                MyImageProcessing.redTonedFillter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),8.0f);
                break;
            case "Green":
                MyImageProcessing.greenTonedFillter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),8.0f);
                break;
            case "Blue":
                MyImageProcessing.blueTonedFillter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),8.0f);
                break;
            case "Magenta":
                MyImageProcessing.redTonedFillter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),8.0f);
                MyImageProcessing.blueTonedFillter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),8.0f);
                //changeRGBChannels(image.getNativeObjAddr(),image.getNativeObjAddr(),40,0,40);
                break;
            case "Cartoon":
                MyImageProcessing.cartoonFilter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr());
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Sketch":
                MyImageProcessing.sketchFilter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr());
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Canny":
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2GRAY, 4);
                Imgproc.Canny(mRgba, mRgba, 80, 100);
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Winter":
                MyImageProcessing.colorMapFilter(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),0);
                break;
            case "Pink":
                MyImageProcessing.colorMapFilter(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),1);
                break;
            case "Hot":
                MyImageProcessing.colorMapFilter(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),5);
                break;
            case "Bone":
                MyImageProcessing.colorMapFilter(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),10);
                break;
            case "Ocean":
                MyImageProcessing.colorMapFilter(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),11);
                break;
            case "HEqY":
                MyImageProcessing.histogramEqualizationYCbCr(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),0);
                break;
            case "HEqCb":
                MyImageProcessing.histogramEqualizationYCbCr(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),1);
                break;
            case "HEqS":
                MyImageProcessing.histogramEqualizationHSV(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),1);
                break;
            case "HEqV":
                MyImageProcessing.histogramEqualizationHSV(mRgba.getNativeObjAddr(), mRgba.getNativeObjAddr(),2);
                break;
            case "New":
                MyImageProcessing.newFilter(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),2);
                break;
        }
        if(redValue != 0 || greenValue != 0 || blueValue!= 0)
            MyImageProcessing.changeRGBChannels(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),redValue,greenValue,blueValue);
        if(brightness != 1.0f)
            MyImageProcessing.gammaCorrection(mRgba.getNativeObjAddr(),mRgba.getNativeObjAddr(),brightness);
        return mRgba;
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
    public void onTakePicture(View view) {
        javaCameraView.takePicture(this,currentFilter,redValue,greenValue,blueValue,brightness,cameraId);
    }


    // Settings activity
    public void onClickSettingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("red",redValue);
        intent.putExtra("green",greenValue);
        intent.putExtra("blue",blueValue);
        intent.putExtra("brightness",brightness);
        startActivityForResult(intent,0);

    }


    //Filtres
    public void onNormalFilterClicked(View view) {
        changeFilter("Normal");
    }

    public void onSepiaFilterClicked(View view) {
        changeFilter("Sepia");
    }

    public void onCartoonFilterClicked(View view) {
        changeFilter("Cartoon");
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

    public void onHEqCrFilterClicked(View view) {
        changeFilter("HEqCr");
    }

    public void onHEqSFilterClicked(View view) {
        changeFilter("HEqS");
    }

    public void onHEqVFilterClicked(View view) {
        changeFilter("HEqV");
    }

    public void onNewFilterClicked(View view) {
        changeFilter("New");
    }

    private void changeFilter(String filterName){
        currentFilter = filterName;
        mRetainedFragment.setFilter(currentFilter);
    }


    // Swap Camera
    public void onSwapCamera(View view) {
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
    public void onFlashlight(View view) {
        flashMode  = (flashMode + 1)%3;
        mRetainedFragment.setFlashMode(flashMode);
        setFlashModeImage();
    }

    private void setFlashModeImage(){
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

    public static Mat processImage(Mat image, String currentFilter ,int redValue,int greenValue,int blueValue,float brightness){
        switch (currentFilter) {
            case "Sepia":
                MyImageProcessing.sepiaFilter(image.getNativeObjAddr(),image.getNativeObjAddr());
                break;
            case "Red":
                MyImageProcessing.redTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                break;
            case "Green":
                MyImageProcessing.greenTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                break;
            case "Blue":
                MyImageProcessing.blueTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                break;
            case "Magenta":
                MyImageProcessing.redTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                MyImageProcessing.blueTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                //changeRGBChannels(image.getNativeObjAddr(),image.getNativeObjAddr(),40,0,40);
                break;
            case "Cartoon":
                MyImageProcessing.cartoonFilter(image.getNativeObjAddr(),image.getNativeObjAddr());
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Sketch":
                MyImageProcessing.sketchFilter(image.getNativeObjAddr(),image.getNativeObjAddr());
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Canny":
                Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY, 4);
                Imgproc.Canny(image, image, 80, 100);
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Winter":
                MyImageProcessing.colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),0);
                break;
            case "Pink":
                MyImageProcessing.colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),1);
                break;
            case "Hot":
                MyImageProcessing.colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),5);
                break;
            case "Bone":
                MyImageProcessing.colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),10);
                break;
            case "Ocean":
                MyImageProcessing.colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),11);
                break;
            case "HEqY":
                MyImageProcessing.histogramEqualizationYCbCr(image.getNativeObjAddr(), image.getNativeObjAddr(),0);
                break;
            case "HEqCb":
                MyImageProcessing.histogramEqualizationYCbCr(image.getNativeObjAddr(), image.getNativeObjAddr(),1);
                break;
            case "HEqS":
                MyImageProcessing.histogramEqualizationHSV(image.getNativeObjAddr(),image.getNativeObjAddr(),1);
                break;
            case "HEqV":
                MyImageProcessing.histogramEqualizationHSV(image.getNativeObjAddr(),image.getNativeObjAddr(),2);
                break;
            case "New":
                MyImageProcessing.newFilter(image.getNativeObjAddr(),image.getNativeObjAddr(),2);
                break;
        }
        MyImageProcessing.changeRGBChannels(image.getNativeObjAddr(),image.getNativeObjAddr(),redValue,greenValue,blueValue);
        MyImageProcessing.gammaCorrection(image.getNativeObjAddr(),image.getNativeObjAddr(),brightness);
        return image;
    }





}

