package com.smartdengg.dynamictheme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView textView = (TextView) findViewById(R.id.text);
    ViewGroup rootView = (ViewGroup) textView.getParent();
    rootView.setBackgroundColor(getResources().getColor(android.R.color.white));

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    textView.setText(NavigatorActivity.class.getCanonicalName());
    textView.setTextColor(Color.BLUE);
  }
}
