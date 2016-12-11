package com.smartdengg.training;

/**
 * Created by SmartDengg on 2016/12/11.
 */
public class ExtendsTest {

  public static void main(String[] args) {

  }

  class Parent {

    Object test(String s) {
      return null;
    }
  }

  class Son extends Parent {

    @Override String test(String s) {
      super.test(s);
      return null;
    }

    String test(CharSequence s) {
      return null;
    }
  }
}
