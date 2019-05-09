package com.example.admin.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    /***
     * opencv库 加载并初始化回调的函数
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    Log.d("MDL", "加载成功");
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    File file = new File(ROOTPATH + PATH);
                    Log.i("MDL","path:" + file.getPath());
                    if(file.exists()) {
                        Log.i("MDL","文件存在");
                        mediaMetadataRetriever.setDataSource(ROOTPATH + PATH);
                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(0);
                        imageView.setImageBitmap(bitmap);
                        Mat mat = new Mat();
                        Mat mat_dst = new Mat();
                        Utils.bitmapToMat(bitmap, mat);
                        Core.bitwise_not(mat, mat_dst);
                        Bitmap bitmap1 = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(mat_dst, bitmap1);
                        saveBitmapToSD(bitmap1, ROOTPATH + "/test.png");
                    }else {
                        Log.i("MDL","文件不粗在");
                    }
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.d("MDL", "加载失败");
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        /*if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }*/

    }

    private MediaMetadataRetriever mediaMetadataRetriever;
    private String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String PATH = "/xiaoYa_mvMask.mp4";
    private String PATH2 = "/xiaoYa_mvColor.mp4";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaMetadataRetriever = new MediaMetadataRetriever();
         //       mediaMetadataRetriever.setDataSource(ROOTPATH + PATH);
               // Bitmap bitmap_mask = mediaMetadataRetriever.getFrameAtTime(0);
              //  saveBitmapToSD(bitmap_mask,ROOTPATH + "/mask.png");
                mediaMetadataRetriever.setDataSource(ROOTPATH + PATH2);
                Bitmap bitmap_color = mediaMetadataRetriever.getFrameAtTime(0);
                saveBitmapToSD(bitmap_color,ROOTPATH +  "/color.png");
            }
        });



    }

    /**
     * 保存图片到指定路劲上去 并且调整图片质量
     */
    private void saveBitmapToSD(Bitmap bitmap, String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
