package com.smartdengg.sqllitesample;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * 创建时间:  2016/11/07 18:12 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MyApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(MyApplication.this);
  }
}
