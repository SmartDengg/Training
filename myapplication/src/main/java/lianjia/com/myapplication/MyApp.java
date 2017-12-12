package lianjia.com.myapplication;

import android.app.Application;

/**
 * 创建时间:  2017/11/14 11:57 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MyApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Thread.setDefaultUncaughtExceptionHandler(new FatalExceptionInterceptHandler(this));
  }
}
