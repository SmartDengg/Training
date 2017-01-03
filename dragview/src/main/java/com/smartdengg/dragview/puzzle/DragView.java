package com.smartdengg.dragview.puzzle;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageEntity;

/**
 * 创建时间:  2016/12/28 20:21 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class DragView extends RelativeLayout implements View.OnClickListener {

  private ImageEntity imageEntity;
  private ImageView imageView;
  private ImageButton imageButton;
  private final Rect rect = new Rect();
  private final Point globalOffset = new Point();

  private Callback callback;

  public DragView(Context context, ImageEntity imageEntity) {
    super(context);
    this.imageEntity = imageEntity;
    if (!isInEditMode()) initView();
  }

  public DragView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public DragView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private void initView() {

    LayoutInflater.from(getContext()).inflate(R.layout.dragview_item, this, true);

    this.imageView = (ImageView) findViewById(R.id.drag_view_display_iv);
    this.imageButton = (ImageButton) findViewById(R.id.drag_view_cancel_btn);

    this.imageView.setImageDrawable(
        getContext().getResources().getDrawable(imageEntity.drawableID));
    this.imageButton.setOnClickListener(this);
    this.imageButton.setVisibility(GONE);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    this.getGlobalVisibleRect(rect);
    getRootView().findViewById(android.R.id.content).getGlobalVisibleRect(new Rect(), globalOffset);
    this.rect.offset(-globalOffset.x, -globalOffset.y);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
  }

  @Override public void onClick(View v) {
    if (callback != null) callback.onCancelClick(this, imageEntity);
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public interface Callback {
    void onCancelClick(DragView dragView, ImageEntity imageEntity);
  }
}
