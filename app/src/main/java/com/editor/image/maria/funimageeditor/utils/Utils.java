package  com.editor.image.maria.funimageeditor.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;


public class Utils {

    public static void saveImage(Context context, Bitmap image){
        String root = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM;
        File myDir = new File(root + "/FunImageEditor");
        myDir.mkdirs();
        Date currentTime = Calendar.getInstance().getTime();
        String fName = "Image-" + currentTime.toString() + ".jpg";
        File file = new File(myDir, fName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

    }

     /*public static Bitmap byteToFinalBitmap(byte[] data, int orientation , int cameraId){

        Bitmap finalBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        */
     public static Bitmap matToFinalBitmap(Mat image, int orientation , int cameraId){
        Bitmap finalBitmap = Bitmap.createBitmap(image.width(), image.height(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(image, finalBitmap);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(cameraId == 0 )
                finalBitmap = rotateBitmap(finalBitmap, 90);
            else
                finalBitmap = rotateBitmap(finalBitmap, -90);
        }

        return finalBitmap;
    }

    private static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
