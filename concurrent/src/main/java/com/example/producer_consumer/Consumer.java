package com.example.producer_consumer;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 创建时间:  2017/06/12 20:20 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Consumer implements Runnable {

  private BlockingQueue<Data> queue;

  public Consumer(BlockingQueue<Data> queue) {
    this.queue = queue;
  }

  @Override public void run() {

    System.out.println("start Consumer id = " + Thread.currentThread().getId());

    Random random = new Random();

    try {
      //noinspection InfiniteLoopStatement
      while (true) {

        Data data = queue.take();

        if (data != null) {
          int intData = data.getIntData();
          int re = intData * intData;
          System.out.println(MessageFormat.format("{0}*{1}={2}", intData, intData, re));
        }

        Thread.sleep(random.nextInt(1000));
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }
}
