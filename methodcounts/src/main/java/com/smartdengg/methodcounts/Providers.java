package com.smartdengg.methodcounts;

/**
 * 创建时间:  2017/02/14 12:15 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Providers {

  interface Provider<T> {
    T get();
  }

  class ViewProvider implements Provider<Number> {
    @Override public Number get() {
      return null;
    }
  }

  class TextViewProvider extends ViewProvider {
    @Override public Integer get() {
      return null;
    }
  }
}
