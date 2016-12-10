package com.smartdengg.dynamictheme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NavigatorActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView textView = (TextView) findViewById(R.id.text);
    ViewGroup rootView = (ViewGroup) textView.getParent();
    rootView.setBackgroundColor(getResources().getColor(android.R.color.white));

    textView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        NavigatorActivity.this.startActivity(
            new Intent(NavigatorActivity.this, SecondActivity.class));
      }
    });
  }
}
