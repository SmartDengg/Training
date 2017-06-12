package com.smartdengg.leaks;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 创建时间:  2017/03/05 16:32 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class LeakActivity extends AppCompatActivity {

  private Handler leakHandler = new Handler();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.big_bitmap_layout);
    ((TextView) findViewById(R.id.content)).setText("memory leak");

    leakHandler.postDelayed(new Runnable() {
      @Override public void run() {
        byte[] bitmap10 = new byte[10 * 1024 * 1024];
      }
    }, 5_000_000);
  }
}
