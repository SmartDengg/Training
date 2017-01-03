package com.smartdengg.loader;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Integer> {

  private static final String TAG = MainActivity.class.getSimpleName();

  private LoaderManager loaderManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loaderManager = getLoaderManager();
    Loader<Integer> loader = loaderManager.initLoader(0, null, this);
  }

  @Override public Loader<Integer> onCreateLoader(int id, Bundle args) {

    Log.d(TAG, "onCreateLoader");

    return null;
  }

  @Override public void onLoadFinished(Loader<Integer> loader, Integer data) {

    Log.d(TAG, "onLoadFinished");
  }

  @Override public void onLoaderReset(Loader<Integer> loader) {

    Log.d(TAG, "onLoaderReset");
  }
}
