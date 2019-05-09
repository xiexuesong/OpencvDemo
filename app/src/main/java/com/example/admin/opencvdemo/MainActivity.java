package com.example.admin.opencvdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaCodec;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import static org.opencv.core.CvType.CV_32FC3;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private ImageView imageView;
    private Bitmap bitmap_src;
    private Bitmap bitmap_mask;
    private Bitmap bitmap_background;
    private String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();

    }

    private void initData() {
        Resources resources = getResources();
        bitmap_src = BitmapFactory.decodeResource(resources, R.mipmap.color);
        bitmap_mask = BitmapFactory.decodeResource(resources, R.mipmap.mask);
        bitmap_background = BitmapFactory.decodeResource(resources, R.mipmap.background);
    }

    private void initEvent() {
        button.setOnClickListener(this);
    }

    private void initView() {
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
    }


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
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button:
                //      bitmap2MatDemo();
                //  xorBitmap();
                //opencv 方法
                Mat foreground = new Mat();
                Utils.bitmapToMat(bitmap_src, foreground);
                Mat background = new Mat();
                Utils.bitmapToMat(bitmap_background, background);
                Mat alpha = new Mat();
                Utils.bitmapToMat(bitmap_mask, alpha);
                foreground.convertTo(foreground, CV_32FC3);
                background.convertTo(background, CV_32FC3);

                alpha.convertTo(alpha, CV_32FC3, 1.0 / 255);
                Mat ouImage = Mat.zeros(foreground.size(), foreground.type());
                Core.multiply(alpha, foreground, foreground);
            //    Mat mat = alpha.setTo(Scalar.all(1.0));
            //    Core.add(foreground, background, ouImage);
                //这个减Alpha值有问题


                break;
            default:
                break;
        }
    }

    private void bitmap2MatDemo() {
        Bitmap bm = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);//将```图像Bitmap```加载为```ARGB_8888```方式，

        Mat m = new Mat();
        Utils.bitmapToMat(bm, m);

        Imgproc.circle(m, new Point(m.cols() / 2, m.rows() / 2), 50,
                new Scalar(255, 0, 0, 255), 2, 8, 0);

        Utils.matToBitmap(m, bm);

        imageView.setImageBitmap(bm);
    }

    private void xorBitmap() {
        int width = bitmap_mask.getWidth();
        int height = bitmap_mask.getHeight();
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap_mask, mat);
            Mat mat1 = new Mat();
            Utils.bitmapToMat(bitmap_src, mat1);
            //       Log.i("MDL", "rows:" + mat1.rows() + " rows:" + mat1.cols() + " width:" + bitmap_src.getWidth() + " height:" + bitmap_src.getHeight());
            Mat mat_dst = new Mat(bitmap_src.getWidth(), bitmap_src.getHeight(), CvType.CV_8SC1);
            Core.bitwise_xor(mat, mat1, mat_dst);
            Bitmap bitmap = Bitmap.createBitmap(bitmap_src.getWidth(), bitmap_src.getHeight(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat_dst, bitmap);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i("MDL", e.getLocalizedMessage() + "");
        }
    }


}
