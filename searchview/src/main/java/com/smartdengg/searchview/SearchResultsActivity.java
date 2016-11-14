package com.smartdengg.searchview;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class SearchResultsActivity extends AppCompatActivity {

  private TextView resultTv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.search_layout);
    this.resultTv = (TextView) findViewById(R.id.result_tv);

    this.handleIntent(getIntent());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    this.setIntent(intent);

    this.handleIntent(getIntent());
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void handleIntent(Intent intent) {

    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      //通过某种方法，根据请求检索你的数据
      resultTv.setText(query);

      SearchRecentSuggestions suggestions =
          new SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY,
              MySuggestionProvider.MODE);
      suggestions.saveRecentQuery(query, null);
    }
  }
}
