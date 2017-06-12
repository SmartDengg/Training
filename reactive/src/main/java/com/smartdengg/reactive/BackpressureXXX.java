package com.smartdengg.reactive;

import android.support.annotation.NonNull;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.BackpressureOverflow;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.plugins.RxJavaErrorHandler;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * 创建时间:  2017/02/27 12:13 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 *
 * Most developers encounter backpressure when their application fails with
 * MissingBackpressureException and the exception usually points to the observeOn operator. The
 * actual cause is usually the non-backpressured use of PublishSubject, timer() or interval() or
 * custom operators created via create()
 */
public class BackpressureXXX {

  public static void main(String[] args) {

    //batching();

    //onBackpressureBuffer(16);

    //onBackpressureBuffer(16, null, BackpressureOverflow.ON_OVERFLOW_ERROR);

    //onBackpressureDrop();

    onBackpressureLatest();

    for (; ; ) ;
  }

  private static void onBackpressureLatest() {

    Observable.interval(1, TimeUnit.MILLISECONDS)
        .onBackpressureLatest()
        .observeOn(Schedulers.io(), 4)
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
   * For example, if one wants to perform some periodic background task but each iteration may last
   * longer than the period, it is safe to drop the excess interval notification as there will be
   * more later on
   */
  private static void onBackpressureDrop() {

    Observable.interval(1, TimeUnit.MILLISECONDS)
        .onBackpressureDrop()
        .observeOn(Schedulers.io(), 4)
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

  class AAA extends RxJavaErrorHandler {




  }

  /**
   * ON_OVERFLOW_ERROR: this is the default behavior of the previous two overloads, signalling a
   * BufferOverflowException
   *
   * ON_OVERFLOW_DEFAULT: currently it is the same as ON_OVERFLOW_ERROR
   *
   * ON_OVERFLOW_DROP_LATEST : if an overflow would happen, the current value will be simply
   * ignored and only the old values will be delivered once the downstream requests.
   *
   * ON_OVERFLOW_DROP_OLDEST : drops the oldest element in the buffer and adds the current value to
   * it
   */
  private static void onBackpressureBuffer(int capacity, Action0 action0,
      BackpressureOverflow.Strategy onOverflowError) {
    Observable.range(1, 1_000_000).onBackpressureBuffer(capacity, action0, onOverflowError).
        observeOn(Schedulers.computation()).subscribe(getSubscriber());
  }

  /**
   * In this example, the observeOn goes with a very low buffer size yet there is no
   * MissingBackpressureException as onBackpressureBuffer soaks up all the 1 million values and
   * hands over small batches of it to observeOn.
   *
   * Note however that onBackpressureBuffer consumes its source in an unbounded manner, that is,
   * without applying any backpressure to it. This has the consequence that even a
   * backpressure-supporting source such as range will be completely realized.
   */
  private static void onBackpressureBuffer(int capacity) {
    Observable<Integer> rangeObservable = Observable.range(1, 1_000_000);

    Observable<Integer> observable;
    if (capacity == 0) {
      observable = rangeObservable.onBackpressureBuffer();
    } else {
      observable = rangeObservable.onBackpressureBuffer(capacity);
    }
    observable.observeOn(Schedulers.computation()).subscribe(getSubscriber());
  }

  /**
   * This is a bounded version that signals BufferOverflowErrorin case its buffer reaches the given
   * capacity
   */
  private static void batching() {
    PublishSubject<Integer> source = PublishSubject.create();

    source.buffer(1024)
        .observeOn(Schedulers.computation(), 1024)
        .subscribe(new Subscriber<List<Integer>>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override public void onNext(List<Integer> list) {

            sleep();
            System.out.println(String.valueOf(list.size()));
          }
        });

    for (int i = 0; i < 1_000_000; i++) {
      source.onNext(i);
    }
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
