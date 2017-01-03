package com.smartdengg.dragview.entity;

import android.support.annotation.DrawableRes;
import java.util.Objects;

/**
 * 创建时间:  2016/12/28 12:40 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class TempEntity {

  public int ID;
  public int normalDrawable;
  public int selectedDrawable;
  public boolean isSelected;

  public TempEntity(int ID, @DrawableRes int normalDrawable, @DrawableRes int selectedDrawable,
      boolean isSelected) {
    this.ID = ID;
    this.normalDrawable = normalDrawable;
    this.selectedDrawable = selectedDrawable;
    this.isSelected = isSelected;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TempEntity that = (TempEntity) o;
    return ID == that.ID &&
        normalDrawable == that.normalDrawable &&
        selectedDrawable == that.selectedDrawable &&
        isSelected == that.isSelected;
  }

  @Override public int hashCode() {
    return Objects.hash(ID, normalDrawable, selectedDrawable, isSelected);
  }
}
