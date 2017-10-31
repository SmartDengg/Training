package com.example.feature;

/**
 * 创建时间:  2017/06/12 11:28 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Client {

  public Data request(final String param) {

    final FeatureData featureData = new FeatureData();

    new Thread(new Runnable() {
      @Override public void run() {
        RealData realData = new RealData(param);
        featureData.setRealData(realData);
      }
    }).start();

    return featureData;
  }
}
