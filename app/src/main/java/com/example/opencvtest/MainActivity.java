package com.example.opencvtest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.example.opencvtest.databinding.ActivityMainBinding;
import com.example.opencvtest.util.ScaleImageView;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
//import org.opencv.core.MatOfLine;


public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("opencvtest");
    }

    private ActivityMainBinding binding;

    private ScaleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.blurImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = getBitmap();
                convertEdge(bitmap);
            }
        });
        binding.nativeImgToLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = getBitmap();
                nativeConvertToLineArt(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        });
        binding.imgToLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = getBitmap();
                detectImage(bitmap);
            }
        });

        Bitmap originalBitmap = getBitmap();
        binding.OriginalImage.setImageBitmap(originalBitmap);
        imageView = binding.showImg;
    }

    private Bitmap getBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.maliao);
        return bitmap;
    }

    //高斯模糊
    private void convertEdge(Bitmap bitmap) {
        blurImg(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    //convertToLineArt: 转成线稿
    private void detectImage(Bitmap bitmap) {
        Bitmap lineArtBitmap = convertToLineArt(bitmap);
        imageView.setImageBitmap(lineArtBitmap);
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
        Imgproc.Canny(grayMat, edges, 25, 70);

        // Invert the edges
        Mat invertedEdges = new Mat(edges.size(), CvType.CV_8UC1);
        Core.bitwise_not(edges, invertedEdges);

        // Convert Mat back to Bitmap
        Bitmap lineArtBitmap = Bitmap.createBitmap(invertedEdges.cols(), invertedEdges.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(invertedEdges, lineArtBitmap);
        return lineArtBitmap;
    }

    /**
     * A native method that is implemented by the 'yoloposedemo' native library,
     * which is packaged with this application.
     */

    //获得Canny边缘
    public native void blurImg(Object bitmap);

    public native void nativeConvertToLineArt(Object bitmap);
}