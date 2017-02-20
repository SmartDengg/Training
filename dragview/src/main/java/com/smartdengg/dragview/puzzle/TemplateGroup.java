package com.smartdengg.dragview.puzzle;

import android.support.annotation.DrawableRes;
import android.util.SparseArray;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * 创建时间:  2016/12/28 12:16 <br>
 * 作者:  dengwei <br>
 * 描述:
 */
public enum TemplateGroup {

  MODE_UNKNOWN(GroupSymbol.UNKNOWN_SYMBOL) {
    @Override List<Temp> apply() {
      return Collections.emptyList();
    }
  },

  MODE_ONE(GroupSymbol.ONE_SYMBOL, ID.ONE_MODE_11) {
    @Override List<Temp> apply() {
      List<Temp> tempEntities = new ArrayList<>(ids.length);
      Temp temp = new Temp(ids[0], R.drawable.temp_1tu_pressed, R.drawable.temp_1tu_pressed, true);
      tempEntities.add(temp);
      return tempEntities;
    }
  },

  MODE_TWO(GroupSymbol.TWO_SYMBOL, ID.TWO_MODE_21) {
    @Override List<Temp> apply() {
      List<Temp> tempEntities = new ArrayList<>(ids.length);
      Temp temp = new Temp(ids[0], R.drawable.temp_2tu_pressed, R.drawable.temp_2tu_pressed, true);
      tempEntities.add(temp);
      return tempEntities;
    }
  },

  MODE_THREE(GroupSymbol.THREE_SYMBOL, ID.THREE_MODE_31, ID.THREE_MODE_32) {
    @Override List<Temp> apply() {
      List<Temp> tempEntities = new ArrayList<>(ids.length);
      Temp temp1 =
          new Temp(ids[0], R.drawable.temp_3tu1_normol, R.drawable.temp_3tu1_pressed, true);
      Temp temp2 =
          new Temp(ids[1], R.drawable.temp_3tu2_normol, R.drawable.temp_3tu2_pressed, false);
      tempEntities.add(temp1);
      tempEntities.add(temp2);
      return tempEntities;
    }
  },

  MODE_FOUR(GroupSymbol.FOUR_SYMBOL, ID.FOUR_MODE_41) {
    @Override List<Temp> apply() {
      List<Temp> tempEntities = new ArrayList<>(ids.length);
      Temp temp = new Temp(ids[0], R.drawable.temp_4tu_pressed, R.drawable.temp_4tu_pressed, true);
      tempEntities.add(temp);
      return tempEntities;
    }
  },

  MODE_FIVE(GroupSymbol.FIVE_SYMBOL, ID.FIVE_MODE_51, ID.FIVE_MODE_52, ID.FIVE_MODE_53) {
    @Override List<Temp> apply() {
      List<Temp> tempEntities = new ArrayList<>(ids.length);
      Temp temp1 =
          new Temp(ids[0], R.drawable.temp_5tu1_normol, R.drawable.temp_5tu1_pressed, true);
      Temp temp2 =
          new Temp(ids[1], R.drawable.temp_5tu2_normol, R.drawable.temp_5tu2_pressed, false);
      Temp temp3 =
          new Temp(ids[2], R.drawable.temp_5tu3_normol, R.drawable.temp_5tu3_pressed, false);

      tempEntities.add(temp1);
      tempEntities.add(temp2);
      tempEntities.add(temp3);
      return tempEntities;
    }
  },

  MODE_SIX(GroupSymbol.SIX_SYMBOL, ID.SIX_MODE_61) {
    @Override List<Temp> apply() {
      List<Temp> tempEntities = new ArrayList<>(ids.length);
      Temp temp = new Temp(ids[0], R.drawable.temp_6tu_pressed, R.drawable.temp_6tu_pressed, true);
      tempEntities.add(temp);
      return tempEntities;
    }
  };

  public String symbol;
  public int[] ids = new int[0];

  TemplateGroup() {
  }

  TemplateGroup(String symbol, int... ids) {
    this.symbol = symbol;
    this.ids = ids;
  }

  abstract List<Temp> apply();

  /** 根据图片个数，对模板进行分类 */
  private static final int UNKNOWN_MODE = 0;
  private static final int ONE_MODE = 1;
  private static final int TWO_MODE = 2;
  private static final int THREE_MODE = 3;
  private static final int FOUR_MODE = 4;
  private static final int FIVE_MODE = 5;
  private static final int SIX_MODE = 6;

  private static class GroupSymbol {
    private static final String UNKNOWN_SYMBOL = "UNKNOWN";
    private static final String ONE_SYMBOL = "ONE";
    private static final String TWO_SYMBOL = "TWO";
    private static final String THREE_SYMBOL = "THREE";
    private static final String FOUR_SYMBOL = "FOUR";
    private static final String FIVE_SYMBOL = "FIVE";
    private static final String SIX_SYMBOL = "SIX";
  }

  public static class ID {
    public static final int ONE_MODE_11 = 11;
    public static final int TWO_MODE_21 = 21;
    public static final int THREE_MODE_31 = 31;
    public static final int THREE_MODE_32 = 32;
    public static final int FOUR_MODE_41 = 41;
    public static final int FIVE_MODE_51 = 51;
    public static final int FIVE_MODE_52 = 52;
    public static final int FIVE_MODE_53 = 53;
    public static final int SIX_MODE_61 = 61;
  }

  private static final SparseArray<TemplateGroup> GROUP_SPARSE_ARRAY = new SparseArray<>(6);

  static {
    EnumSet<TemplateGroup> templateGroups = EnumSet.allOf(TemplateGroup.class);
    TemplateGroup[] groups = templateGroups.toArray(new TemplateGroup[templateGroups.size()]);
    for (int i = 0, n = groups.length; i < n; i++) {
      GROUP_SPARSE_ARRAY.append(i, groups[i]);
    }
  }

  public static TemplateGroup correspondTemplate(List<ImageItem> items) {

    if (items == null) return TemplateGroup.MODE_UNKNOWN;

    int mode = items.size();

    switch (mode) {
      case UNKNOWN_MODE:
        return TemplateGroup.MODE_UNKNOWN;

      case ONE_MODE:  /*一张图片：居中*/
        return GROUP_SPARSE_ARRAY.get(ONE_MODE);

      case TWO_MODE:  /*两张图片：上下排列*/
        return GROUP_SPARSE_ARRAY.get(TWO_MODE);

      case THREE_MODE: /*三张图片：两种排列方式*/
        return GROUP_SPARSE_ARRAY.get(THREE_MODE);

      case FOUR_MODE: /*四张图片：居中*/
        return GROUP_SPARSE_ARRAY.get(FOUR_MODE);

      case FIVE_MODE: /*五张图片：三种排列方式*/
        return GROUP_SPARSE_ARRAY.get(FIVE_MODE);

      case SIX_MODE: /*六张图片：居中*/
        return GROUP_SPARSE_ARRAY.get(SIX_MODE);

      default:
        throw new IllegalStateException("no mode items size = " + mode);
    }
  }

  public static class Temp implements Serializable {

    private static final long serialVersionUID = -4133458106480471240L;

    int ID;
    int normalDrawable;
    int selectedDrawable;
    boolean isSelected;

    public int normalSize;
    public int largeSize;

    Temp(int ID, @DrawableRes int normalDrawable, @DrawableRes int selectedDrawable,
        boolean isSelected) {
      this.ID = ID;
      this.normalDrawable = normalDrawable;
      this.selectedDrawable = selectedDrawable;
      this.isSelected = isSelected;
    }
  }
}
