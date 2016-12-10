package com.smartdengg.wakelock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * 创建时间:  2016/12/06 22:44 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class WakefulReceiver extends WakefulBroadcastReceiver {

  private static final String TAG = WakefulReceiver.class.getSimpleName();

  @Override public void onReceive(Context context, Intent intent) {

    //String stringExtra = intent.getStringExtra(TYPE);
    //Log.d(TAG, "WakefulReceiver received type = " + stringExtra);

    ComponentName componentName =
        new ComponentName(context.getPackageName(), WakefulSchedulingService.class.getName());
    startWakefulService(context, (intent.setComponent(componentName)));
  }
}
