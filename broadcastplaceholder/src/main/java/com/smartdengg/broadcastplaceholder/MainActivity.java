package com.smartdengg.broadcastplaceholder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  static final String TAG = MainActivity.class.getSimpleName();
  private static final String RECORD_ACTION = "message_clicked_action";
  private static final String TARGET_INTENT = "target_intent";
  Context context;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    context = MyApp.getInstance();
    context.registerReceiver(new RecordBroadcastReceiver(), new IntentFilter(RECORD_ACTION));

    findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        String title = "title";
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(RECORD_ACTION);

        Intent targetIntent = new Intent(context, NotifyActivity.class);
        targetIntent.addCategory(Intent.CATEGORY_DEFAULT);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(TARGET_INTENT, targetIntent);

        int requestCode = new Random().nextInt();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        Notification notification =
            new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())//
                .setSmallIcon(R.mipmap.ic_launcher_round)//
                .setContentTitle(title)//
                .setContentText("content")//
                .setContentIntent(pendingIntent)//
                .setAutoCancel(true)//
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(requestCode, notification);
      }
    });
  }

  class RecordBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {

      Log.d(TAG, "record");
      Intent targetIntent = intent.getParcelableExtra(TARGET_INTENT);
      context.startActivity(targetIntent);
    }
  }
}
