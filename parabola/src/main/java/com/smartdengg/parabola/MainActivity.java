package com.smartdengg.parabola;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.initView();
  }

  @Override protected void onPostResume() {
    super.onPostResume();
  }

  private void initView() {

    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    final View targetView = findViewById(R.id.target);

    final Adapter adapter = new Adapter(this, RGBHelper.COLORS);
    adapter.setCallback(new Adapter.Callback() {
      @Override public void onItemClick(Integer colorValue, Rect startBound) {

        final View hero = new View(MainActivity.this);
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.addView(hero);
        hero.setBackgroundColor(colorValue);

        final Rect targetBound = ParabolaUtils.getGlobalVisibleBound(targetView);
        final float scale = ParabolaUtils.calculateScale(startBound, targetBound);
        ParabolaUtils.startParabolaAnimation(hero, startBound, targetBound, scale);
      }
    });
    final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }
}
