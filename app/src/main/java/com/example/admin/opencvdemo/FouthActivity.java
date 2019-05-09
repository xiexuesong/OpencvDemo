package com.example.admin.opencvdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FouthActivity extends Activity {

    private ImageView imageView;
    private String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private Bitmap bitmap_src, bitmap_mask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fouth);

        imageView = findViewById(R.id.imageView);
        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Bitmap bitmap2 = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
//        bitmap_src = BitmapFactory.decodeResource(getResources(), R.mipmap.color);
 //       bitmap_mask = BitmapFactory.decodeResource(getResources(), R.mipmap.mask);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap_src, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        //paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap_mask, 0, 0, paint);
      //  canvas.drawBitmap(bitmap2, 0, 0, paint);
        imageView.setImageBitmap(bitmap);
        saveBitmapToSD(bitmap, ROOTPATH + "/test.png");
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
