package com.example;

/**
 * 创建时间:  2017/09/19 17:03 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class OtherSingleton {

  private static final String cons = "cons";
  private static final String cons1 = "cons1";
  private static final String cons2 = "cons2";
  static final Singleton.Inner finalSingleton = new Singleton.Inner();
  //static OtherSingleton singleton = new OtherSingleton();

  private OtherSingleton() {

  }

  static {
    System.out.println("OtherSingleton instance initializer : static");
  }

  {
    System.out.println("OtherSingleton instance initializer");
  }
}
