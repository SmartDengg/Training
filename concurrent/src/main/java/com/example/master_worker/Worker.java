package com.example.master_worker;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建时间:  2017/06/12 17:15 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Worker<Input, Result> implements Runnable {

  private Queue<Input> workQueue;
  private Map<String, Result> resultMap;

  private AtomicInteger activeCount;

  public void setActiveCount(AtomicInteger activeCount) {
    this.activeCount = activeCount;
  }

  public void setWorkQueue(Queue<Input> workQueue) {
    this.workQueue = workQueue;
  }

  public void setResultMap(Map<String, Result> resultMap) {
    this.resultMap = resultMap;
  }

  protected Result handle(Input input) {
    //noinspection unchecked
    return (Result) input;
  }

  @Override public void run() {

    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    while (true) {

      Input input = workQueue.poll();

      if (input == null) break;

      Result result = handle(input);

      resultMap.put(String.valueOf(input.hashCode()), result);
    }

    activeCount.decrementAndGet();
  }
}
