package com.smartdengg.reactive;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 创建时间:  2017/05/04 11:29 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class EmitterTest {

  public static void main(String[] args) {

    emitter();

    //Scheduler scheduler = Schedulers.newThread();
    //for (int i = 0; i < 100; i++)
    //  scheduler.createWorker().schedule(new Action0() {
    //    @Override public void call() {
    //      System.out.println(Thread.currentThread().toString());
    //    }
    //  });

    for (; ; ) ;
  }

  private static void emitter() {
    Observable<Integer> obs = Observable.create(new Action1<Emitter<Integer>>() {
      @Override public void call(Emitter<Integer> emitter) {
        for (int i = 1; i < 300; i++) {
          if (i % 5 == 0) {
            sleep(300L);
          }

          emitter.onNext(i);
        }

        emitter.onCompleted();
      }
    }, Emitter.BackpressureMode.LATEST);

    obs.subscribeOn(Schedulers.computation())
        .observeOn(Schedulers.computation())
        .subscribe(new Action1<Integer>() {
          @Override public void call(Integer value) {
            System.out.println("Received " + value);
          }
        }); // Why does this get stuck at "Received 128"
  }

  static void sleep(Long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
