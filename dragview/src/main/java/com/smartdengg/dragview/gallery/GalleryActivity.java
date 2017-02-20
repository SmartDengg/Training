package com.smartdengg.dragview.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.smartdengg.dragview.MarginDecoration;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageItem;
import com.smartdengg.dragview.puzzle.PuzzlePlayingActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间:  2016/12/19 16:23 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class GalleryActivity extends Activity {

  private static final int[] DRAWABLES = {
      R.drawable.number_0, R.drawable.number_1, R.drawable.number_2, R.drawable.number_3,
      R.drawable.number_4, R.drawable.number_5, R.drawable.number_6, R.drawable.icon_0,
      R.drawable.icon_1, R.drawable.icon_2, R.drawable.icon_4, R.drawable.icon_5, R.drawable.icon_6,
      R.drawable.icon_7, R.drawable.icon_8, R.drawable.icon_9
  };

  private List<ImageItem> selectedItems = new ArrayList<>(DRAWABLES.length);

  public static void start(Context context) {
    Intent intent = new Intent(context, GalleryActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gallery_layout);
    setTitle("gallery activity");

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    final List<ImageItem> items = new ArrayList<>(DRAWABLES.length);

    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = DRAWABLES.length; i < n; i++) {
      ImageItem item = new ImageItem();
      item.drawableID = DRAWABLES[i];
      items.add(item);
    }

    final GalleryAdapter galleryAdapter = new GalleryAdapter(GalleryActivity.this, items);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(galleryAdapter);
    recyclerView.setLayoutManager(new GridLayoutManager(GalleryActivity.this, 3));
    recyclerView.addItemDecoration(
        new MarginDecoration(GalleryActivity.this, R.dimen.material_2dp));

    galleryAdapter.setCallback(new GalleryAdapter.Callback() {
      @Override public void onItemClick(ImageItem imageItem) {
        if (selectedItems.contains(imageItem)) {
          selectedItems.remove(imageItem);
        } else {
          selectedItems.add(imageItem);
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

        PuzzlePlayingActivity.start(GalleryActivity.this, selectedItems);
      }
    });
  }
}
