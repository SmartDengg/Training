package com.smartdengg.gesturedemo;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * 创建时间:  2016/12/27 22:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class HeroView extends View {

  private static final String TAG = HeroView.class.getSimpleName();

  private VelocityTracker mVelocityTracker = null;

  public HeroView(Context context) {
    this(context, null);
  }

  public HeroView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HeroView(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public HeroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    if (!isInEditMode()) init();
  }

  private GestureDetectorCompat mDetector;

  private void init() {
    mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
  }

 /* @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    int actionMasked = MotionEventCompat.getActionMasked(ev);
    if (actionMasked == MotionEvent.ACTION_CANCEL || actionMasked == MotionEvent.ACTION_UP) {
      mDragHelper.cancel();
      return false;
    }
    return mDragHelper.shouldInterceptTouchEvent(ev);
  }*/

  @Override public boolean onTouchEvent(MotionEvent event) {

    this.mDetector.onTouchEvent(event);

    /*计算速度*/
    //this.calculateVelocity(event);

    int actionMasked = MotionEventCompat.getActionMasked(event);
    // Get the index of the pointer associated with the action.
    int actionIndex = MotionEventCompat.getActionIndex(event);
    int pointerId = event.getPointerId(actionIndex);

    int xPos = -1;
    int yPos = -1;
    if (event.getPointerCount() > 1) {
      Log.d(TAG, "Multitouch event");
      // The coordinates of the current screen contact, relative to
      // the responding View or Activity.
      xPos = (int) event.getX();
      yPos = (int) event.getY();
    } else {
      // Single touch event
      Log.d(TAG, "Single touch event");
      xPos = (int) event.getX();
      yPos = (int) event.getY();
    }

    Log.d(TAG, "xPos = " + xPos);
    Log.d(TAG, "yPos = " + yPos);

    switch (-1) {
      case (MotionEvent.ACTION_DOWN):
        Log.d(TAG, "Action was DOWN");
        return true;
      case (MotionEvent.ACTION_MOVE):
        Log.d(TAG, "Action was MOVE");
        return true;
      case (MotionEvent.ACTION_UP):
        Log.d(TAG, "Action was UP");
        return true;
      case (MotionEvent.ACTION_CANCEL):
        Log.d(TAG, "Action was CANCEL");
        return true;
      case (MotionEvent.ACTION_OUTSIDE):
        Log.d(TAG, "Movement occurred outside bounds " + "of current screen element");
        return true;
      default:
        return super.onTouchEvent(event);
    }
  }

  /**
   * 需要注意的是，我们应该在ACTION_MOVE事件，而不是在ACTION_UP事件后计算速度。在ACTION_UP事件之后，计算x、y方向上的速度都会是0。
   */
  private boolean calculateVelocity(MotionEvent event) {
    int index = event.getActionIndex();
    int action = event.getActionMasked();
    int pointerId = event.getPointerId(index);

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        if (mVelocityTracker == null) {
          // Retrieve a new VelocityTracker object to watch the velocity of a motion.
          mVelocityTracker = VelocityTracker.obtain();
        } else {
          // Reset the velocity tracker back to its initial state.
          mVelocityTracker.clear();
        }
        // Add a user's movement to the tracker.
        mVelocityTracker.addMovement(event);
        break;
      case MotionEvent.ACTION_MOVE:
        mVelocityTracker.addMovement(event);
        // When you want to determine the velocity, call
        // computeCurrentVelocity(). Then call getXVelocity()
        // and getYVelocity() to retrieve the velocity for each pointer ID.
        mVelocityTracker.computeCurrentVelocity(1000);
        // Log velocity of pixels per second
        // Best practice to use VelocityTrackerCompat where possible.
        Log.d("", "X velocity: " + VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId));
        Log.d("", "Y velocity: " + VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId));
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        // Return a VelocityTracker object back to be re-used by others.
        mVelocityTracker.recycle();
        break;
    }
    return true;
  }

  class MyGestureListener implements GestureDetector.OnGestureListener {

    @Override public boolean onDown(MotionEvent event) {
      Log.d(TAG, "onDown: " + event.toString());
      return true;
    }

    @Override public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
        float velocityY) {
      Log.d(TAG, "onFling: " + event1.toString() + event2.toString());
      return true;
    }

    @Override public void onLongPress(MotionEvent event) {
      Log.d(TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      Log.d(TAG, "onScroll: " + e1.toString() + e2.toString());
      return true;
    }

    @Override public void onShowPress(MotionEvent event) {
      Log.d(TAG, "onShowPress: " + event.toString());
    }

    @Override public boolean onSingleTapUp(MotionEvent event) {
      Log.d(TAG, "onSingleTapUp: " + event.toString());
      return true;
    }
  }
}
