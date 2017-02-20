package com.smartdengg.sqllitesample;

/**
 * 创建时间:  2016/11/08 21:44 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class MainTest {

  public static void main(String[] args) throws InterruptedException {

    int a = 1;
    int b = 2;
    int c = 3;

    int result = 0;

    result |= a;
    System.out.println(result);

    result |= b;
    System.out.println(result);

    result &= ~a;
    System.out.println(result);

    result ^= a;
    System.out.println(result);

    result ^= a;
    System.out.println(result);

    for (; ; ) ;
  }
}
