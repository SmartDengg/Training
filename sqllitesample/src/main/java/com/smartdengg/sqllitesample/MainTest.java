package com.smartdengg.sqllitesample;

/**
 * 创建时间:  2016/11/08 21:44 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class MainTest {

  public static void main(String[] args) throws InterruptedException {

    int a = 1 << 0;
    int b = 1 << 1;
    int c = 1 << 2;

    int result = 0;

    result |= a;
    System.out.println(result);// 1

    result |= b;
    System.out.println(result);// 3

    final boolean includeA = (result | a) == result;
    System.out.println(includeA);// true

    final boolean includeB = (result & b) != 0;
    System.out.println(includeB);// true

    final boolean excludeC = (result & c) == 0;
    System.out.println(excludeC);// true

    result &= ~a;
    System.out.println(result);// 2

    result ^= a;
    System.out.println(result);// 3

    result ^= a;
    System.out.println(result);// 2

    for (; ; ) ;
  }
}
