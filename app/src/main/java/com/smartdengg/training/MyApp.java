package com.smartdengg.training;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * 创建时间:  2016/12/04 23:24 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class MyApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
  }
}
