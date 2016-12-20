package com.smartdengg.puzzlecat.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.smartdengg.puzzlecat.CatEntity;
import com.smartdengg.puzzlecat.MarginDecoration;
import com.smartdengg.puzzlecat.R;
import com.smartdengg.puzzlecat.puzzle.PuzzleActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间:  2016/12/19 16:23 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class GalleryActivity extends Activity {

  private static final int[] DRAWABLES = {
      R.drawable.icon_0, R.drawable.icon_1, R.drawable.icon_2, R.drawable.icon_3, R.drawable.icon_4,
      R.drawable.icon_5, R.drawable.icon_6, R.drawable.icon_7, R.drawable.icon_8, R.drawable.icon_9
  };

  private RecyclerView mRecyclerView;
  private List<CatEntity> selectedItems = new ArrayList<>(DRAWABLES.length);

  public static void start(Context context) {
    Intent intent = new Intent(context, GalleryActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gallery_layout);
    setTitle("gallery activity");

    this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    final List<CatEntity> items = new ArrayList<>(DRAWABLES.length);

    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = DRAWABLES.length; i < n; i++) items.add(new CatEntity(DRAWABLES[i]));

    final GalleryAdapter galleryAdapter = new GalleryAdapter(GalleryActivity.this, items);
    this.mRecyclerView.setHasFixedSize(true);
    this.mRecyclerView.setAdapter(galleryAdapter);
    mRecyclerView.addItemDecoration(new MarginDecoration(GalleryActivity.this));
    this.mRecyclerView.setLayoutManager(new GridLayoutManager(GalleryActivity.this, 3));
    this.mRecyclerView.addItemDecoration(new MarginDecoration(GalleryActivity.this));

    galleryAdapter.setCallback(new GalleryAdapter.Callback() {
      @Override public void onItemClick(CatEntity catEntity) {
        if (selectedItems.contains(catEntity)) {
          selectedItems.remove(catEntity);
        } else {
          selectedItems.add(catEntity);
        }
      }
    });

    findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        if (selectedItems.size() > 6) {
          Toast.makeText(GalleryActivity.this, "最多选择六张照片啊喂", Toast.LENGTH_LONG).show();
          return;
        }

        if (selectedItems.size() == 0) {
          Toast.makeText(GalleryActivity.this, "至少选一张照片啊喂", Toast.LENGTH_LONG).show();
          return;
        }

        PuzzleActivity.start(GalleryActivity.this, selectedItems);
      }
    });
  }
}
