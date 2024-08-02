#include <jni.h>
#include <string>

#include <android/bitmap.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_opencvtest_MainActivity_blurImg(JNIEnv *env, jobject thiz, jobject bitmap) {
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
    // 设置高斯模糊的内核大小和标准差
    Size kernelSize(15, 15); // 内核大小，奇数
    double sigmaX = 5.0;     // X方向的标准差
    double sigmaY = 5.0;     // Y方向的标准差

    // 高斯模糊
    GaussianBlur(image, image, kernelSize, sigmaX, sigmaY);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_opencvtest_MainActivity_nativeConvertToLineArt(JNIEnv *env, jobject thiz,
                                                                jobject bitmap) {
    AndroidBitmapInfo info;
    void *pixels;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
              info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);

//保留细节很少
//    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
//        Mat temp(info.height, info.width, CV_8UC4, pixels);
//        Mat gray;
//        cvtColor(temp, gray, COLOR_RGBA2GRAY);
//        Canny(gray, gray, 20, 70);
//        bitwise_not(gray, gray); // 反转图像
//        cvtColor(gray, temp, COLOR_GRAY2RGBA);
//    } else {
//        Mat temp(info.height, info.width, CV_8UC2, pixels);
//        Mat gray;
//        cvtColor(temp, gray, COLOR_RGB2GRAY);
//        Canny(gray, gray,20, 70);
//        bitwise_not(gray, gray); // 反转图像
//        cvtColor(gray, temp, COLOR_GRAY2RGB);
//    }

    //保留细节很多
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        GaussianBlur(gray, gray, Size(3, 3), 0.2);
        Canny(gray, gray, 20, 70);
        bitwise_not(gray, gray); // 反转图像
        cvtColor(gray, temp, COLOR_GRAY2RGBA);
    } else {
        Mat temp(info.height, info.width, CV_8UC1, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGB2GRAY);
        GaussianBlur(gray, gray, Size(3, 3), 0.2);
        Canny(gray, gray, 20, 70);
        bitwise_not(gray, gray); // 反转图像
        cvtColor(gray, temp, COLOR_GRAY2RGB);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}