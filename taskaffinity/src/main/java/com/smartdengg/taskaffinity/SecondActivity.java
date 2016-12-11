package com.smartdengg.taskaffinity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SecondActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("SecondActivity");

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        SecondActivity.this.startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
      }
    });
  }
}
