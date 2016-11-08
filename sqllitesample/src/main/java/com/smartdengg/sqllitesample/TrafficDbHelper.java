package com.smartdengg.sqllitesample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建时间:  2016/11/07 16:56 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class TrafficDbHelper extends SQLiteOpenHelper {

  /*initialize version：1*/
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "Traffic.db";

  private static final String TEXT_TYPE = " TEXT";
  private static final String COMMA_SEP = ",";
  private static final String SQL_CREATE_ENTRIES =
      "CREATE TABLE " + TrafficResultContract.TrafficEntry.TABLE_NAME + " (" +
          TrafficResultContract.TrafficEntry._ID + " INTEGER PRIMARY KEY," +
          TrafficResultContract.TrafficEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
          TrafficResultContract.TrafficEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
          TrafficResultContract.TrafficEntry.COLUMN_NAME_SUBTITLE + TEXT_TYPE + COMMA_SEP +
          TrafficResultContract.TrafficEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
          TrafficResultContract.TrafficEntry.COLUMN_NAME_BULL + TEXT_TYPE + COMMA_SEP +
          TrafficResultContract.TrafficEntry.COLUMN_NAME_UPDATED + TEXT_TYPE +
          " )";

  private static final String SQL_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS " + TrafficResultContract.TrafficEntry.TABLE_NAME;

  public TrafficDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_ENTRIES);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(SQL_DELETE_ENTRIES);
    this.onCreate(db);
  }

  @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    this.onUpgrade(db, oldVersion, newVersion);
  }
}
