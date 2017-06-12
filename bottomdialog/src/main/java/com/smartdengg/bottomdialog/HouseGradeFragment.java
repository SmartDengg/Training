package com.smartdengg.bottomdialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * 创建时间:  2017/04/13 12:25 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class HouseGradeFragment extends DialogFragment {

  @Override public int getTheme() {
    return R.style.MyCustomTheme;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setCancelable(true);
    getDialog().setCanceledOnTouchOutside(true);
    getDialog().getWindow().setGravity(Gravity.BOTTOM);
    //getDialog().getWindow().setWindowAnimations(R.style.MyAnimation_Window);
    getDialog().getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.grade_layout, container);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Get field from view
    final EditText editText = (EditText) view.findViewById(R.id.edit_text);
    // Fetch arguments from bundle and set title
    //String title = getArguments().getString("title", "Enter Name");
    //getDialog().setTitle(title);
    // Show soft keyboard automatically and request focus to field
    editText.requestFocus();
    //noinspection ConstantConditions
    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }
}
