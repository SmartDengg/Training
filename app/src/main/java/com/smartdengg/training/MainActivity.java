package com.smartdengg.training;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.VERTICAL;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class MainActivity extends Activity {

  private TextView immView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(VERTICAL);
    immView = new TextView(this);

    final EditText leaking = new EditText(this);

    final Button button = new Button(this);
    button.setId(1);
    button.setText("Remove EditText");
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        layout.removeView(leaking);
        layout.removeView(v);

        //v.requestFocusFromTouch();

        Button fixLeak = new Button(MainActivity.this);
        fixLeak.setId(2);
        fixLeak.setText("Fix IMM leak");
        fixLeak.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            View rootView = v.getRootView();
            // This will give focus to the button.
            rootView.requestFocusFromTouch();
          }
        });
        layout.addView(fixLeak, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
      }
    });

    layout.addView(immView, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    layout.addView(button, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    layout.addView(leaking, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

    setContentView(layout);
    updateImmView();
  }

  private void updateImmView() {
    logServedView();
    immView.postDelayed(new Runnable() {
      @Override public void run() {
        updateImmView();
      }
    }, 100);
  }

  private void logServedView() {
    try {
      Field sInstanceField = InputMethodManager.class.getDeclaredField("sInstance");
      sInstanceField.setAccessible(true);
      Object imm = sInstanceField.get(null);
      if (imm == null) return;

      Field mServedViewField = InputMethodManager.class.getDeclaredField("mServedView");
      mServedViewField.setAccessible(true);
      View servedView = (View) mServedViewField.get(imm);

      Field mCurRootViewField = InputMethodManager.class.getDeclaredField("mCurRootView");
      mCurRootViewField.setAccessible(true);
      View curRootView = (View) mCurRootViewField.get(imm);

      immView.setText("InputMethodManager.mServedView: "
          + servedView.getClass().getName()
          + "\nmCurRootView: "
          + curRootView.getClass().getName()
          + "\nAttached:"
          + servedView.isAttachedToWindow()
          + "\nId:"
          + servedView.getId());
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}