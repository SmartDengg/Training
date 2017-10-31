package com.example;

/**
 * 创建时间:  2017/09/18 18:24 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Singleton {

  Singleton() {
    System.out.println("Singleton.Singleton");
  }

  public static void main(String[] args) {
    //Singleton singleton = Inner.finalSingleton;
    //Singleton singleton1 = Inner.singleton;
    //System.out.println(Inner.cons);
    System.out.println(OtherSingleton.finalSingleton);
  }

  public static class Inner {
    private static final String cons = "cons";
    private static final String cons1 = "cons1";
    private static final String cons2 = "cons2";
    static final Singleton finalSingleton = new Singleton();
    //static Inner inner = new Inner();

    public Inner() {

    }

    static {
      System.out.println("Inner.instance initializer : static");
    }

    {
      System.out.println("Inner.instance initializer");
    }
  }
}
