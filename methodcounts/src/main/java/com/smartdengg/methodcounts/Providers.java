package com.smartdengg.methodcounts;

import java.util.List;

/**
 * 创建时间:  2017/02/14 12:15 <br>
 * 作者:  dengwei <br>
 * 描述:
 */
public class Providers {

  class Provider<T> {
    T get() {
      return null;
    }
  }

  class ViewProvider extends Provider<Number> {
    @Override public Number get() {
      return null;
    }
  }

  class TextViewProvider extends ViewProvider {
    @Override public Integer get() {
      return null;
    }
  }

  class HouseSearchConfig {

    String name;

    List<HouseSearchConfig> children;
  }
}
