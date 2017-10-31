package com.example.feature;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 创建时间:  2017/06/12 11:29 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class FeatureData implements Data {

  private Data realData;
  private AtomicBoolean isReady = new AtomicBoolean();

  protected synchronized void setRealData(Data realData) {

    if (isReady.get()) return;

    this.isReady.set(true);
    this.realData = realData;

    notifyAll();
  }

  @Override public synchronized String getResult() {

    while (!isReady.get()) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return realData.getResult();
  }
}
