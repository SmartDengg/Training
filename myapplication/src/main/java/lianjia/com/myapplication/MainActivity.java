package lianjia.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //throw new NullPointerException();
    //throw new ClassNotFoundException();
    //throw new IllegalAccessException();

    findViewById(R.id.crash).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        throw new NullPointerException();
      }
    });

    findViewById(R.id.navigation).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
      }
    });
  }
}
