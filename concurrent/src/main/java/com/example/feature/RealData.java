package com.example.feature;

/**
 * 创建时间:  2017/06/12 12:14 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class RealData implements Data {

  private String result;

  public RealData(String param) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0, n = 10; i < n; i++) {
      stringBuilder.append(param).append(System.lineSeparator());
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    result = stringBuilder.toString();
  }

  @Override public String getResult() {
    return result;
  }
}
