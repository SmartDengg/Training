package com.smartdengg.reactive;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;

/**
 * 创建时间:  2017/03/05 22:06 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class RxJavaHooksSample {

  public static void main(String[] args) {

    setError();

    //assemblyTracking();

    RxJavaHooks.setOnScheduleAction(new Func1<Action0, Action0>() {
      @Override public Action0 call(Action0 action0) {
        return null;
      }
    });

    RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
      @Override public Scheduler call(Scheduler scheduler) {
        return null;
      }
    });

    RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {

      @Override public Scheduler getNewThreadScheduler() {
        return super.getNewThreadScheduler();
      }

      @Override public Action0 onSchedule(Action0 action) {
        return super.onSchedule(action);
      }
    });

    for (; ; ) ;
  }

  private static void assemblyTracking() {
    RxJavaHooks.enableAssemblyTracking();

    Observable.empty().single().subscribe(new Action1<Object>() {
      @Override public void call(Object x) {
        System.out.println(x);
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        //throwable.printStackTrace();
      }
    });

    RxJavaHooks.resetAssemblyTracking();
  }

  private static void setError() {

    RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
      @Override public void handleError(Throwable e) {
        super.handleError(e);
        System.err.println("================");
        System.err.println(e.getMessage());
        System.err.println("================");
      }
    });

    RxJavaHooks.setOnError(new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        System.err.println("---------------");
        System.err.println(throwable.getMessage());
        System.err.println("---------------");
      }
    });

    Observable.error(new Exception("ssss")).subscribe(new Subscriber<Object>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        throw new IllegalStateException("22222222222");
      }

      @Override public void onNext(Object o) {

      }
    });
  }
}
