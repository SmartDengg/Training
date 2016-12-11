package com.smartdengg.intentfilter;

import android.app.Application;

/**
 * Created by SmartDengg on 2016/12/11.
 */

public class MyApp extends Application {

  private ParcelGenerator parcelGenerator;

  @Override public void onCreate() {
    super.onCreate();
    parcelGenerator = ParcelGenerator.initWith(this);
  }

  public ParcelGenerator getParcelGenerator() {
    return parcelGenerator;
  }
}
