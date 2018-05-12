package com.editor.image.maria.funimageeditor.utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MyImageProcessing {


     static {
          System.loadLibrary("MyOpencvLibs");
     }

     public static native void changeRGBChannels(long addrRgba, long addrResultImage, int red,int green,int blue);
     public static native void gammaCorrection(long addrRgba, long addrResultImage, float gamma);
     public static native void sepiaFilter(long addrRgba, long addrResultImage);
     public static native void cartoonFilter(long addrRgba, long addrResultImage);
     public static native void sketchFilter(long addrRgba, long addrResultImage);
     public static native void colorMapFilter(long addrRgba, long addrResultImage, int colorMapName);
     public static native void histogramEqualizationYCbCr(long addrRgba, long addrResultImage, int channel);
     public static native void histogramEqualizationHSV(long addrRgba, long addrResultImage, int channel);
     public static native void redTonedFillter(long addrRgba, long addrResultImage, double alpha );
     public static native void greenTonedFillter(long addrRgba, long addrResultImage, double alpha );
     public static native void blueTonedFillter(long addrRgba, long addrResultImage, double alpha );
     public static native void newFilter(long addrRgba, long addrResultImage, double alpha);

     public static native void rotate(long addrRgba, long addrResultImage, float alpha , int newWidth , int newHight);
     public static native void flipOrizontaly(long addrRgba, long addrResultImage);
     public static native void flipVerticaly(long addrRgba, long addrResultImage);

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
          if(redValue != 0 || greenValue != 0 || blueValue!= 0)
              changeRGBChannels(image.getNativeObjAddr(),image.getNativeObjAddr(),redValue,greenValue,blueValue);
          if(brightness != 1.0f)
              gammaCorrection(image.getNativeObjAddr(),image.getNativeObjAddr(),brightness);
          return image;
     }


     public static Mat rotateImage(Mat image,float alpha){
          double alphaRad = alpha * Math.PI / 180.0;

          double NWL = image.width() * Math.cos(alphaRad);
          double NHL = image.width() * Math.sin(alphaRad);

          double NHU = image.height() * Math.cos(alphaRad);
          double NWR = image.height() * Math.sin(alphaRad);

          int newWidth = (int) (NWL + NWR);
          int newHeight = (int) (NHU + NHL);

          Mat resultImage =new Mat(newHeight, newWidth, CvType.CV_8UC4);
          rotate(image.getNativeObjAddr(),resultImage.getNativeObjAddr(),alpha ,newWidth,newHeight);
          return resultImage;
     }
     public static Mat flipImageVerticaly(Mat image){
          Mat resultImage = image.clone();
          flipVerticaly(image.getNativeObjAddr(),resultImage.getNativeObjAddr());
          return resultImage;
     }
     public static Mat flipImageOrizontaly(Mat image){
          Mat resultImage = image.clone();
          flipOrizontaly(image.getNativeObjAddr(),resultImage.getNativeObjAddr());
          return resultImage;
     }


}
