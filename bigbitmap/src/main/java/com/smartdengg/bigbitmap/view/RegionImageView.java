package com.smartdengg.bigbitmap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;

public class RegionImageView extends View {

  private static final String TAG = RegionImageView.class.getSimpleName();

  private BitmapRegionDecoder mDecoder;
  private int mImageWidth, mImageHeight;
  private volatile Rect mRect = new Rect();
  private MoveGestureDetector mDetector;

  private static final BitmapFactory.Options options = new BitmapFactory.Options();

  static {
    options.inPreferredConfig = Bitmap.Config.RGB_565;
  }

  public RegionImageView(Context context) {
    this(context, null);
  }

  public RegionImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RegionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void loadFromStream(InputStream is) {
    try {
      mDecoder = BitmapRegionDecoder.newInstance(is, false);

      int height = mDecoder.getHeight();
      int width = mDecoder.getWidth();

      BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
      // Grab the bounds for the scene dimensions
      tmpOptions.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(is, null, tmpOptions);
      mImageWidth = tmpOptions.outWidth;
      mImageHeight = tmpOptions.outHeight;

      requestLayout();
      invalidate();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (is != null) is.close();
      } catch (Exception ignored) {
      }
    }
  }

  public void init() {
    mDetector =
        new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
          @Override public boolean onMove(MoveGestureDetector detector) {
            int moveX = (int) detector.getMoveX();
            int moveY = (int) detector.getMoveY();

            Log.d(TAG, "moveX = " + moveX);
            Log.d(TAG, "moveY = " + moveY);

            if (mImageWidth > getWidth()) {
              mRect.offset(-moveX, 0);
              checkWidth();
              invalidate();
            }
            if (mImageHeight > getHeight()) {
              mRect.offset(0, -moveY);
              checkHeight();
              invalidate();
            }

            return true;
          }
        });
  }

  private void checkWidth() {

    Rect rect = mRect;
    int imageWidth = mImageWidth;

    if (rect.right > imageWidth) {
      rect.right = imageWidth;
      rect.left = imageWidth - getWidth();
    }

    if (rect.left < 0) {
      rect.left = 0;
      rect.right = getWidth();
    }
  }

  private void checkHeight() {

    Rect rect = mRect;
    int imageHeight = mImageHeight;

    if (rect.bottom > imageHeight) {
      rect.bottom = imageHeight;
      rect.top = imageHeight - getHeight();
    }

    if (rect.top < 0) {
      rect.top = 0;
      rect.bottom = getHeight();
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    mDetector.onToucEvent(event);
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    Bitmap bm = mDecoder.decodeRegion(mRect, options);
    canvas.drawBitmap(bm, 0, 0, null);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int width = getMeasuredWidth();
    int height = getMeasuredHeight();

    mRect.left = 0;
    mRect.top = 0;
    mRect.right = mRect.left + width;
    mRect.bottom = mRect.top + height;
  }
}
