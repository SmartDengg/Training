package com.smartdengg.dynamictheme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.setTheme(R.style.AppTheme_Transparent);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView textView = (TextView) findViewById(R.id.text);

    ViewGroup rootView = (ViewGroup) textView.getParent();
    rootView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    textView.setText(NavigatorActivity.class.getCanonicalName());
    textView.setTextColor(Color.RED);

    textView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SecondActivity.this.startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
      }
    });
  }
}
