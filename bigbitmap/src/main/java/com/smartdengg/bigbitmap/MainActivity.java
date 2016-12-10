package com.smartdengg.bigbitmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, NormalActivity.class));
      }
    });

    findViewById(R.id.region).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        MainActivity.this.startActivity(
            new Intent(MainActivity.this, RegionActivity.class));
      }
    });
  }
}
