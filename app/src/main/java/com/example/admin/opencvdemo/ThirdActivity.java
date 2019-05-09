package com.example.admin.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class ThirdActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap_src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        imageView = findViewById(R.id.imageView);
    //    bitmap_src = BitmapFactory.decodeResource(getResources(), R.mipmap.mask);

        Log.i("MDL", "time:" + System.currentTimeMillis());
        imageView.setImageBitmap(getNegativePic());
        Log.i("MDL", "time2:" + System.currentTimeMillis());


    }

    /**
     * 底片效果
     */
    private Bitmap getNegativePic() {
        int width = bitmap_src.getWidth();
        int height = bitmap_src.getHeight();
        int[] oldArr = new int[width * height];
        int[] curArr = new int[width * height];
        int oldA, oldR, oldG, oldB, newA, newR, newG, newB;
        Bitmap curPic = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap_src.getPixels(oldArr, 0, width, 0, 0, width, height);//获取原图的像素值，将像素值放入oldArr中
        int index;
        int length = oldArr.length;
        for (int i = 0; i < length; i++) {
            index = oldArr[i];//依次获取某个像素点
            oldA = Color.alpha(index);//获取相应的ARGB值
            oldR = Color.red(index);
            oldG = Color.green(index);
            oldB = Color.blue(index);

            newA = oldA;
            newR = 255 - oldR;//做相应的处理，生成新的ARGB值
            newG = 255 - oldG;
            newB = 255 - oldB;

            //保证每个新的ARGB值的范围在0~255之间
            newR = (newR > 255 ? 255 : newR) < 0 ? 0 : (newR > 255 ? 255 : newR);
            newG = (newG > 255 ? 255 : newG) < 0 ? 0 : (newG > 255 ? 255 : newG);
            newB = (newB > 255 ? 255 : newB) < 0 ? 0 : (newB > 255 ? 255 : newB);
            //合成新的像素点，将新的像素点一次放入数组curArr
            curArr[i] = Color.argb(newA, newR, newG, newB);
        }
        //用curArr合成新的图片curPic
        curPic.setPixels(curArr, 0, width, 0, 0, width,height);
        return curPic;
    }

}
