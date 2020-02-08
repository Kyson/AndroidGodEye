package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.robolectric.shadows.ShadowBitmapFactory;

public class Test4ImageActivity extends Activity {

    private ImageView imageView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout contentView = new LinearLayout(this);
        ImageView imageView0 = new ImageView(this);
        imageView0.setImageBitmap(ShadowBitmapFactory.create("AndroidGodEye-Bitmap", null, new Point(200, 100)));
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageBitmap(ShadowBitmapFactory.create("AndroidGodEye-Bitmap", null, new Point(200, 100)));
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageBitmap(ShadowBitmapFactory.create("AndroidGodEye-Bitmap", null, new Point(200, 100)));
        imageView3 = new ImageView(this);
        imageView3.setImageBitmap(ShadowBitmapFactory.create("AndroidGodEye-Bitmap", null, new Point(200, 100)));
        contentView.addView(imageView0);
        contentView.addView(imageView1);
        contentView.addView(imageView2);
        contentView.addView(imageView3);
        setContentView(contentView);
        imageView0.measure(50, 50);
        imageView0.layout(0, 0, 50, 50);
        imageView1.measure(500, 500);
        imageView1.layout(0, 0, 500, 500);
        imageView2.measure(210, 95);
        imageView2.layout(0, 0, 210, 95);
        imageView3.measure(50, 50);
        imageView3.layout(0, 0, 50, 50);
    }

    public ImageView imageView3() {
        return imageView3;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
