package com.smartdengg.methodcounts;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 创建时间:  2017/02/16 17:49 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public enum Template {

  MODE_UNKNOWN("") {
    @Override public List<Temp> apply() {
      return Collections.emptyList();
    }
  };

  public abstract List<Temp> apply();

  public String symbol;
  public int[] ids = new int[0];

  Template() {
  }

  Template(String s, int... ids) {
  }

  public static class Temp implements Serializable {

    private static final long serialVersionUID = -4133458106480471240L;

    int ID;
    String event;
    int normalDrawable;
    int selectedDrawable;
    boolean isSelected;

    Temp(int ID, String event, int normalDrawable, int selectedDrawable, boolean isSelected) {
      this.ID = ID;
      this.event = event;
      this.normalDrawable = normalDrawable;
      this.selectedDrawable = selectedDrawable;
      this.isSelected = isSelected;
    }
  }
}