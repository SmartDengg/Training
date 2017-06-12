package com.smartdengg.reactive;

import android.support.annotation.NonNull;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * 创建时间:  2017/02/27 11:10 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 * Backpressure is when in an Observable processing pipeline, some asynchronous stages can't
 * process
 * the values fast enough and need a way to tell the upstream producer to slow down
 */
public class MissingBackpressureException {

  public static void main(String[] args) {

    //occurThroughCreate();

    //occurThroughPublishSubject();

    //noOccurThroughRange();

    noOccurThroughInterval();

    for (; ; ) ;
  }

  /**
   * Beyond the PublishSubjectabove, there are other operators that don't support backpressure,
   * mostly due to functional reasons. For example, the operator interval emits values
   * periodically,
   * backpressuring it would lead to shifting in the period relative to a wall clock.
   */
  private static void noOccurThroughInterval() {
    Observable.interval(0, TimeUnit.MILLISECONDS)
        //.onBackpressureBuffer()
        .observeOn(Schedulers.computation())
        .subscribe(new Subscriber<Long>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override public void onNext(Long aLong) {
            sleep();
            System.out.println(aLong);
          }
        });
  }

  /**
   * There is no error and everything runs smoothly with small memory usage. The reason for this is
   * that many source operators can "generate" values on demand and thus the operator observeOn can
   * tell the range generate at most so many values the observeOn buffer can hold at once without
   * overflow.
   *
   * This negotiation is based on the computer science concept of co-routines (I call you, you call
   * me). The operator range sends a callback, in the form of an implementation of the Producer
   * interface, to the observeOn by calling its (inner Subscriber's) setProducer. In return, the
   * observeOn calls Producer.request(n) with a value to tell the range it is allowed to produce
   * (i.e., onNext it) that many additional elements. It is then the observeOn's responsibility to
   * call the request method in the right time and with the right value to keep the data flowing
   * but
   * not overflowing.
   */
  private static void noOccurThroughRange() {
    Observable.range(1, 1_000_000).observeOn(Schedulers.computation()).subscribe(getSubscriber());
  }

  private static void occurThroughPublishSubject() {

    PublishSubject<Integer> publishSubject = PublishSubject.create();

    publishSubject.observeOn(Schedulers.computation()).subscribe(getSubscriber());

    for (int i = 0; i < 10000; i++) {
      publishSubject.onNext(i);
    }
  }

  private static void occurThroughCreate() {
    Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {

        for (int i = 0; i < 10000; i++) {
          subscriber.onNext(i);
        }
      }
    }).observeOn(Schedulers.immediate()).subscribe(getSubscriber());
  }

  @NonNull private static Subscriber<Integer> getSubscriber() {
    return new Subscriber<Integer>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override public void onNext(Integer integer) {

        sleep();

        System.out.println(integer);
      }
    };
  }

  static void sleep() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
