package com.example.master_worker;

/**
 * 创建时间:  2017/06/12 17:25 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class PlusWorker extends Worker<Integer, Double> {

  @Override protected Double handle(Integer integer) {
    return Math.pow(integer, 2);
  }
}
