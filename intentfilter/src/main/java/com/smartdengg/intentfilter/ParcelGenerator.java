package com.smartdengg.intentfilter;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcel;
import java.util.List;

/**
 * Created by SmartDengg on 2016/12/11.
 */
public class ParcelGenerator {

  private static final String PACKAGE = "com.smartdengg.intentfilter";
  private static final String SHARE_CLASS = PACKAGE + ".ShareActivity";
  private static final String SENDING_CLASS = PACKAGE + ".SendingActivity";

  private static ParcelGenerator instance;
  private static Context appContext;
  private static PackageManager packageManager;

  private ParcelGenerator(Context context) {
    if (context instanceof Application) {
      appContext = context;
    } else {
      appContext = context.getApplicationContext();
    }
    packageManager = appContext.getPackageManager();
  }

  public static ParcelGenerator initWith(Context context) {
    if (context == null) throw new NullPointerException("context = null");

    if (instance == null) {
      synchronized (ParcelGenerator.class) {
        if (instance == null) instance = new ParcelGenerator(context);
      }
    }
    return instance;
  }

  public Intent createShareIntent() {

    Intent intent = new Intent();
    ComponentName componentName = null;
    Parcel parcel = Parcel.obtain();
    try {
      parcel.setDataSize(2);
      parcel.writeString(PACKAGE);
      parcel.writeString(SHARE_CLASS);
      parcel.setDataPosition(0);

      componentName = new ComponentName(parcel);
      intent.setComponent(componentName);
    } finally {
      parcel.recycle();
    }

    return intent;
  }

  public Intent createImplicitShareIntent() {

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.setDataAndType(Uri.parse("http://dengg"), "text/plain");
    //intent.setDataAndType(Uri.parse("content://dengg"), "text/plain");

    return startByChooser(intent);
  }

  private static Intent startByChooser(Intent intent) {
    if (hasHandlers(checkIntent(intent))) {
      intent = Intent.createChooser(intent, "plz choose one");
    }
    return intent;
  }

  private static Intent checkIntent(Intent intent) {
    if (intent.resolveActivity(packageManager) != null) return intent;
    throw new IllegalStateException("cannot resolve intent = " + intent.toString());
  }

  private static boolean hasHandlers(Intent intent) {
    List<ResolveInfo> handlers =
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    return !handlers.isEmpty();
  }
}
