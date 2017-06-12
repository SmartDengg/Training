package com.smartdengg.methodcounts;

/**
 * 创建时间:  2017/03/10 13:46 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class InnerClass {

  private void call() {

    Callback callback = new Callback() {
      @Override public void set(String s) {

      }
    };
  }

  private void setCallback(Callback c) {

  }

  interface Callback {

    void set(String s);
  }
}
