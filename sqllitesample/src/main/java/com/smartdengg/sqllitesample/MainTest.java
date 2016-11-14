package com.smartdengg.sqllitesample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建时间:  2016/11/08 21:44 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class MainTest {

  public static void main(String[] args) throws InterruptedException {

    ExecutorService executorService = Executors.newCachedThreadPool();

    for (int i = 0; i < 10; i++) {

      Thread.sleep(65_000);

      executorService.execute(new Runnable() {
        @Override public void run() {

          System.out.println(Thread.currentThread().getName());
        }
      });
    }
  }
}
