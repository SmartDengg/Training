package com.example.threadpool;

import java.util.LinkedList;

/**
 * 创建时间:  2017/06/13 12:29 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ThreadPool {

  private LinkedList<ThreadWrapper> idleThreads;
  private int threadCount = 0;
  private boolean isShutdown = false;

  public static class SingletonHolder {
    public static ThreadPool instance = new ThreadPool();
  }

  public ThreadPool() {
    this.idleThreads = new LinkedList<>();
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void release(ThreadWrapper threadWrapper) {

    if (!isShutdown) {
      idleThreads.add(threadWrapper);
    } else {
      threadWrapper.shutdown();
    }
  }

  public synchronized void shuwdown() {

    isShutdown = true;

    for (int i = 0, n = idleThreads.size(); i < n; i++) {
      ThreadWrapper threadWrapper = idleThreads.get(i);
      threadWrapper.shutdown();
    }
  }

  public synchronized void execute(Runnable target) {

    ThreadWrapper threadWrapper;

    if (idleThreads.size() > 0) {
      threadWrapper = idleThreads.removeFirst();
      threadWrapper.setTarget(target);
    } else {
      threadWrapper = new ThreadWrapper(target, "ThreadWrapper#" + threadCount++, this);
      threadWrapper.start();
    }
  }
}
