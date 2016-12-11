package com.smartdengg.taskaffinity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ThirdActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("ThirdActivity");

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        final Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        ThirdActivity.this.startActivity(intent);

        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            ThirdActivity.this.startActivity(intent);
          }
        }, 1000);
      }
    });
  }
}
