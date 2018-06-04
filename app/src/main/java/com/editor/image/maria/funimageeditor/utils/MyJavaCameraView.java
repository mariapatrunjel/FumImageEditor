package  com.editor.image.maria.funimageeditor.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.util.AttributeSet;
import android.util.Log;

import com.editor.image.maria.funimageeditor.activities.ShowPhotoActivity;

import org.opencv.android.*;
import org.opencv.core.Mat;

import java.util.List;


public class MyJavaCameraView extends JavaCameraView{

    private static final String TAG = "MyJavaCameraView";

    public MyJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void turnTheFlashOff() {
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
    }

    public void turnTheFlashOn() {
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        mCamera.setParameters(params);
    }

    public void turnTheFlashAuto() {
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        mCamera.setParameters(params);

    }

    public void setCameraPictureSize(){
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Log.e(TAG, sizes.toString());
        // position: variable where you choose between different supported resolutions,
        // this varies from phone to phone, even the order can be different,
        // ex. ascending or descending order.
        int position = sizes.size()-1;

        mFrameWidth = (int) sizes.get(position).width;
        mFrameHeight = (int) sizes.get(position).height;

        params.setPictureSize(mFrameWidth, mFrameHeight);
        mCamera.setParameters(params); // mCamera is a Camera object
    }
    public void takePicture(final Context context, final String currentFilter,final int blueValue,final int greenValue,final int redValue,final float brightness,final Integer cameraId) {
        Camera.PictureCallback callback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                MediaActionSound sound = new MediaActionSound();
                sound.play(MediaActionSound.SHUTTER_CLICK);

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // convert Bitmap to Mat
                Mat image = new Mat();
                Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                org.opencv.android.Utils.bitmapToMat(bmp32, image);

                //process image
                if(cameraId == 1)
                    MyImageProcessing.processImage(MyImageProcessing.flipImageHorizontally(image),currentFilter,redValue, greenValue, blueValue, brightness);
                else
                    MyImageProcessing.processImage(image, currentFilter, blueValue, greenValue, redValue, brightness);

                Bitmap finalBitmap = Utils.matToFinalBitmap(image, getResources().getConfiguration().orientation, cameraId);

                //Bitmap finalBitmap = Utils.byteToFinalBitmap(data, getResources().getConfiguration().orientation, cameraId);

                Photo photo = Photo.getInstance();
                photo.setImage(finalBitmap);

                Intent intent = new Intent(context, ShowPhotoActivity.class);
                context.startActivity(intent);
            }
        };

        mCamera.takePicture(null, null, callback);
    }
}
