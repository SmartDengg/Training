package com.example.producer_consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建时间:  2017/06/12 20:24 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Client {

  public static void main(String[] args) throws InterruptedException {

    BlockingQueue<Data> blockingQueue = new LinkedBlockingQueue<>();

    Producer producer1 = new Producer(blockingQueue);
    Producer producer2 = new Producer(blockingQueue);
    Producer producer3 = new Producer(blockingQueue);

    Consumer consumer1 = new Consumer(blockingQueue);
    Consumer consumer2 = new Consumer(blockingQueue);
    Consumer consumer3 = new Consumer(blockingQueue);

    ExecutorService executorService = Executors.newCachedThreadPool();

    executorService.execute(producer1);
    executorService.execute(producer2);
    executorService.execute(producer3);

    executorService.execute(consumer1);
    executorService.execute(consumer2);
    executorService.execute(consumer3);

    Thread.sleep(5000);

    producer1.stop();
    producer2.stop();
    producer3.stop();

    Thread.sleep(3000);

    executorService.shutdown();

    for (; ; ) ;
  }
}
