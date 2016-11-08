package com.smartdengg.sqllitesample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.Random;

import static android.provider.BaseColumns._ID;
import static com.smartdengg.sqllitesample.TrafficResultContract.COLUMN_NAME_NULLABLE;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.COLUMN_NAME_BULL;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.COLUMN_NAME_CONTENT;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.COLUMN_NAME_ENTRY_ID;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.COLUMN_NAME_SUBTITLE;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.COLUMN_NAME_TITLE;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.COLUMN_NAME_UPDATED;
import static com.smartdengg.sqllitesample.TrafficResultContract.TrafficEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  private static final String[] CONFLICT_VALUES = new String[] {
      "", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "
  };

  private SQLiteDatabase writableDatabase;
  private SQLiteDatabase readableDatabase;

  private static String[] PROJECTION = {
      _ID, COLUMN_NAME_TITLE, COLUMN_NAME_ENTRY_ID, COLUMN_NAME_TITLE, COLUMN_NAME_SUBTITLE,
      COLUMN_NAME_CONTENT, COLUMN_NAME_BULL, COLUMN_NAME_UPDATED
  };
  private TrafficDbHelper trafficDbHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.show_btn).setOnClickListener(MainActivity.this);
    findViewById(R.id.insert_btn).setOnClickListener(MainActivity.this);
    findViewById(R.id.insert_empty_btn).setOnClickListener(MainActivity.this);
    findViewById(R.id.delete_btn).setOnClickListener(MainActivity.this);
    findViewById(R.id.drop_btn).setOnClickListener(MainActivity.this);
    findViewById(R.id.update_btn).setOnClickListener(MainActivity.this);
    findViewById(R.id.retrieve_btn).setOnClickListener(MainActivity.this);

    trafficDbHelper = new TrafficDbHelper(MainActivity.this);
    this.writableDatabase = trafficDbHelper.getWritableDatabase();
    this.readableDatabase = trafficDbHelper.getReadableDatabase();
  }

  @Override public void onClick(View v) {

    int id = v.getId();

    int random = new Random().nextInt(13);

    switch (id) {

      case R.id.show_btn://显示数据库名称
        Log.d(TAG, "databaseName = " + trafficDbHelper.getDatabaseName());
        break;

      case R.id.insert_btn://插入数据

        //writableDatabase.beginTransaction();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_ENTRY_ID, "entryid:" + random);
        values.put(COLUMN_NAME_TITLE, "title:" + random);
        values.put(COLUMN_NAME_SUBTITLE, "subtitle:" + random);
        values.put(COLUMN_NAME_CONTENT, "content:" + random);
        values.put(COLUMN_NAME_BULL, "null");
        values.put(COLUMN_NAME_UPDATED, System.currentTimeMillis());

        long newRowId = writableDatabase.insert(TABLE_NAME, COLUMN_NAME_NULLABLE, values);
        Log.d(TAG, "newRowId = " + newRowId);

        //writableDatabase.endTransaction();

        break;

      case R.id.insert_empty_btn://插入空行数据

        //writableDatabase.beginTransaction();

        ContentValues emptyValues = new ContentValues();

        /*emptyValues.put(COLUMN_NAME_ENTRY_ID, "entryid:" + random);
        emptyValues.put(COLUMN_NAME_TITLE, "title:" + random);
        emptyValues.put(COLUMN_NAME_SUBTITLE, "subtitle:" + random);
        emptyValues.put(COLUMN_NAME_CONTENT, "content:" + random);
        emptyValues.put(COLUMN_NAME_UPDATED, System.currentTimeMillis());*/

        long emptyRowId = writableDatabase.insert(TABLE_NAME, COLUMN_NAME_ENTRY_ID, emptyValues);
        Log.d(TAG, "emptyRowId = " + emptyRowId);

        insertWithOnConflict(TABLE_NAME, COLUMN_NAME_ENTRY_ID, emptyValues, 0);
        insertWithOnConflict(TABLE_NAME, COLUMN_NAME_ENTRY_ID, null, 0);
        insertWithOnConflict(TABLE_NAME, null, emptyValues, 0);
        insertWithOnConflict(TABLE_NAME, null, null, 0);

        insertWithOnConflict(TABLE_NAME, COLUMN_NAME_ENTRY_ID + "," + COLUMN_NAME_TITLE, null, 0);// fault
        //writableDatabase.endTransaction();

        break;

      case R.id.delete_btn://删除数据

        // Define 'where' part of query.
        String deleteSelection = COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] deleteSelectionArgs = { String.valueOf(5) };
        // Issue SQL statement.
        writableDatabase.delete(TABLE_NAME, deleteSelection, deleteSelectionArgs);

        break;

      case R.id.drop_btn://清空数据库

        writableDatabase.beginTransaction();
        writableDatabase.delete(TABLE_NAME, null, null);
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();

        break;

      case R.id.update_btn://更新数据库

        // New value for one column
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_NAME_TITLE, "title update " + random);

        // Which row to update, based on the ID
        //String updateSelection = COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String updateSelection = COLUMN_NAME_ENTRY_ID + " like ?";
        String[] updateSelectionArgs = { String.valueOf(4) };

        int rowId =
            writableDatabase.update(TABLE_NAME, newValues, updateSelection, updateSelectionArgs);
        Log.d(TAG, "rowID = " + rowId);

        break;

      case R.id.retrieve_btn:

        Cursor cursor = null;
        try {
          String sortOrder = COLUMN_NAME_UPDATED + " DESC";
          cursor = readableDatabase.query(TABLE_NAME,    // The table to query
              null,                                      // The columns to return
              null,                                      // The columns for the WHERE clause
              null,                                      // The values for the WHERE clause
              null,                                      // don't group the rows
              null,                                      // don't filter by row groups
              sortOrder                                  // The sort order
          );

          DatabaseUtils.dumpCursor(cursor);

          int columnCount = cursor.getCount();
          cursor.moveToPosition(columnCount - 1);
          DatabaseUtils.dumpCurrentRow(cursor);

          /*cursor.moveToFirst();
          do {
            int column = cursor.getColumnIndex(KEY1);
            String value = cursor.getString(column);
            // Do sth with the `value`.
          } while (cursor.moveToNext());*/
        } finally {
          if (cursor != null && !cursor.isClosed()) cursor.close();
        }

        break;
    }
  }

  public void insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues,
      int conflictAlgorithm) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append("INSERT");
      sql.append(CONFLICT_VALUES[conflictAlgorithm]);
      sql.append(" INTO ");
      sql.append(table);
      sql.append('(');

      Object[] bindArgs = null;
      int size = (initialValues != null && initialValues.size() > 0) ? initialValues.size() : 0;

      if (size > 0) {

        bindArgs = new Object[size];
        int i = 0;

        for (String colName : initialValues.keySet()) {
          sql.append((i > 0) ? "," : "");
          sql.append(colName);
          bindArgs[i++] = initialValues.get(colName);
        }
        sql.append(')');
        sql.append(" VALUES (");
        for (i = 0; i < size; i++) {
          sql.append((i > 0) ? ",?" : "?");
        }
      } else {
        sql.append(nullColumnHack + ") VALUES (NULL");
      }
      sql.append(')');

      Log.d(TAG, "SQL:" + sql);
    } finally {
      //releaseReference();
    }
  }
}
