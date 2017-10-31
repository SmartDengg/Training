package com.example.master_worker;

import java.util.Map;
import java.util.Set;

/**
 * 创建时间:  2017/06/12 17:31 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Client {

  public static void main(String[] args) throws InterruptedException {

    Master<Integer, Double> master = new Master<>(new PlusWorker(), 5);

    for (int i = 0, n = 5; i < n; i++) {
      master.submit(i);
    }

    master.execute();

    double result = 0;

    Map<String, Double> resultMap = master.getResultMap();

    while (resultMap.size() > 0 || !master.isCompleted()) {
      //Thread.sleep(100);

      Set<String> keys = resultMap.keySet();
      String key = null;

      //noinspection LoopStatementThatDoesntLoop
      for (String k : keys) {
        key = k;
        break;
      }

      Double d = null;

      if (key != null) {
        d = resultMap.remove(key);
        System.out.println(d);
      }

      if (d != null) result += d;
    }

    System.out.println("result = " + result);

    for (; ; ) ;
  }
}
