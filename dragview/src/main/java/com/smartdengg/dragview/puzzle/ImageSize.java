package com.smartdengg.dragview.puzzle;

import android.view.ViewGroup;

/**
 * 创建时间:  2017/01/10 11:05 <br>
 * 作者:  dengwei <br>
 * 描述:  存储拼图尺寸
 */
class ImageSize {

  static final int[] NORMAL = new int[2];
  static final int[] LARGE = new int[2];
  static final ViewGroup.MarginLayoutParams[] LAYOUT_PARAMS = new ViewGroup.MarginLayoutParams[2];

  private ImageSize() {
    throw new AssertionError("No instance");
  }

  static boolean validatedNormalSize() {
    for (int i = 0, n = NORMAL.length; i < n; i++) {
      if (NORMAL[i] == 0) return false;
    }
    return true;
  }

  static boolean validatedLargeSize() {
    for (int i = 0, n = LARGE.length; i < n; i++) {
      if (LARGE[i] == 0) return false;
    }
    return true;
  }

  static boolean validatedParams() {
    return checkNull(LAYOUT_PARAMS);
  }

  private static boolean checkNull(Object object) {
    if (object == null) return false;

    if (object.getClass().isArray()) {
      Object[] array = (Object[]) object;
      for (int i = 0, n = array.length; i < n; i++) {
        if (array[i] == null) return false;
      }
    }

    return true;
  }
}
