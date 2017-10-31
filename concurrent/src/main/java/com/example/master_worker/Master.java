package com.example.master_worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建时间:  2017/06/12 17:11 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Master<Input, Result> {

  private Queue<Input> workQueue = new ConcurrentLinkedQueue<>();
  private Map<String, Thread> threadMap = new HashMap<>();
  private Map<String, Result> resultMap = new ConcurrentHashMap<>();

  private AtomicInteger activeCount;

  public Master(Worker<Input, Result> worker, int countWorker) {

    activeCount = new AtomicInteger(countWorker);
    worker.setActiveCount(activeCount);

    worker.setWorkQueue(workQueue);
    worker.setResultMap(resultMap);

    for (int i = 0; i < countWorker; i++) {
      String name = String.valueOf(i);
      Thread thread = new Thread(worker, name);

      threadMap.put(name, thread);
    }
  }

  public boolean isCompleted() {
    return activeCount.get() == 0;
  }

  public void submit(Input input) {
    workQueue.add(input);
  }

  public Map<String, Result> getResultMap() {
    return resultMap;
  }

  public void execute() {
    for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
      entry.getValue().start();
    }
  }
}
