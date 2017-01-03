package com.smartdengg.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 创建时间:  2016/11/11 16:22 <br>
 * 作者:  dengwei <br>
 * 描述:  正方形ImageView
 */
public class SquareImageView extends ImageView {
  public SquareImageView(Context context) {
    super(context);
  }

  public SquareImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int width = getMeasuredWidth();
    setMeasuredDimension(width, width);
  }
}
