package com.smartdengg.bottomdialog;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        HouseGradeFragment houseGradeFragment = new HouseGradeFragment();
        houseGradeFragment.show(fm, "fragment_edit_name");
      }
    });
  }
}
