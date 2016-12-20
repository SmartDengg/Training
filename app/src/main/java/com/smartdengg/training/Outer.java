package com.smartdengg.training;

/**
 * 创建时间:  2016/12/04 23:58 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Outer {

  public Outer() {
  }

  public Outer(String s) {
  }

  private Inner mInner = new Inner("Inner");
  private Anonymous mAnonymous = new Anonymous("Anonymous") {
    @Override void show() {
      outerInvoke();
    }
  };

  private Inner mAnonymousInner = new Inner("Anonymous Inner") {
    @Override void show() {
      outerInvoke();
    }
  };

  private String mString = "string";
  private static final String mStaticString = "static string";

  private void start() {
  }

  private void outerInvoke() {
    System.out.println("Outer.outerInvoke");
  }

  public static void access$0(Outer outer) {
    String string = outer.mString;
  }

  private abstract class Anonymous {

    Anonymous(String s) {
    }

    abstract void show();
  }

  private class Inner {
    private Inner(String s) {
      String string = mString;
      String statucString = mStaticString;

      outerInvoke();
    }

    void show() {
    }
  }

  private class Inner2 extends Inner {
    private Inner2(String s) {
      super(s);
    }
  }

  private static class StaticInner {
    private void invoke(Outer outer) {
      String string = outer.mString;
      String statucString = mStaticString;

      outer.outerInvoke();
    }
  }
}
