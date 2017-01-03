package com.smartdengg.dragview.puzzle;

import android.util.SparseArray;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageEntity;
import com.smartdengg.dragview.entity.TempEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * 创建时间:  2016/12/28 12:16 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
//@formatter:off
public enum TemplateGroup {

  MODE_UNKNOWN(GroupSymbol.UNKNOWN_SYMBOL) {
    @Override List<TempEntity> apply() {
      return Collections.emptyList();
    }
  },

  MODE_ONE(GroupSymbol.ONE_SYMBOL, Subset.ONE_MODE_11) {
    @Override List<TempEntity> apply() {
      List<TempEntity> tempEntities = new ArrayList<>(ids.length);
      TempEntity tempEntity =
          new TempEntity(ids[0], R.drawable.format_1tu_pressed, R.drawable.format_1tu_pressed, true);
      tempEntities.add(tempEntity);
      return tempEntities;
    }
  },

  MODE_TWO(GroupSymbol.TWO_SYMBOL, Subset.TWO_MODE_21) {
    @Override List<TempEntity> apply() {
      List<TempEntity> tempEntities = new ArrayList<>(ids.length);
      TempEntity tempEntity =
          new TempEntity(ids[0], R.drawable.format_2tu_pressed, R.drawable.format_2tu_pressed, true);
      tempEntities.add(tempEntity);
      return tempEntities;
    }
  },

  MODE_THREE(GroupSymbol.THREE_SYMBOL, Subset.THREE_MODE_31, Subset.THREE_MODE_32) {
    @Override List<TempEntity> apply() {
      List<TempEntity> tempEntities = new ArrayList<>(ids.length);
      TempEntity tempEntity1 =
          new TempEntity(ids[0], R.drawable.format_3tu1_normol, R.drawable.format_3tu1_pressed, true);
      TempEntity tempEntity2 =
          new TempEntity(ids[1], R.drawable.format_3tu2_normol, R.drawable.format_3tu2_pressed, false);
      tempEntities.add(tempEntity1);
      tempEntities.add(tempEntity2);
      return tempEntities;
    }
  },

  MODE_FOUR(GroupSymbol.FOUR_SYMBOL, Subset.FOUR_MODE_41) {
    @Override List<TempEntity> apply() {
      List<TempEntity> tempEntities = new ArrayList<>(ids.length);
      TempEntity tempEntity =
          new TempEntity(ids[0], R.drawable.format_4tu_pressed, R.drawable.format_4tu_pressed, true);
      tempEntities.add(tempEntity);
      return tempEntities;
    }
  },

  MODE_FIVE(GroupSymbol.FIVE_SYMBOL,Subset.FIVE_MODE_51,Subset.FIVE_MODE_52,Subset.FIVE_MODE_53) {
    @Override List<TempEntity> apply() {
      List<TempEntity> tempEntities = new ArrayList<>(ids.length);
      TempEntity tempEntity1 =
          new TempEntity(ids[0], R.drawable.format_5tu1_normol, R.drawable.format_5tu1_pressed, true);
      TempEntity tempEntity2 =
          new TempEntity(ids[1], R.drawable.format_5tu2_normol, R.drawable.format_5tu2_pressed, false);
      // TODO: 16/12/28 UI少给了张图
      TempEntity tempEntity3 =
          new TempEntity(ids[2], R.drawable.format_5tu3_normol, R.drawable.format_5tu2_pressed, false);

      tempEntities.add(tempEntity1);
      tempEntities.add(tempEntity2);
      tempEntities.add(tempEntity3);
      return tempEntities;
    }
  },

  MODE_SIX(GroupSymbol.SIX_SYMBOL,Subset.SIX_MODE_61) {
    @Override List<TempEntity> apply() {
      List<TempEntity> tempEntities = new ArrayList<>(ids.length);
      TempEntity tempEntity =
          new TempEntity(ids[0], R.drawable.format_6tu_pressed, R.drawable.format_6tu_pressed, true);
      tempEntities.add(tempEntity);
      return tempEntities;
    }
  };

  String symbol;
  int[] ids = new int[0];

  TemplateGroup(){}

  TemplateGroup(String symbol, int... ids) {
    this.symbol = symbol;
    this.ids = ids;
  }

  abstract List<TempEntity> apply();

  /** 根据图片个数，对模板进行分类 */
  private static final int UNKNOWN_MODE = 0;
  private static final int ONE_MODE = 1;
  private static final int TWO_MODE = 2;
  private static final int THREE_MODE = 3;
  private static final int FOUR_MODE = 4;
  private static final int FIVE_MODE = 5;
  private static final int SIX_MODE = 6;

  private static class GroupSymbol{
    private static final String UNKNOWN_SYMBOL = "UNKNOWN";
    private static final String ONE_SYMBOL = "ONE";
    private static final String TWO_SYMBOL = "TWO";
    private static final String THREE_SYMBOL = "THREE";
    private static final String FOUR_SYMBOL = "FOUR";
    private static final String FIVE_SYMBOL = "FIVE";
    private static final String SIX_SYMBOL = "SIX";
  }

  public static class Subset{
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
    for (int i = 0,n = groups.length;i<n;i++){
      GROUP_SPARSE_ARRAY.append(i,groups[i]);
    }
  }

  public static TemplateGroup correspondTemplate(List<ImageEntity> items) {

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
}
