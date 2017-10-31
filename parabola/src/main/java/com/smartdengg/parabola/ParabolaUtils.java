package com.smartdengg.parabola;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 创建时间:  2017/07/06 11:36 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ParabolaUtils {

  public ParabolaUtils() {
    throw new AssertionError("No instance");
  }

  private static int[] getLocationInWindow(View view) {
    int position[] = new int[2];
    view.getLocationInWindow(position);
    return position;
  }

  static Rect getGlobalVisibleBound(View view) {

    final Point globalOffset = new Point();
    final Rect contentBound = new Rect();

    view.getRootView()
        .findViewById(android.R.id.content)
        .getGlobalVisibleRect(contentBound, globalOffset);

    final Rect bound = new Rect();

    int[] locationInWindow = getLocationInWindow(view);

    int width = view.getWidth();
    int height = view.getHeight();

    bound.left = locationInWindow[0];
    bound.top = locationInWindow[1];
    bound.right = bound.left + width;
    bound.bottom = bound.top + height;
    bound.offset(-globalOffset.x, -globalOffset.y);

    return bound;
  }

  static float calculateScale(Rect startBound, Rect finalBound) {

    float scale;
    if ((float) startBound.width() / startBound.height()
        > (float) finalBound.width() / finalBound.height()) {
      /* Extend start bounds horizontally*/
      scale = (float) finalBound.height() / startBound.height();
      float startWidth = scale * startBound.width();
      float deltaWidth = (startWidth - finalBound.width()) / 2;
      finalBound.left -= deltaWidth;
      finalBound.right += deltaWidth;
    } else {
      /* Extend start bounds vertically*/
      scale = (float) finalBound.width() / startBound.width();
      float startHeight = scale * startBound.height();
      float deltaHeight = (startHeight - finalBound.height()) / 2;
      finalBound.top -= deltaHeight;
      finalBound.bottom += deltaHeight;
    }

    return scale;
  }

  static Animator startParabolaAnimation(final View hero, Rect startBound, Rect targetBound,
      float scale) {

    if (hero == null) throw new IllegalArgumentException("hero == null");
    if (startBound == null) throw new IllegalArgumentException("startBound == null");
    if (targetBound == null) throw new IllegalArgumentException("targetBound == null");

    ViewGroup.LayoutParams layoutParams = hero.getLayoutParams();
    layoutParams.width = startBound.width();
    layoutParams.height = startBound.height();

    hero.setVisibility(View.VISIBLE);

    hero.setPivotX(0);
    hero.setPivotY(0);

    Point startPosition = new Point(startBound.left, startBound.top);
    Point endPosition = new Point(targetBound.left, targetBound.top);

    final AnimatorSet animatorSet = new AnimatorSet();
    Point controllerPoint =
        new Point((startBound.left + targetBound.left) / 2, startBound.top - 200);

    ValueAnimator valueAnimator =
        ValueAnimator.ofObject(new ParabolaEvaluator(controllerPoint), startPosition, endPosition);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        Point point = (Point) animation.getAnimatedValue();
        hero.setX(point.x);
        hero.setY(point.y);
      }
    });

    animatorSet.play(valueAnimator)
        .with(ObjectAnimator.ofFloat(hero, View.SCALE_X, 1.03f, scale))
        .with(ObjectAnimator.ofFloat(hero, View.SCALE_Y, 1.03f, scale));
    animatorSet.setDuration(1_000);
    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        ViewGroup parent = (ViewGroup) hero.getParent();
        if (parent != null) parent.removeView(hero);
      }
    });

    animatorSet.start();

    return animatorSet;
  }

  private static class ParabolaEvaluator implements TypeEvaluator<Point> {

    private Point point;

    ParabolaEvaluator(Point point) {
      this.point = point;
    }

    @Override public Point evaluate(float t, Point startValue, Point endValue) {
      int x =
          (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * point.x + t * t * endValue.x);
      int y =
          (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * point.y + t * t * endValue.y);
      return new Point(x, y);
    }
  }
}
