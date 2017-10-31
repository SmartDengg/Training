package com.example.producer_consumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建时间:  2017/06/12 20:12 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Producer implements Runnable {

  private volatile boolean isRunning = true;
  private BlockingQueue<Data> queue;

  private static AtomicInteger count = new AtomicInteger();

  public Producer(BlockingQueue<Data> queue) {
    this.queue = queue;
  }

  @Override public void run() {

    Data data;
    Random random = new Random();
    System.out.println("start Producer id = " + Thread.currentThread().getId());

    try {
      while (isRunning) {
        Thread.sleep(random.nextInt(1000));

        data = new Data(count.incrementAndGet());
        System.out.println(data + "is put into queue");

        if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
          System.err.println("failed ro put data: " + data);
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  public void stop() {
    isRunning = false;
  }
}
