package com.smartdengg.methodcounts;

/**
 * 创建时间:  2017/02/13 16:56 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Callbacks {

  interface Callback<T> {
    T call(T value);
  }

  class StringCallback implements Callback<Integer> {

    @Override public Integer call(Integer value) {
      return null;
    }
  }
}
