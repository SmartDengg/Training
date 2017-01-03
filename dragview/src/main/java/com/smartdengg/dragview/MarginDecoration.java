package com.smartdengg.dragview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 创建时间:  2016/12/19 17:10 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

  private int margin;

  public MarginDecoration(Context context, @DimenRes int dimen) {
    margin = context.getResources().getDimensionPixelSize(dimen);
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    outRect.set(margin, margin, margin, margin);
  }
}
