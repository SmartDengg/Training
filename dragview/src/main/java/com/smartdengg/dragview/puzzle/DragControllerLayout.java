package com.smartdengg.dragview.puzzle;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * 创建时间:  2017/01/05 15:29 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class DragControllerLayout extends RelativeLayout {

  private static final String TAG = DragControllerLayout.class.getSimpleName();

  private static final int BASE_SETTLE_DURATION = 256; // ms
  private static final int MAX_SETTLE_DURATION = 600; // ms

  public enum Behavior {
    NORMAL,
    EDITOR
  }

  private final List<DragView> dragViews = new ArrayList<>();
  private ViewDragHelper viewDragHelper;
  private DragController dragController;
  private Behavior currentBehavior;

  private VelocityTracker mVelocityTracker;
  private float mMaxVelocity;
  private float mMinVelocity;
  private int mActivePointerId;
  private float x;
  private float y;

  private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
    @Override public boolean tryCaptureView(View child, int pointerId) {

      boolean captured = false;
      if (child instanceof DragView
          && dragViews.contains(child)
          && ((DragView) child).isEditable()) {
        captured = true;
      }

      return currentBehavior == Behavior.EDITOR && captured;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      if (dragController != null) dragController.onViewPositionChanged((int) x, (int) y);
    }

    @Override public void onViewDragStateChanged(int state) {
      if (dragController != null) dragController.onViewDragStateChanged(state);
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      return left;
    }

    @Override public int clampViewPositionVertical(View child, int top, int dy) {
      return top;
    }

    @Override public void onViewCaptured(View capturedChild, int activePointerId) {
      super.onViewCaptured(capturedChild, activePointerId);

      //抬高当前被拖拽的View，浮于所有View之上
      capturedChild.setZ(20.0f);
      capturedChild.setAlpha(0.8f);
      ((DragView) capturedChild).getImageButton().setVisibility(View.GONE);

      if (dragController != null) dragController.onViewCaptured((DragView) capturedChild);
    }

    @Override public void onViewReleased(final View releasedChild, float xvel, float yvel) {
      super.onViewReleased(releasedChild, xvel, yvel);

      boolean swapped = false;
      if (dragController != null) {
        swapped = dragController.onViewReleased((DragView) releasedChild);
        releasedChild.postDelayed(new Runnable() {
          @Override public void run() {
            releasedChild.setAlpha(1.0f);
            releasedChild.setZ(20.0f);
          }
        }, 300);
      }

      if (!swapped) {
        DragView dragView = (DragView) releasedChild;
        viewDragHelper.settleCapturedViewAt(dragView.getStartBound().left,
            dragView.getStartBound().top);

        final int startLeft = releasedChild.getLeft();
        final int startTop = releasedChild.getTop();
        final int dx = dragView.getStartBound().left - startLeft;
        final int dy = dragView.getStartBound().top - startTop;

        final int duration = computeSettleDuration(releasedChild, dx, dy,
            (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId),
            (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId));

        //恢复View的Z轴高度
        releasedChild.postDelayed(new Runnable() {
          @Override public void run() {
            releasedChild.setAlpha(1.0f);
            releasedChild.setZ(0.0f);
            ((DragView) releasedChild).getImageButton().setVisibility(View.VISIBLE);
          }
        }, duration);
        DragControllerLayout.this.invalidate();
      }
    }

    @Override public int getViewHorizontalDragRange(View child) {
      return getMeasuredWidth() - child.getMeasuredWidth();
    }

    @Override public int getViewVerticalDragRange(View child) {
      return getMeasuredHeight() - child.getMeasuredHeight();
    }
  };

  public DragControllerLayout(Context context) {
    this(context, null, 0, 0);
  }

  public DragControllerLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0, 0);
  }

  public DragControllerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public DragControllerLayout(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    if (!isInEditMode()) init();
  }

  //@formatter:off
  /**
   * shouldInterceptTouchEvent：
   *
   * DOWN:
   *  getOrderedChildIndex(findTopChildUnder)
   *    ->onEdgeTouched
   *
   * MOVE:
   *  getOrderedChildIndex(findTopChildUnder)
   *    ->getViewHorizontalDragRange &
   *  getViewVerticalDragRange(checkTouchSlop)(MOVE中可能不止一次)
   *    ->clampViewPositionHorizontal&
   *  clampViewPositionVertical
   *    ->onEdgeDragStarted
   *    ->tryCaptureView
   *    ->onViewCaptured
   *    ->onViewDragStateChanged
   *
   * processTouchEvent:
   *
   * DOWN:
   *  getOrderedChildIndex(findTopChildUnder)
   *    ->tryCaptureView
   *    ->onViewCaptured
   *    ->onViewDragStateChanged
   *    ->onEdgeTouched
   * MOVE:
   *    ->STATE==DRAGGING:dragTo
   *    ->STATE!=DRAGGING:
   * onEdgeDragStarted
   *    ->getOrderedChildIndex(findTopChildUnder)
   *    ->getViewHorizontalDragRange&
   * getViewVerticalDragRange(checkTouchSlop)
   *    ->tryCaptureView
   *    ->onViewCaptured
   *    ->onViewDragStateChanged
   */
  //@formatter:on
  private void init() {

    this.currentBehavior = Behavior.NORMAL;
    this.viewDragHelper = ViewDragHelper.create(this, 0.5f, callback);

    final ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mVelocityTracker = VelocityTracker.obtain();
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    int actionMasked = MotionEventCompat.getActionMasked(ev);
    if (actionMasked == MotionEvent.ACTION_CANCEL || actionMasked == MotionEvent.ACTION_UP) {
      this.viewDragHelper.cancel();
      return false;
    }

    this.mVelocityTracker.addMovement(ev);
    return viewDragHelper.shouldInterceptTouchEvent(ev);
  }

  @Override public boolean onTouchEvent(MotionEvent motionEvent) {

    viewDragHelper.processTouchEvent(motionEvent);

    final int action = MotionEventCompat.getActionMasked(motionEvent);
    switch (action) {

      case MotionEvent.ACTION_DOWN: { //防止多点触控引起的bug，我们只根据ID来获取事件，并在合适的时机置空
        mActivePointerId = motionEvent.getPointerId(0);
        break;
      }

      case MotionEvent.ACTION_MOVE: {
        final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
        this.x = motionEvent.getX(pointerIndex);
        this.y = motionEvent.getY(pointerIndex);
        break;
      }

      case MotionEvent.ACTION_UP: {
        mActivePointerId = INVALID_POINTER_ID;
        break;
      }

      case MotionEvent.ACTION_CANCEL: {
        mActivePointerId = INVALID_POINTER_ID;
        break;
      }

      case MotionEvent.ACTION_POINTER_UP: {
        final int pointerIndex = motionEvent.getActionIndex();
        final int pointerId = motionEvent.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
          final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
          mActivePointerId = motionEvent.getPointerId(newPointerIndex);
        }
        break;
      }
    }
    return true;
  }

  @Override public void computeScroll() {
    super.computeScroll();
    if (viewDragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this);
  }

  public void setDragViews(List<DragView> dragViews) {
    this.dragViews.clear();
    this.dragViews.addAll(dragViews);
  }

  public void clear() {
    this.removeAllViews();
    this.dragViews.clear();
  }

  public void setDragFrameController(DragController dragController) {
    this.dragController = dragController;
  }

  public void setBehavior(Behavior behavior) {
    currentBehavior = behavior;
    this.update();
  }

  private void update() {
    for (DragView dragView : dragViews) {
      dragView.setBehavior(currentBehavior);
    }
  }

  private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
    xvel = clampMag(xvel, (int) mMinVelocity, (int) mMaxVelocity);
    yvel = clampMag(yvel, (int) mMinVelocity, (int) mMaxVelocity);
    final int absDx = Math.abs(dx);
    final int absDy = Math.abs(dy);
    final int absXVel = Math.abs(xvel);
    final int absYVel = Math.abs(yvel);
    final int addedVel = absXVel + absYVel;
    final int addedDistance = absDx + absDy;

    final float xweight = xvel != 0 ? (float) absXVel / addedVel : (float) absDx / addedDistance;
    final float yweight = yvel != 0 ? (float) absYVel / addedVel : (float) absDy / addedDistance;

    int xduration = computeAxisDuration(dx, xvel, callback.getViewHorizontalDragRange(child));
    int yduration = computeAxisDuration(dy, yvel, callback.getViewVerticalDragRange(child));

    return (int) (xduration * xweight + yduration * yweight);
  }

  private int clampMag(int value, int absMin, int absMax) {
    final int absValue = Math.abs(value);
    if (absValue < absMin) return 0;
    if (absValue > absMax) return value > 0 ? absMax : -absMax;
    return value;
  }

  private int computeAxisDuration(int delta, int velocity, int motionRange) {
    if (delta == 0) return 0;

    final int width = this.getWidth();
    final int halfWidth = width / 2;
    final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
    final float distance = halfWidth + halfWidth * distanceInfluenceForSnapDuration(distanceRatio);

    int duration;
    velocity = Math.abs(velocity);
    if (velocity > 0) {
      duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
    } else {
      final float range = (float) Math.abs(delta) / motionRange;
      duration = (int) ((range + 1) * BASE_SETTLE_DURATION);
    }
    return Math.min(duration, MAX_SETTLE_DURATION);
  }

  private float distanceInfluenceForSnapDuration(float f) {
    f -= 0.5f; // center the values about 0.
    f *= 0.3f * Math.PI / 2.0f;
    return (float) Math.sin(f);
  }

  public Behavior getBehavior() {
    return currentBehavior;
  }

  public interface DragController {

    void onViewCaptured(DragView capturedChild);

    boolean onViewReleased(DragView releasedChild);

    void onViewPositionChanged(int x, int y);

    void onViewDragStateChanged(int state);
  }
}
