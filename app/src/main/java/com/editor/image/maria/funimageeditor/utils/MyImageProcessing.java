package com.editor.image.maria.funimageeditor.utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MyImageProcessing {

    static {
        System.loadLibrary("MyOpencvLibs");
    }

    private static native void changeRGBChannels(long addrRgba, long addrResultImage, int red, int green, int blue);

    private static native void gammaCorrection(long addrRgba, long addrResultImage, float gamma);


    private static native void negativeFilter(long addrRgba, long addrResultImagea);

    private static native void binaryFilter(long addrRgba, long addrResultImagea);

    private static native void sepiaFilter(long addrRgba, long addrResultImage);

    private static native void sketchFilter(long addrRgba, long addrResultImage);

    private static native void colorMapFilter(long addrRgba, long addrResultImage, int colorMapName);



    private static native void histogramEqualizationYCbCr(long addrRgba, long addrResultImage, int channel);

    private static native void histogramEqualizationHSV(long addrRgba, long addrResultImage, int channel);



    private static native void redTonedFilter(long addrRgba, long addrResultImage, double alpha);

    private static native void greenTonedFilter(long addrRgba, long addrResultImage, double alpha);

    private static native void blueTonedFilter(long addrRgba, long addrResultImage, double alpha);



    private static native void rotate(long addrRgba, long addrResultImage, double alpha);

    private static native void flipHorizontally(long addrRgba, long addrResultImage);

    private static native void flipVertically(long addrRgba, long addrResultImage);

    private static native void twirl(long addrRgba, long addrResultImage, double alpha, double rmax);

    private static native void ripple(long addrRgba, long addrResultImage);

    private static native void spherical(long addrRgba, long addrResultImage,double rmax, double p);



    public static Mat processImage(Mat image, String currentFilter, int redValue, int greenValue, int blueValue, float brightness) {
        switch (currentFilter) {
            case "Sepia":
                sepiaFilter(image.getNativeObjAddr(), image.getNativeObjAddr());
                break;
            case "Red":
                redTonedFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 8.0f);
                break;
            case "Green":
                greenTonedFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 8.0f);
                break;
            case "Blue":
                blueTonedFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 8.0f);
                break;
            case "Magenta":
                redTonedFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 8.0f);
                blueTonedFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 8.0f);
                break;
            case "Sketch":
                sketchFilter(image.getNativeObjAddr(), image.getNativeObjAddr());
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Canny":
                Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY, 4);
                Imgproc.Canny(image, image, 80, 100);
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Winter":
                colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 0);
                break;
            case "Pink":
                colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 1);
                break;
            case "Hot":
                colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 5);
                break;
            case "Bone":
                colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 10);
                break;
            case "Ocean":
                colorMapFilter(image.getNativeObjAddr(), image.getNativeObjAddr(), 11);
                break;
            case "HEqY":
                histogramEqualizationYCbCr(image.getNativeObjAddr(), image.getNativeObjAddr(), 0);
                break;
            case "HEqS":
                histogramEqualizationHSV(image.getNativeObjAddr(), image.getNativeObjAddr(), 1);
                break;
            case "HEqV":
                histogramEqualizationHSV(image.getNativeObjAddr(), image.getNativeObjAddr(), 2);
                break;
            case "Gray":
                Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY, 4);
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Negative":
                Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY, 4);
                negativeFilter(image.getNativeObjAddr(), image.getNativeObjAddr());
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;
            case "Binary":
                Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY, 4);
                binaryFilter(image.getNativeObjAddr(), image.getNativeObjAddr());
                Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2RGB, 4);
                break;

        }
        if (redValue != 0 || greenValue != 0 || blueValue != 0)
            changeRGBChannels(image.getNativeObjAddr(), image.getNativeObjAddr(), redValue, greenValue, blueValue);
        if (brightness != 1.0f)
            gammaCorrection(image.getNativeObjAddr(), image.getNativeObjAddr(), brightness);
        return image;
    }

    public static Mat rotateImage(Mat image, float alpha) {

        Mat resultImage;
        if ((alpha > 45 && alpha < 135) || (alpha > 225 && alpha < 315)) {
            resultImage = new Mat(image.width(), image.height(), CvType.CV_8UC4);
        } else {
            resultImage = new Mat(image.height(), image.width(), CvType.CV_8UC4);

        }
        double alphaDouble = ((double) alpha);
        rotate(image.getNativeObjAddr(), resultImage.getNativeObjAddr(), alphaDouble);
        return resultImage;
    }

    public static Mat flipImageVertically(Mat image) {
        Mat resultImage = image.clone();
        flipVertically(image.getNativeObjAddr(), resultImage.getNativeObjAddr());
        return resultImage;
    }

    public static Mat flipImageHorizontally(Mat image) {
        Mat resultImage = image.clone();
        flipHorizontally(image.getNativeObjAddr(), resultImage.getNativeObjAddr());
        return resultImage;
    }

    public static Mat twirlImage(Mat image, float alpha,int rmax) {

        Mat resultImage;
        resultImage = new Mat(image.height(), image.width(), CvType.CV_8UC4);
        double alphaDouble = ((double) alpha);
        double radius;
        if(image.height()>image.width())
            radius =(double)(image.width()/20.0) * rmax;
        else
            radius =(double)(image.height()/20.0) * rmax;
        twirl(image.getNativeObjAddr(), resultImage.getNativeObjAddr(), alphaDouble,radius);
        return resultImage;
    }

    public static Mat rippleImage(Mat image) {

        Mat resultImage;
        resultImage = new Mat(image.height(), image.width(), CvType.CV_8UC4);
        ripple(image.getNativeObjAddr(), resultImage.getNativeObjAddr());
        return resultImage;
    }

    public static Mat sphericalImage(Mat image,int rmax) {

        Mat resultImage;
        resultImage = new Mat(image.height(), image.width(), CvType.CV_8UC4);
        double radius;
        if(image.height()>image.width())
            radius =(image.width()/20.0) * rmax;
        else
            radius =(image.height()/20.0) * rmax;
        spherical(image.getNativeObjAddr(), resultImage.getNativeObjAddr(), radius, 1.8f);
        return resultImage;
    }

}
