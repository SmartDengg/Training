package com.example.producer_consumer;

/**
 * 创建时间:  2017/06/12 20:10 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Data {

  private final int intData;

  public Data(int intData) {
    this.intData = intData;
  }

  public int getIntData() {
    return intData;
  }

  @Override public String toString() {
    return "Data{" + "intData=" + intData + '}';
  }
}
