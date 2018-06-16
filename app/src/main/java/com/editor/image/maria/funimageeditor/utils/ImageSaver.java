package com.editor.image.maria.funimageeditor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class ImageSaver {
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
}
