package com.smartdengg.methodcounts;

import java.lang.reflect.Method;

/**
 * 创建时间:  2017/04/10 14:53 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class SyntheticTest {

  public void main(String args[]) {

    SampleNestedClass nestObj = new SampleNestedClass();
    System.out.println("Nested Variable: " + nestObj.aPrivateVariable);

    Class c = nestObj.getClass();
    Method[] methods = c.getDeclaredMethods();

    for (Method m : methods) {
      System.out.println("m: " + m + " m.isSynthetic: " + m.isSynthetic());
    }
  }

  private final class SampleNestedClass {
    private String aPrivateVariable = "A Private Variable!";

    private SampleNestedClass() {
      aPrivateVariable = "wwwwwww";
    }

    private SampleNestedClass(String s) {
      aPrivateVariable = s;
    }

    public String getaPrivateVariable() {
      return aPrivateVariable;
    }
  }
}
