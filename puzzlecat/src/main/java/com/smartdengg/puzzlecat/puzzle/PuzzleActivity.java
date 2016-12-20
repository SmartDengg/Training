package com.smartdengg.puzzlecat.puzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.ViewAnimator;
import com.smartdengg.puzzlecat.CatEntity;
import com.smartdengg.puzzlecat.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间:  2016/12/19 17:24 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class PuzzleActivity extends Activity {

  public static final String KEY = "IntegerArrayList";

  private List<CatEntity> items;
  private RecyclerView recyclerView;
  private Bitmap bitmap;
  private ViewAnimator viewAnimator;
  private ViewGroup puzzleViewGroup;

  public static void start(Context context, List<CatEntity> list) {
    Intent intent = new Intent(context, PuzzleActivity.class);
    ArrayList<CatEntity> items = new ArrayList<>(list);
    intent.putExtra(KEY, items);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.puzzle_layout);
    setTitle("拼了");

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      //noinspection unchecked
      this.items = (List<CatEntity>) intent.getSerializableExtra(KEY);
    } else {
      //noinspection unchecked
      this.items = (List<CatEntity>) savedInstanceState.getSerializable(KEY);
    }

    this.initView();
  }

  private void initView() {

    viewAnimator = (ViewAnimator) findViewById(R.id.viewanimator);
    puzzleViewGroup = (ViewGroup) findViewById(R.id.puzzle_container);
    final ImageView imageView = (ImageView) findViewById(R.id.result_iv);
    this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    this.recyclerView.setHasFixedSize(false);

    final PuzzleAdapter puzzleAdapter = new PuzzleAdapter(PuzzleActivity.this, items);
    final GridLayoutManager gridLayoutManager = new GridLayoutManager(PuzzleActivity.this, 2);
    gridLayoutManager.setAutoMeasureEnabled(true);
    gridLayoutManager.setSmoothScrollbarEnabled(true);
    if (puzzleAdapter.isSingular()) {
      gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
          return puzzleAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
        }
      });
    }
    recyclerView.setLayoutManager(gridLayoutManager);

    ItemTouchHelper.Callback callback = new ItemDragHelperCallback(puzzleAdapter);
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
    recyclerView.setAdapter(puzzleAdapter);

    findViewById(R.id.preview_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        bitmap = retrieveViewSnapshot(findViewById(R.id.scrollview));
        viewAnimator.showNext();
        imageView.setImageBitmap(bitmap);
      }
    });
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putSerializable(KEY, (Serializable) items);
    super.onSaveInstanceState(outState);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK
        && event.getRepeatCount() == 0
        && puzzleViewGroup.getVisibility() == View.GONE) {
      viewAnimator.showPrevious();
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  public static Bitmap retrieveViewSnapshot(View view) {

    //view.setDrawingCacheEnabled(true);
    //Bitmap drawingCache = view.getDrawingCache();

    float height = view.getHeight();
    if (view instanceof RecyclerView) {
      int count = ((RecyclerView) view).getChildCount();
      height = ((RecyclerView) view).getChildAt(0).getHeight();

      if (count == 2 || count == 3) height = height * 1.5f;
      if (count == 4 || count == 5) height = height * 2f;
      if (count == 6) height = height * 2.5f;
    }

    if (view instanceof ScrollView) {
      height = ((ScrollView) view).getChildAt(0).getHeight();
    }

    if (view instanceof NestedScrollView) {
      height = ((NestedScrollView) view).getChildAt(0).getHeight();
    }

    Bitmap screenBitmap =
        Bitmap.createBitmap(view.getWidth(), (int) height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(screenBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setFlags(Paint.FILTER_BITMAP_FLAG);

    //canvas.drawBitmap(drawingCache, 0, 0, paint);
    //view.destroyDrawingCache();

    view.draw(canvas);

    return screenBitmap;
  }
}
