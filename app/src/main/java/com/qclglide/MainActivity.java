package com.qclglide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private ImageView image2;
    private ImageView image3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);

        String images1 = "https://camo.githubusercontent" +
                ".com/8374bc5b7feb5afd17c6c4962ae1ae8ca9ba2c00/687474703a2f2f61332e717069632e636e2f7073623f2f56313379796654393349326a676c2f716944526e355371636d44664f4d49576d5734766e526c354d413846572a7655525a6c775a692e38344355212f622f64427742414141414141414126626f3d62774747416741414141414346396b212672663d7669657765725f34";
        String images2 = "http://imgsrc.baidu" +
                ".com/forum/pic/item/62b8f5246b600c331463403b1a4c510fd8f9a102.jpg";

        Glide.with(this)
                .load(images1)
                .animate(R.anim.zoom_in) // or R.anim.zoom_in
                .error(R.mipmap.ic_launcher)
                .into(image);
        //        Glide.with(this)
        //                .load(images2)
        //                .skipMemoryCache(true)//禁止内存缓存：
        //                .animate(R.anim.zoom_in) // or R.anim.zoom_in
        //                .into(image2);
        //圆形裁剪
        Glide.with(this)
                .load(images2)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(image2);
        // //圆角处理
        Glide.with(this)
                .load(images2)
                //                .bitmapTransform(new GrayscaleTransformation(this))//带灰色蒙层
                .bitmapTransform(new RoundedCornersTransformation(this, 30, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(image3);
    }
}
