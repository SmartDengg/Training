package com.smartdengg.methodcounts;

/**
 * 创建时间:  2017/02/13 16:43 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Aamazing {

  int c;
  int count;

  private void test() {

    new Runnable() {
      @Override public void run() {

        c = 0;

        count = 0;
        ++count;
        --count;
        count++;
        count--;
      }
    };
  }
}
