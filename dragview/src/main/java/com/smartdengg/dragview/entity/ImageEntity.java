package com.smartdengg.dragview.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 创建时间:  2016/12/28 11:59 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ImageEntity implements Serializable {

  private static final long serialVersionUID = 7842687543908673176L;
  public int drawableID;
  public boolean isChecked;

  public ImageEntity(int drawableID) {
    this.drawableID = drawableID;
  }

  public void updateChecked() {
    this.isChecked = !isChecked;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ImageEntity that = (ImageEntity) o;
    return drawableID == that.drawableID && isChecked == that.isChecked;
  }

  @Override public int hashCode() {
    return Objects.hash(drawableID, isChecked);
  }

  @Override public String toString() {
    return "ImageEntity{" +
        "drawableID=" + drawableID +
        ", isChecked=" + isChecked +
        '}';
  }
}
