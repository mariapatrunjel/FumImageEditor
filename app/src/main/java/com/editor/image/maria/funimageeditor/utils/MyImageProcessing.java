package com.editor.image.maria.funimageeditor.utils;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MyImageProcessing {


     static {
         System.loadLibrary("MyOpencvLibs");
     }

     private static native void changeRGBChannels(long addrRgba, long addrResultImage, int red,int green,int blue);
     private static native void gammaCorrection(long addrRgba, long addrResultImage, float gamma);
     private static native void sepiaFilter(long addrRgba, long addrResultImage);
     private static native void cartoonFilter(long addrRgba, long addrResultImage);
     private static native void sketchFilter(long addrRgba, long addrResultImage);
     private static native void colorMapFilter(long addrRgba, long addrResultImage, int colorMapName);
     private static native void histogramEqualizationYCbCr(long addrRgba, long addrResultImage, int channel);
     private static native void histogramEqualizationHSV(long addrRgba, long addrResultImage, int channel);
     private static native void redTonedFillter(long addrRgba, long addrResultImage, double alpha );
     private static native void greenTonedFillter(long addrRgba, long addrResultImage, double alpha );
     private static native void blueTonedFillter(long addrRgba, long addrResultImage, double alpha );
     private static native void newFilter(long addrRgba, long addrResultImage, double alpha);

     private static native void rotate(long addrRgba, long addrResultImage, double alpha);
     private static native void scale(long addrRgba, long addrResultImage, double alpha);
     private static native void flip(long addrRgba, long addrResultImage);

     public static Mat processImage(Mat image, String currentFilter ,int redValue,int greenValue,int blueValue,float brightness){
          switch (currentFilter) {
               case "Sepia":
                    sepiaFilter(image.getNativeObjAddr(),image.getNativeObjAddr());
                    break;
               case "Red":
                    redTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                    break;
               case "Green":
                    greenTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                    break;
               case "Blue":
                    blueTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                    break;
               case "Magenta":
                    redTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                    blueTonedFillter(image.getNativeObjAddr(),image.getNativeObjAddr(),8.0f);
                    //changeRGBChannels(image.getNativeObjAddr(),image.getNativeObjAddr(),40,0,40);
                    break;
               case "Cartoon":
                    cartoonFilter(image.getNativeObjAddr(),image.getNativeObjAddr());
                    Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                    break;
               case "Sketch":
                    sketchFilter(image.getNativeObjAddr(),image.getNativeObjAddr());
                    Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                    break;
               case "Canny":
                    Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY, 4);
                    Imgproc.Canny(image, image, 80, 100);
                    Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                    break;
               case "Winter":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),0);
                    break;

               case "Pink":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),1);
                    break;
               case "Hot":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),5);
                    break;
               case "Bone":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),10);
                    break;
               case "Ocean":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),11);
                    break;

               case "HEqY":
                    histogramEqualizationYCbCr(image.getNativeObjAddr(), image.getNativeObjAddr(),0);
                    break;
               case "HEqCb":
                    histogramEqualizationYCbCr(image.getNativeObjAddr(), image.getNativeObjAddr(),1);
                    break;
               case "HEqS":
                    histogramEqualizationHSV(image.getNativeObjAddr(),image.getNativeObjAddr(),1);
                    break;
               case "HEqV":
                    histogramEqualizationHSV(image.getNativeObjAddr(),image.getNativeObjAddr(),2);
                    break;
               case "New":
                    newFilter(image.getNativeObjAddr(),image.getNativeObjAddr(),2);
                    break;
          }
          changeRGBChannels(image.getNativeObjAddr(),image.getNativeObjAddr(),redValue,greenValue,blueValue);
          gammaCorrection(image.getNativeObjAddr(),image.getNativeObjAddr(),brightness);
          return image;
     }


    public static Mat rotateImage(Mat image,double alpha){
         Mat resultImage = new Mat(image.height(), image.width(), CvType.CV_8UC4);
         rotate(image.getNativeObjAddr(),resultImage.getNativeObjAddr());
         return resultImage;
    }
    public static Mat flipImage(Mat image){
        Mat resultImage = new Mat(image.height(), image.width(), CvType.CV_8UC4);
        flip(image.getNativeObjAddr(),resultImage.getNativeObjAddr(),alpha);
        return resultImage;
    }
}


 /*
               case "Rainbow":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),2);
                    break;
               case "Autumn":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),3);
                    break;
               case "Jet":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),4);
                    break;
               case "Summer":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),6);
                    break;
               case "Cool":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),7);
                    break;
               case "Spring":
                    colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(),8);
                    break;
               case "HEqCr":
                    histogramEqualizationYCbCr(image.getNativeObjAddr(), image.getNativeObjAddr(),2);
                    break;
               case "HEqH":
                    histogramEqualizationHSV(image.getNativeObjAddr(),image.getNativeObjAddr(),0);
                    break;

*/