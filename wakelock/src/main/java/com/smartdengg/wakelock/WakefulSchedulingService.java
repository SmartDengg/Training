package com.smartdengg.wakelock;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

/**
 * 创建时间:  2016/12/06 22:18 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class WakefulSchedulingService extends IntentService {

  public static final String TAG = WakefulSchedulingService.class.getSimpleName();
  public static final String TYPE = "type";

  private SharedPreferences preferences;
  private Vibrator vibrator;
  private long[] pattern = { 100, 400, 100, 400 };

  public WakefulSchedulingService() {
    super("WakefulSchedulingService");
  }

  @Override public void onCreate() {
    super.onCreate();
    this.preferences = getSharedPreferences("time", MODE_PRIVATE);
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
  }

  @SuppressLint("CommitPrefEdits") @Override protected void onHandleIntent(Intent intent) {
    String type = intent.getStringExtra(TYPE);
    Log.d(TAG, "WakefulSchedulingService received type = " + type);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    Date time = calendar.getTime();
    preferences.edit().putString(type + "_" + time, time.toString().trim()).commit();

    vibrator.vibrate(pattern, -1);

    WakefulReceiver.completeWakefulIntent(intent);
  }
}
