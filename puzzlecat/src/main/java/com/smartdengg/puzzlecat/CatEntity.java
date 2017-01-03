package com.smartdengg.puzzlecat;

import java.io.Serializable;

/**
 * 创建时间:  2016/12/19 16:21 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class CatEntity implements Serializable {

  private static final long serialVersionUID = 8513742531153754109L;

  public int drawableID;
  public boolean isChecked;
  public boolean isResume;

  public CatEntity(int drawableID) {
    this.drawableID = drawableID;
  }

  public void updateChecked() {
    this.isChecked = !isChecked;
  }

  public void updateResume() {
    this.isResume = !isResume;
  }
}
