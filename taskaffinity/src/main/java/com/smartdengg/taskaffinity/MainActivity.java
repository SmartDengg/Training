package com.smartdengg.taskaffinity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.d(TAG, "onCreate");

    setTitle("MainActivity");

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, SecondActivity.class));
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
  }
}
