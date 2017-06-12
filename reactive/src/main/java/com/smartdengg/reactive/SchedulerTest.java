package com.smartdengg.reactive;

import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.TrampolineScheduler;

/**
 * 创建时间:  2017/06/01 17:47 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class SchedulerTest {

  public static void main(String[] args) {

    final Scheduler.Worker worker = TrampolineScheduler.INSTANCE.createWorker();

    for (int i = 0, n = 5; i < n; i++) {
      final int finalI = i;
      new Thread(new Runnable() {
        @Override public void run() {
          Subscription schedule = worker.schedule(new Action0() {
            @Override public void call() {
              System.out.println("i = " + finalI);
              System.out.println(Thread.currentThread().toString());
            }
          }, finalI, TimeUnit.SECONDS);

          boolean unsubscribed = schedule.isUnsubscribed();
          System.out.println(String.valueOf(unsubscribed));
          System.out.println("\n");
        }
      }).start();
    }

    for (; ; ) ;
  }
}
