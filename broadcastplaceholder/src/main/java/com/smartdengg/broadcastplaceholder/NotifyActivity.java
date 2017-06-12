package com.smartdengg.broadcastplaceholder;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * 创建时间:  2017/04/11 17:04 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class NotifyActivity extends Activity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView = new TextView(this);
    textView.setText("hello");
    setContentView(textView);
  }
}
