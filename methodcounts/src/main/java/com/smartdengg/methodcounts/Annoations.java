package com.smartdengg.methodcounts;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 创建时间:  2017/02/17 11:12 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Annoations {

  public static void main(String[] args) {

    testClass("sss");
    testRunntime("saaa");
    testSource("ddd");
  }

  @Retention(RetentionPolicy.SOURCE) @interface Source {
  }

  @Retention(RetentionPolicy.CLASS) @interface Class {
  }

  @Retention(RetentionPolicy.RUNTIME) @interface Runntime {
  }

  static void testSource(@Source String s) {
    System.err.println("");
  }

  static void testClass(@Class String s) {
    System.err.println("");
  }

  static void testRunntime(@Runntime String s) {
    System.err.println("");
  }
}
