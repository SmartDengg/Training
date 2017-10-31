package com.example.threadpool;

/**
 * 创建时间:  2017/06/13 15:52 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Main {

  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    for (int i = 0, n = 100; i < n; i++) {
      ThreadPool.SingletonHolder.instance.execute(new Runnable() {
        @Override public void run() {
          System.out.println(Thread.currentThread().toString());
        }
      });
    }

    long end = System.currentTimeMillis();
    System.out.println(String.valueOf(end - start));

    for (; ; ) ;
  }
}
