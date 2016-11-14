package com.smartdengg.searchview;

import android.content.SearchRecentSuggestionsProvider;

/**
 * 创建时间:  2016/11/14 15:34 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
  public final static String AUTHORITY = "com.smartdengg.searchview.MySuggestionProvider";
  public final static int MODE = DATABASE_MODE_QUERIES;

  public MySuggestionProvider() {
    setupSuggestions(AUTHORITY, MODE);
  }
}
