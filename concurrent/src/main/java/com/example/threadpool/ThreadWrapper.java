package com.example.threadpool;

/**
 * 创建时间:  2017/06/13 14:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ThreadWrapper extends Thread {

  private ThreadPool threadPool;

  private Runnable target;
  private boolean isShutdown = false;
  private boolean isIdle = false;

  public ThreadWrapper(Runnable target, String name, ThreadPool threadPool) {
    super(name);
    this.target = target;
    this.threadPool = threadPool;
  }

  public Runnable getTarget() {
    return target;
  }

  public boolean isIdle() {
    return isIdle;
  }

  @Override public void run() {

    while (!isShutdown) {

      isIdle = false;

      if (target != null) target.run();

      isIdle = true;

      try {
        threadPool.release(this);
        synchronized (this) {
          wait();
        }
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public synchronized void setTarget(Runnable newTarget) {
    this.target = newTarget;
    notifyAll();
  }

  public synchronized void shutdown() {
    isShutdown = true;
    notifyAll();
  }
}
