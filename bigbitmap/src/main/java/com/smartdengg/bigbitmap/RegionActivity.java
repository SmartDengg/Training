package com.smartdengg.bigbitmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.smartdengg.bigbitmap.view.RegionImageView;
import java.io.IOException;
import java.io.InputStream;

public class RegionActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.region_layout);

    RegionImageView regionImageView = (RegionImageView) findViewById(R.id.region_iv);
    try {
      InputStream inputStream = getAssets().open("big.jpg");
      regionImageView.loadFromStream(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
