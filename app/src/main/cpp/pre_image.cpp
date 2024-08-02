#include <jni.h>
#include <string>

#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_opencvtest_MainActivity_blurImg(JNIEnv *env, jobject thiz, jobject bitmap) {
//    AndroidBitmapInfo info;
//    void *pixels;
//
//    AndroidBitmapInfo info;
//    void *pixels;
//
//    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
//    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
//              info.format == ANDROID_BITMAP_FORMAT_RGB_565);
//    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
//    CV_Assert(pixels);
//    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
//        Mat temp(info.height, info.width, CV_8UC4, pixels);
//        Mat gray;
//        cvtColor(temp, gray, COLOR_RGBA2GRAY);
//        Canny(gray, gray, 45, 75);
//        cvtColor(gray, temp, COLOR_GRAY2RGBA);
//    } else {
//        Mat temp(info.height, info.width, CV_8UC2, pixels);
//        Mat gray;
//        cvtColor(temp, gray, COLOR_RGB2GRAY);
//        Canny(gray, gray, 45, 75);
//        cvtColor(gray, temp, COLOR_GRAY2RGB);
//    }
//    AndroidBitmap_unlockPixels(env, bitmap);
        AndroidBitmapInfo info;
        void *pixels;

        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
        //判断图片是位图格式有RGB_565 、RGBA_8888
        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                  info.format == ANDROID_BITMAP_FORMAT_RGB_565);
        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
        CV_Assert(pixels);

        //将bitmap转化为Mat类
        Mat image(info.height, info.width, CV_8UC4, pixels);
        // 高斯模糊
        GaussianBlur(image, image, Size(101, 101), 0);
}