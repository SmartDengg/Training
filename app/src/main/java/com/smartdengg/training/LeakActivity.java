package com.smartdengg.training;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * 创建时间:  2016/12/04 22:58 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class LeakActivity extends Activity {

  private Handler mHandler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /*TextView textView = new TextView(this);
    textView.setText("TV");
    setContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));*/

    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

    mHandler.postDelayed(new Runnable() {
      @Override public void run() {
        Toast.makeText(LeakActivity.this, "leaking", Toast.LENGTH_LONG).show();
      }
    }, 200 * 1000);
  }
}
