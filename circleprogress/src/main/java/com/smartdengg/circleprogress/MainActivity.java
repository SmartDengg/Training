package com.smartdengg.circleprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  private CircleProgress circleProgress;
  private ProgressHelper progressHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    circleProgress = (CircleProgress) findViewById(R.id.circleprogress);
    progressHelper = new ProgressHelper(circleProgress);

    // Start the timer.
    findViewById(R.id.startTimer).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        progressHelper.startTimer(360, 10 * 1000);
      }
    });

    // Reset the timer.
    findViewById(R.id.resetTimer).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        progressHelper.resetTimer();
      }
    });

    // Restart the timer.
    findViewById(R.id.restartTimer).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        progressHelper.restartTimer();
      }
    });
  }
}
