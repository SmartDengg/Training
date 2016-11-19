package com.smartdengg.searchview;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * http://codetheory.in/adding-search-to-android/
 * http://www.grokkingandroid.com/android-tutorial-adding-suggestions-to-search/
 * http://www.grokkingandroid.com/android-tutorial-adding-search-to-your-apps/
 * http://www.grokkingandroid.com/why-is-there-no-configuration-by-default-for-android-search/
 *
 *
 * http://www.grokkingandroid.com/adding-actionviews-to-your-actionbar/
 * http://www.grokkingandroid.com/adding-action-items-from-within-fragments/
 */
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options_menu, menu);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      // 关联检索配置和SearchView
      SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
      MenuItem menuItem = menu.findItem(R.id.search);
      SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
      searchView.setIconifiedByDefault(false);
      searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

      MenuItemCompat.OnActionExpandListener expandListener =
          new MenuItemCompat.OnActionExpandListener() {
            @Override public boolean onMenuItemActionCollapse(MenuItem item) {
              Toast.makeText(MainActivity.this, "collapses", Toast.LENGTH_LONG).show();
              return true;  // Return true to collapse action view
            }

            @Override public boolean onMenuItemActionExpand(MenuItem item) {
              Toast.makeText(MainActivity.this, "expand", Toast.LENGTH_LONG).show();
              return true;  // Return true to expand action view
            }
          };

      MenuItemCompat.setOnActionExpandListener(menuItem, expandListener);
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.search:
        //MainActivity.this.onSearchRequested();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onSearchRequested() {
    Bundle appData = new Bundle();
    appData.putString("hello", "world");
    startSearch(null, false, appData, false);
    return true;
  }
}
