package com.smartdengg.dragview.puzzle;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageItem;

/**
 * 创建时间:  2016/12/28 20:21 <br>
 * 作者:  dengwei <br>
 * 描述:  "使用拼了营销房源" - 可拖拽View
 */
public class DragView extends RelativeLayout implements View.OnClickListener {

  private static final String TAG = DragView.class.getSimpleName();
  private static final long NS_PER_MS = 1000000;
  private static final int PADDING_7PIXEL = 7;

  private ImageItem mImageItem;
  private ImageView mImageView;
  private ImageButton mRemoveButton;
  private final Rect mStartRect = new Rect();
  private final Point mGlobalOffset = new Point();
  private DragControllerLayout.Behavior currentBehavior;
  private GestureDetector gestureDetector;
  private boolean editable = true;

  private Callback mCallback;

  public DragView(Context context, ImageItem imageItem, int[] size) {
    super(context);
    this.mImageItem = imageItem;
    if (!isInEditMode()) initView();
  }

  public DragView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.initView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DragView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.initView();
  }

  private void initView() {

    LayoutInflater.from(getContext()).inflate(R.layout.puzzle_dragview_item, this, true);

    this.mImageView = (ImageView) findViewById(R.id.drag_view_display_iv);
    this.mRemoveButton = (ImageButton) findViewById(R.id.drag_view_cancel_btn);
    this.mImageView.setImageResource(mImageItem.drawableID);
    this.mRemoveButton.setOnClickListener(this);

    /*默认状态 - 标准非编辑状态*/
    this.setBehavior(DragControllerLayout.Behavior.NORMAL);

    ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
    if (viewTreeObserver.isAlive()) {
      viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override public boolean onPreDraw() {
          DragView.this.getViewTreeObserver().removeOnPreDrawListener(this);
          if (!mStartRect.isEmpty()) return true;
          initRect();
          return true;
        }
      });
    }

    // Converts 1 dip into its equivalent px
    //Resources resources = getResources();
    //int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
    //    resources.getDimension(R.dimen.material_1dp), resources.getDisplayMetrics());
    setPadding(PADDING_7PIXEL, PADDING_7PIXEL, PADDING_7PIXEL, PADDING_7PIXEL);

    this.setHapticFeedbackEnabled(true);
    this.gestureDetector =
        new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
          @Override public void onLongPress(MotionEvent e) {
            long l = e.getDownTime() / NS_PER_MS;
            if (editable
                && currentBehavior == DragControllerLayout.Behavior.NORMAL
                && mCallback != null) {

              DragView.this.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
                  HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
              mCallback.onLongPressed();
            }
          }
        });
  }

  private void initRect() {
    this.calculateGlobalRect(mStartRect);
    Log.d(TAG, mStartRect.toString());
  }

  public void getCurrentBound(Rect rect) {
    if (rect == null) throw new NullPointerException("rect == null");
    this.calculateGlobalRect(rect);
  }

  private Rect calculateGlobalRect(Rect rect) {
    DragView.this.getGlobalVisibleRect(rect);
    ((ViewGroup) getParent()).getGlobalVisibleRect(new Rect(), mGlobalOffset);
    rect.offset(-mGlobalOffset.x, -mGlobalOffset.y);
    return rect;
  }

  public ImageView getImageView() {
    return mImageView;
  }

  public ImageButton getImageButton() {
    return mRemoveButton;
  }

  public Rect getStartBound() {
    return this.mStartRect;
  }

  public void setBehavior(DragControllerLayout.Behavior behavior) {

    if ((currentBehavior = behavior) == DragControllerLayout.Behavior.NORMAL) {
      this.mRemoveButton.setVisibility(GONE);
      this.mRemoveButton.setEnabled(false);
    } else {
      this.mRemoveButton.setVisibility(VISIBLE);
      this.mRemoveButton.setEnabled(true);
    }
  }

  @Override public void onClick(View v) {
    if (mCallback != null) mCallback.onCancelClick(this, mImageItem);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return this.gestureDetector.onTouchEvent(event);
  }

  public void setCallback(Callback callback) {
    this.mCallback = callback;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public boolean isEditable() {
    return editable;
  }

  public interface Callback {

    void onLongPressed();

    void onCancelClick(DragView dragView, ImageItem imageItem);
  }
}
