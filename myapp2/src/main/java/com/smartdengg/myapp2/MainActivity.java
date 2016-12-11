package com.smartdengg.myapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Log.d(TAG, "onCreate");

    TextView textView = (TextView) findViewById(android.R.id.text1);
    textView.setText(TAG);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
  }
}
