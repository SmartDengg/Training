package com.smartdengg.reactive2;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建时间:  2017/10/30 12:19 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class SubscribeOn {

  public static void main(String[] args) {

    Observable.just(1).subscribeOn(Schedulers.io())/*.map(new Function<Integer, Integer>() {
      @Override public Integer apply(@NonNull Integer integer) throws Exception {
        System.out.println(Thread.currentThread().toString());
        return 2;
      }
    })*/.subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Integer>() {
      @Override public void accept(@NonNull Integer integer) throws Exception {
        System.out.println(Thread.currentThread().toString());
      }
    });

    for (; ; ) ;
  }
}
