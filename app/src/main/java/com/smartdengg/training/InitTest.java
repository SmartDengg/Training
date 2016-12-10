package com.smartdengg.training;

/**
 * 创建时间:  2016/12/06 16:10 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class InitTest {

  private static final String TAG = InitTest.class.getSimpleName();
  private final String TAG0 = InitTest.class.getSimpleName();
  private String TAG1 = InitTest.class.getSimpleName();

  private static String name = "deng";

  private String[] mStrings = new String[0];

  static {
    name = "wei";
  }

  {
    String s = "denggggg";
  }

  public InitTest() {
    String s1 = "weiiiiiii";
  }

  public InitTest(String s) {
    String useage = s;
  }

  public String getName() {

    String haha = name;

    return haha;
  }
}
