package com.example.opencvtest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.opencvtest.databinding.ActivityMainBinding;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
//import org.opencv.core.MatOfLine;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("opencvtest");
    }

    private ActivityMainBinding binding;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button checkImgBtn = binding.checkImgBtn;
        checkImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadImage();
            }
        });

        Button buttonDet = binding.buttonDet;
        buttonDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                detectImage();
            }
        });

        Bitmap Original_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.code);
        ImageView Original_image=findViewById(R.id.Original_image);
        Original_image.setImageBitmap(Original_bitmap);
        imageView = binding.showImg;
    }

    private Bitmap convertToLineArt(Bitmap bitmap) {
        // Convert Bitmap to Mat
        Mat imgMat = new Mat();
        org.opencv.android.Utils.bitmapToMat(bitmap, imgMat);

        // Convert to grayscale
        Mat grayMat = new Mat();
        Imgproc.cvtColor(imgMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        // Apply GaussianBlur
        Imgproc.GaussianBlur(grayMat, grayMat, new Size(3, 3), 0);

        // Detect edges using Canny
        Mat edges = new Mat();
        Imgproc.Canny(grayMat, edges, 100, 200);

        // Invert the edges
        Mat invertedEdges = new Mat(edges.size(), CvType.CV_8UC1);
        Core.bitwise_not(edges, invertedEdges);

        // Convert Mat back to Bitmap
        Bitmap lineArtBitmap = Bitmap.createBitmap(invertedEdges.cols(), invertedEdges.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(invertedEdges, lineArtBitmap);
        return lineArtBitmap;
    }

//    public static Mat convertToSketch(Mat image) {
//        Mat gray = new Mat();
//        Mat edges = new Mat();
//
//        // 转换成灰度图
//        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
//
//        // 使用Canny算法检测边缘
//        Imgproc.Canny(gray, edges, 100, 200);
//
//        // 使用霍夫线变换检测线条
////        MatOfLine lines = new MatOfLine();
//        Mat rho = new Mat(1, edges.cols(), CvType.CV_32F);
//        Mat theta = new Mat(1, edges.cols(), CvType.CV_32F);
//        double threshold = 1;
//        double minLineLength = 0;
//        double maxLineGap = 100;
//
//        Imgproc.HoughLines(edges, rho, theta, threshold, minLineLength, maxLineGap);
//
//        // 绘制线稿
//        Mat result = new Mat(image.size(), image.type(), new Scalar(255, 255, 255));
//        for (int i = 0; i < rho.cols(); i++) {
//            double r = rho.get(0, i)[0];
//            double t = theta.get(0, i)[0];
//            double a = Math.cos(t);
//            double b = Math.sin(t);
//            double x0 = a * r;
//            double y0 = b * r;
//            Point pt1 = new Point();
//            Point pt2 = new Point();
//            double alpha = 1;
//
//            pt1.x = (int) Math.round(x0 + alpha * -b);
//            pt1.y = (int) Math.round(y0 + alpha * a);
//            pt2.x = (int) Math.round(x0 - alpha * -b);
//            pt2.y = (int) Math.round(y0 - alpha * a);
//
//            Imgproc.line(result, pt1, pt2, new Scalar(0, 0, 0), 3);
//        }
//
//        rho.release();
//        theta.release();
//        edges.release();
//        gray.release();
//
//        return result;
//    }

    private void loadImage() {
        String imageName = "dog";
        int imageResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(imageResourceId);
    }

    private void detectImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.code);
        Bitmap lineArtBitmap = convertToLineArt(bitmap);
        imageView.setImageBitmap(lineArtBitmap);

//        getEdge(bitmap);
//        imageView.setImageBitmap(bitmap);
    }

    /**
     * A native method that is implemented by the 'yoloposedemo' native library,
     * which is packaged with this application.
     */

    //获得Canny边缘
    public native void getEdge(Object bitmap);
}