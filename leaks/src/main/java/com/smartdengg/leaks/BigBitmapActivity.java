package com.smartdengg.leaks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 创建时间:  2017/03/05 16:32 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class BigBitmapActivity extends AppCompatActivity {

  private byte[] bitmap1 = new byte[1024 * 1024];
  private byte[] bitmap2 = new byte[2 * 1024 * 1024];
  private byte[] bitmap3 = new byte[3 * 1024 * 1024];
  private byte[] bitmap4 = new byte[4 * 1024 * 1024];
  private byte[] bitmap10 = new byte[10 * 1024 * 1024];

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.big_bitmap_layout);
    ((TextView) findViewById(R.id.content)).setText("big bitmap");
  }
}
