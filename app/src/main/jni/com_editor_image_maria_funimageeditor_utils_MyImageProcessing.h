#include <jni.h>
#include <stdio.h>
#include <math.h>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;


/* DO NOT EDIT THIS FILE - it is machine generated */
/* Header for class com_editor_image_maria_funimageeditor_utils_MyImageProcessing */

#ifndef _Included_com_editor_image_maria_funimageeditor_utils_MyImageProcessing
#define _Included_com_editor_image_maria_funimageeditor_utils_MyImageProcessing
#ifdef __cplusplus
#define JNICALL
extern "C" {
#endif
/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    changeRGBChannels
 * Signature: (JJIII)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_changeRGBChannels
  (JNIEnv *, jclass, jlong, jlong, jint, jint, jint);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    gammaCorrection
 * Signature: (JJF)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_gammaCorrection
  (JNIEnv *, jclass, jlong, jlong, jfloat);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    sepiaFilter
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_sepiaFilter
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    cartoonFilter
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_cartoonFilter
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    sketchFilter
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_sketchFilter
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    colorMapFilter
 * Signature: (JJI)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_colorMapFilter
  (JNIEnv *, jclass, jlong, jlong, jint);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    histogramEqualizationYCbCr
 * Signature: (JJI)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_histogramEqualizationYCbCr
  (JNIEnv *, jclass, jlong, jlong, jint);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    histogramEqualizationHSV
 * Signature: (JJI)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_histogramEqualizationHSV
  (JNIEnv *, jclass, jlong, jlong, jint);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    redTonedFillter
 * Signature: (JJD)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_redTonedFillter
  (JNIEnv *, jclass, jlong, jlong, jdouble);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    greenTonedFillter
 * Signature: (JJD)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_greenTonedFillter
  (JNIEnv *, jclass, jlong, jlong, jdouble);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    blueTonedFillter
 * Signature: (JJD)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_blueTonedFillter
  (JNIEnv *, jclass, jlong, jlong, jdouble);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    newFilter
 * Signature: (JJD)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_newFilter
  (JNIEnv *, jclass, jlong, jlong, jdouble);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    rotate
 * Signature: (JJD)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_rotate
  (JNIEnv *, jclass, jlong, jlong, jdouble);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    flipOrizontaly
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_flipOrizontaly
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     com_editor_image_maria_funimageeditor_utils_MyImageProcessing
 * Method:    flipVerticaly
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_editor_image_maria_funimageeditor_utils_MyImageProcessing_flipVerticaly
  (JNIEnv *, jclass, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
