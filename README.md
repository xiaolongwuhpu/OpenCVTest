# OpenCVTest

### 根目录下有个 opencv module 的文件夹，没有上传，需要自己下载(https://opencv.org/releases/)，当时用的版本是：OpenCV – 4.10.0，然后把压缩包中的sdk目录放到自己的根目录下命名为 opencv 
1：修改opencv/build.gradle文件，修改
```java
android {
    compileSdkVersion 34  //和自己的项目一致

    defaultConfig {
        minSdk 24   //和自己的项目一致
        targetSdk 34  //和自己的项目一致
        ...
    }
    ...
}
```  

