package com.smartdengg.puzzlecat;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 创建时间:  2016/12/19 17:10 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

  private int margin;

  public MarginDecoration(Context context) {
    margin = context.getResources().getDimensionPixelSize(R.dimen.material_1dp);
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    outRect.set(margin, margin, margin, margin);
  }
}
