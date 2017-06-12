package com.smartdengg.broadcastplaceholder;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * 创建时间:  2017/04/11 16:59 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class MyApp extends Application {

  @SuppressLint("StaticFieldLeak") private static Application instance = null;

  public static Application getInstance() {
    return instance;
  }

  public void onCreate() {
    instance = this;
    super.onCreate();
  }
}
