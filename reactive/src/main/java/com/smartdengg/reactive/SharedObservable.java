package com.smartdengg.reactive;

import rx.Observable;
import rx.functions.Action1;

/**
 * 创建时间:  2017/02/27 18:08 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class SharedObservable {

  public static void main(String[] args) {

    Observable<Integer> publish = Observable.just(1, 2, 3, 4).share();

    publish.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println("1-integer = [" + integer + "]");
      }
    });

    publish.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println("2-integer = [" + integer + "]");
      }
    });

    for (; ; ) ;
  }
}
