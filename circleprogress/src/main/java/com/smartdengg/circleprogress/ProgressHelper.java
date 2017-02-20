package com.smartdengg.circleprogress;

/**
 * 创建时间:  2017/01/04 00:21 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ProgressHelper {

  private CircleProgress circleProgress;
  private ArcProgressAnimation arcProgressAnimation;

  public ProgressHelper(CircleProgress circleProgress) {
    this.circleProgress = circleProgress;
  }

  /**
   * @param endingAngle Value between 0-360; The point up-till which the user wants the
   * progression.
   * @param duration Value in milliseconds; Progress animation time period.
   */
  public void startTimer(int endingAngle, long duration) {
    arcProgressAnimation = new ArcProgressAnimation(circleProgress, endingAngle);
    arcProgressAnimation.setDuration(duration);
    circleProgress.startAnimation(arcProgressAnimation);
  }

  /**
   * Method to reset the timer to start angle and then start the progress again.
   */
  public void restartTimer() {
    if (arcProgressAnimation != null) {
      arcProgressAnimation.cancel();
      circleProgress.startAnimation(arcProgressAnimation);
    }
  }

  /**
   * Method to reset the timer to start angle.
   */
  public void resetTimer() {
    if (arcProgressAnimation != null) arcProgressAnimation.cancel();
  }
}
