package com.smartdengg.circleprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 创建时间:  2017/01/03 23:17 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class CircleProgress extends View {

  private static final String TAG = CircleProgress.class.getSimpleName();

  private Paint circlePaint;
  private Paint arcPaint;
  private Point point = new Point();
  private RectF rectF = new RectF();

  private int initialColor;
  private int progressColor;

  // The point from where the color-fill animation will start.
  private int startingPointInDegrees = 270;

  // The point up-till which user wants the circle to be pre-filled.
  private float preFillAngle = 0;

  private int radius;
  private int strokedWidthPixel;

  public CircleProgress(Context context) {
    this(context, null);
  }

  public CircleProgress(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    if (!isInEditMode()) this.init(attrs);
  }

  private void init(AttributeSet attrs) {

    TypedArray typedArray = null;
    try {
      typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgress);

      // Retrieve the view attributes.
      //int circleRadiusInDp = (int) typedArray.getDimension(R.styleable.CircleProgress_radius, 5);
      int strokeWidthInDp =
          (int) typedArray.getDimension(R.styleable.CircleProgress_strokeWidth, 2);
      this.initialColor = typedArray.getColor(R.styleable.CircleProgress_initialColor,
          ContextCompat.getColor(getContext(), R.color.colorInitial));
      this.progressColor = typedArray.getColor(R.styleable.CircleProgress_progressColor,
          ContextCompat.getColor(getContext(), R.color.colorProgress));
      this.startingPointInDegrees =
          typedArray.getInt(R.styleable.CircleProgress_startingPoint, 270);
      this.preFillAngle = typedArray.getInt(R.styleable.CircleProgress_preFillPoint, 0);

      // Define the size of the circle.
      this.strokedWidthPixel = (int) convertDpIntoPixel(strokeWidthInDp);
      //this.radius = (int) convertDpIntoPixel(circleRadiusInDp);
      //this.rectF =
      //    new RectF(strokedWidthPixel, strokedWidthPixel, strokedWidthPixel + radius * 2,
      //        strokedWidthPixel + radius * 2);

      this.circlePaint = new Paint();
      this.circlePaint.setColor(initialColor);
      this.circlePaint.setAntiAlias(true);
      this.circlePaint.setStrokeWidth(strokedWidthPixel);
      this.circlePaint.setStyle(Paint.Style.STROKE);

      this.arcPaint = new Paint();
      this.arcPaint.setColor(progressColor);
      this.arcPaint.setAntiAlias(true);
      this.arcPaint.setStyle(Paint.Style.FILL);
    } finally {
      if (typedArray != null) typedArray.recycle();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    Log.d(TAG, "w = " + w);
    Log.d(TAG, "h = " + h);
    Log.d(TAG, "oldw = " + oldw);
    Log.d(TAG, "oldh = " + oldh);
    int min = Math.min(w, h);
    int max = Math.max(w, h);
    this.radius = min / 2 - strokedWidthPixel;
    Log.d(TAG, "radius = " + radius);
    Log.d(TAG, "strokedWidthPixel = " + strokedWidthPixel);

    float left = strokedWidthPixel / 2;
    float top = strokedWidthPixel / 2;

    point.set(min / 2, min / 2);
    rectF.set(left, top, (float) min, (float) min);
    Log.d(TAG, "point = " + point);
    Log.d(TAG, "rectF = " + rectF);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    //Log.d(TAG, "onDraw point = " + point);
    //Log.d(TAG, "onDraw rectF = " + rectF);

    try {
      // Grey Circle - This circle will be there by default.
      float cx = point.x;
      float cy = point.y;
      canvas.drawCircle(cx, cy, radius, circlePaint);

      // Green Arc (Arc with 360 angle) - This circle will be animated as time progresses.
      canvas.drawArc(rectF, startingPointInDegrees, preFillAngle, true, arcPaint);
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Method to get the degrees up-till which the arc is already pre-filled.
   */
  public float getPreFillAngle() {
    return preFillAngle;
  }

  public void setPreFillAngle(float preFillAngle) {
    this.preFillAngle = preFillAngle;
  }

  private float convertDpIntoPixel(float dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        getResources().getDisplayMetrics());
  }
}
