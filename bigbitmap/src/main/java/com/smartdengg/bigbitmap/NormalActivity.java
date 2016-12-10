package com.smartdengg.bigbitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建时间:  2016/12/08 18:20 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class NormalActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.imageview);

    ImageView imageView = (ImageView) findViewById(R.id.iv);
    imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    try {
      InputStream inputStream = getAssets().open("big.jpg");
      Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
      imageView.setImageBitmap(bitmap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
