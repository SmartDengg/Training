package com.smartdengg.indicators;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private List<Fragment> fragments = new ArrayList<>(2);
  private ViewPager viewPager;

  private TextView secondTv;
  private TextView recentTv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.viewPager = (ViewPager) findViewById(R.id.viewpager);
    this.secondTv = (TextView) findViewById(R.id.second);
    this.recentTv = (TextView) findViewById(R.id.recent);




    fragments.add(new FragmentA());
    fragments.add(new FragmentB());
    FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(2);
  }

  //http://stackoverflow.com/questions/18747975/difference-between-fragmentpageradapter-and-fragmentstatepageradapter
  static class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
      super(fm);
      this.fragments = fragments;
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return fragments.size();
    }

    @Override public CharSequence getPageTitle(int position) {
      return fragments.get(position).getClass().getSimpleName();
    }
  }
}
