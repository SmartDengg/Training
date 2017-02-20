package com.smartdengg.circleprogress;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 创建时间:  2017/01/04 00:19 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ArcProgressAnimation extends Animation {
  private CircleProgress circleProgress;

  private float startingAngle;
  private float endingAngle;

  public ArcProgressAnimation(CircleProgress circleProgress, int endingAngle) {
    this.circleProgress = circleProgress;
    this.startingAngle = circleProgress.getPreFillAngle();
    this.endingAngle = endingAngle;
  }

  @Override
  protected void applyTransformation(float interpolatedTime, Transformation transformation) {

    float finalAngle = startingAngle + ((endingAngle - startingAngle) * interpolatedTime);

    circleProgress.setPreFillAngle(finalAngle);
    circleProgress.invalidate();
  }
}
