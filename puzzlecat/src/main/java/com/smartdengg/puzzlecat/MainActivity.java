package com.smartdengg.puzzlecat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.smartdengg.puzzlecat.gallery.GalleryActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    findViewById(R.id.navigate_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        GalleryActivity.start(MainActivity.this);
      }
    });
  }
}
