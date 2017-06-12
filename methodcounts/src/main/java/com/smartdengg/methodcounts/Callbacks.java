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

  class IntegerCallback implements Callback<Integer> {

    @Override public Integer call(Integer value) {
      return null;
    }
  }

  class CharSequenceCallback implements Callback<CharSequence> {

    @Override public CharSequence call(CharSequence value) {
      return null;
    }
  }

  class StringCallback extends CharSequenceCallback {

    @Override public String call(CharSequence value) {

      super.call("");

      test();
      test1();

      return null;
    }

    private void test() {
    }

    public void test1() {
    }
  }
}
