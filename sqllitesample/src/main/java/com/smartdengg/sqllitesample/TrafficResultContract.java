package com.smartdengg.sqllitesample;

import android.provider.BaseColumns;

/**
 * 创建时间:  2016/11/07 17:14 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public final class TrafficResultContract implements BaseColumns {

  public static final String COLUMN_NAME_NULLABLE = "NA";

  public TrafficResultContract() {
  }

  /* Inner class that defines the table contents */
  public static abstract class TrafficEntry implements BaseColumns {
    public static final String TABLE_NAME = "entry";

    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_BULL = "bull";
    public static final String COLUMN_NAME_UPDATED = "updated";
  }
}
