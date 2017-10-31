/*
* Copyright (C) 2012 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.example.android.immersivemode;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import com.example.android.common.logger.Log;

public class ImmersiveModeFragment extends Fragment {

  public static final String TAG = "ImmersiveModeFragment";
  int newUiOptions;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final View decorView = getActivity().getWindow().getDecorView();
    decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
      @Override public void onSystemUiVisibilityChange(int i) {
        int height = decorView.getHeight();
        Log.i(TAG, "Current height: " + height);

        if (Build.VERSION.SDK_INT >= 14) {
          Log.i(TAG, "SYSTEM_UI_FLAG_HIDE_NAVIGATION = " + String.valueOf(
              (newUiOptions | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == newUiOptions));
        }

        if (Build.VERSION.SDK_INT >= 16) {
          Log.i(TAG, "SYSTEM_UI_FLAG_FULLSCREEN = " + String.valueOf(
              (newUiOptions | View.SYSTEM_UI_FLAG_FULLSCREEN) == newUiOptions));
        }

        if (Build.VERSION.SDK_INT >= 19) {
          Log.i(TAG, "SYSTEM_UI_FLAG_IMMERSIVE_STICKY = " + String.valueOf(
              (newUiOptions & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) != 0));
        }
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.sample_action) {
      toggleHideyBar();

      /*淡化状态栏和系统栏*/
      //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      //  final View decorView = getActivity().getWindow().getDecorView();
      //  decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
      //}

      /*动态清淡化标签*/
      //final View decorView = getActivity().getWindow().getDecorView();
      //decorView.setSystemUiVisibility(0);
    }
    return true;
  }

  /**
   * Detects and toggles immersive mode (also known as "hidey bar" mode).
   */
  public void toggleHideyBar() {

    // BEGIN_INCLUDE (get_current_ui_flags)
    // The UI options currently enabled are represented by a bitfield.
    // getSystemUiVisibility() gives us that bitfield.
    int uiOptions = getActivity().getWindow().getDecorView().getSystemUiVisibility();
    newUiOptions = uiOptions;
    // END_INCLUDE (get_current_ui_flags)
    // BEGIN_INCLUDE (toggle_ui_flags)
    boolean isImmersiveModeEnabled = false;
    if (Build.VERSION.SDK_INT >= 19) {
      isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
    }
    if (isImmersiveModeEnabled) {
      Log.i(TAG, "Turning immersive mode mode off. ");
    } else {
      Log.i(TAG, "Turning immersive mode mode on.");
    }

    // Navigation bar hiding:  Backwards compatible to ICS.
    if (Build.VERSION.SDK_INT >= 14) {
      newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    // Status bar hiding: Backwards compatible to Jellybean
    if (Build.VERSION.SDK_INT >= 16) {
      newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
      //newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
      //newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    // Immersive mode: Backward compatible to KitKat.
    // Note that this flag doesn't do anything by itself, it only augments the behavior
    // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
    // all three flags are being toggled together.
    // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
    // Sticky immersive mode differs in that it makes the navigation and status bars
    // semi-transparent, and the UI flag does not get cleared when the user interacts with
    // the screen.
    if (Build.VERSION.SDK_INT >= 19) {
      //newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
      newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE;
    }

    getActivity().getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    //END_INCLUDE (set_ui_flags)
  }
}
