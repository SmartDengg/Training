package com.smartdengg.wakelock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.Calendar;

import static com.smartdengg.wakelock.WakefulSchedulingService.TYPE;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    Intent intent = new Intent(MainActivity.this, WakefulReceiver.class);
    intent.putExtra(TYPE, calendar.getTime().toString());
    this.sendBroadcast(intent);*/

    //Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    //long[] pattern = { 100, 400, 100, 400 };
    //vibrator.vibrate(pattern, -1);

    //this.initRepeatAlarmManager();
    //this.initDayAlarmManager();
    //this.initNightAlarmManager();

    if ( double.class.isAssignableFrom(double.class) ) {}

    Toast.makeText(this, "toast", Toast.LENGTH_LONG).show();
  }

  private void initRepeatAlarmManager() {

    AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
    Intent intent = new Intent(MainActivity.this, WakefulReceiver.class);
    intent.putExtra(TYPE, "repeat");
    PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
    alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        SystemClock.elapsedRealtime() + 60 * 1000L, 60 * 1000L, alarmIntent);
  }

  private void initDayAlarmManager() {
    AlarmManager dayAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
    Intent intent = new Intent(MainActivity.this, WakefulReceiver.class);
    intent.putExtra(TYPE, "day");
    PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    //calendar.set(Calendar.HOUR_OF_DAY, 8);
    //calendar.set(Calendar.MINUTE, 30);

    dayAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
  }

  private void initNightAlarmManager() {
    AlarmManager nightAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
    Intent intent = new Intent(MainActivity.this, WakefulReceiver.class);
    intent.putExtra(TYPE, "night");
    PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    //calendar.set(Calendar.HOUR_OF_DAY, 24);
    //calendar.set(Calendar.MINUTE, 30);

    nightAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        AlarmManager.INTERVAL_HOUR, alarmIntent);
  }
}
