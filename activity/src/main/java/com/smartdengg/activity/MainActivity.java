package com.smartdengg.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.d(TAG, "onCreate");

    SparseArray<String> sparseArray = new SparseArray<>();

    sparseArray.put(0, "0");
    sparseArray.put(0, "00");
    sparseArray.put(1, "1");
    sparseArray.put(2, "2");
    sparseArray.put(3, "3");
    sparseArray.put(4, "4");

    sparseArray.append(5, "5");
    sparseArray.append(5, "55");

    sparseArray.delete(0);
    sparseArray.indexOfKey(1);

    findViewById(android.R.id.text1).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
      }
    });

    Parcel parcel = Parcel.obtain();

    ClassLoader loader = MainActivity.class.getClassLoader();
    while (loader != null) {
      System.out.println(loader.toString());
      loader = loader.getParent();
    }

    /*I/System.out: dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.smartdengg.activity-1.apk"],nativeLibraryDirectories=[/data/app-lib/com.smartdengg.activity-1, /vendor/lib, /system/lib]]]*/
    String classPath = System.getProperty("java.class.path", ".");
    String librarySearchPath = System.getProperty("java.library.path", "");

    System.out.println(classPath);
    System.out.println(librarySearchPath);
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState");
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Log.d(TAG, "onRestoreInstanceState");
  }

  @Override protected void onStart() {
    super.onStart();
    Log.d(TAG, "The activity is about to become visible.");
  }

  @Override protected void onResume() {
    super.onResume();
    // The activity has become visible (it is now "resumed").
    Log.d(TAG, "The activity has become visible (it is now \"resumed\")");
  }

  @Override protected void onPause() {
    super.onPause();
    // Another activity is taking focus (this activity is about to be "paused").
    Log.d(TAG, "Another activity is taking focus (this activity is about to be \"paused\").");
  }

  @Override protected void onStop() {
    super.onStop();
    // The activity is no longer visible (it is now "stopped")
    Log.d(TAG, "The activity is no longer visible (it is now \"stopped\")");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    // The activity is about to be destroyed.
    Log.d(TAG, "The activity is about to be destroyed");
  }
}
