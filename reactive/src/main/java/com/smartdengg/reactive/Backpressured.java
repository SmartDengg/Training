package com.smartdengg.reactive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import rx.Emitter;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Cancellable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.SyncOnSubscribe;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * 创建时间:  2017/02/27 16:01 <br>
 * 作者:  dengwei <br>
 * 描述:
 *
 * Creating backpressured data sources is the relatively easier task when dealing with backpressure
 * in general because the library already offers static methods on Observable that handle
 * backpressure for the developer. We can distinguish two kinds of factory methods: cold
 * "generators" that either return and generate elements based on downstream demand and hot
 * "pushers" that usually bridge non-reactive and/or non-backpressurable data sources and layer
 * some
 * backpressure handling on top of them
 */
public class Backpressured {

  public static void main(String[] args) {

    //just();

    //fromCallable();

    //binarySyncOnSubscribe();

    //intSyncOnSubscribe();

    //fromEmitterSingleResource();

    //fromEmitterMultipleResource();

    //Observable.error(new Exception()).subscribe(new Action1<Object>() {
    //  @Override public void call(Object o) {
    //
    //  }
    //});

    Observable.just(1).map(new Func1<Integer, Integer>() {
      @Override public Integer call(Integer integer) {

        if (integer == 1) throw new RuntimeException("sssssss");

        return null;
      }
    }).subscribe(new Subscriber<Integer>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override public void onNext(Integer integer) {

      }
    });

    Observable.error(new Exception("惺惺惜惺惺想")).subscribe(new Observer<Object>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        //throw new IllegalStateException("This should crash the app");

        e.printStackTrace();
      }

      @Override public void onNext(Object o) {

      }
    });

    for (; ; ) ;
  }

  /**
   * These methods allow only a single resource to be associated with the emitter at a time and
   * setting a new one unsubscribes the old one automatically. If one has to handle multiple
   * resources, create a CompositeSubscription, associate it with the emitter and then add further
   * resources to the CompositeSubscription itself
   */
  private static void fromEmitterMultipleResource() {
    Subscription subscription = Observable.fromEmitter(new Action1<Emitter<Integer>>() {
      @Override public void call(Emitter<Integer> emitter) {

        CompositeSubscription cs = new CompositeSubscription();
        Scheduler.Worker worker = Schedulers.computation().createWorker();

        emitter.onNext(666);
        worker.schedule(new Action0() {
          @Override public void call() {
            System.out.println("worker schedule");
          }
        });

        cs.add(worker);
        cs.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            System.out.println("worker unsubscribe");
          }
        }));

        emitter.setSubscription(cs);
      }
    }, Emitter.BackpressureMode.LATEST).subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println("integer = " + integer);
      }
    });

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }

    subscription.unsubscribe();
  }

  /**
   * Sometimes, the source to be wrapped into an Observable is already hot (such as mouse moves) or
   * cold but not backpressurable in its API (such as an asynchronous network callback).
   */
  private static void fromEmitterSingleResource() {
    Observable.fromEmitter(new Action1<Emitter<Integer>>() {
      @Override public void call(Emitter<Integer> emitter) {

        emitter.onNext(1);

        emitter.setCancellation(new Cancellable() {
          @Override public void cancel() throws Exception {

          }
        });
      }
    }, Emitter.BackpressureMode.LATEST).subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {

      }
    });
  }

  private static void intSyncOnSubscribe() {

    SyncOnSubscribe<Integer, Integer> stateful =
        SyncOnSubscribe.createStateful(new Func0<Integer>() {
          @Override public Integer call() {
            return 0;
          }
        }, new Func2<Integer, Observer<? super Integer>, Integer>() {
          @Override public Integer call(Integer current, Observer<? super Integer> output) {
            output.onNext(current);
            return current + 1;
          }
        }, new Action1<Integer>() {
          @Override public void call(Integer e) {
          }
        });

    Observable.create(stateful).subscribe(new Subscriber<Integer>() {

      @Override public void onStart() {
        request(1);
      }

      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(Integer integer) {
        System.out.println(integer);

        if (integer <= 10) request(1);
      }
    });
  }

  /**
   * Sometimes, the data source to be converted into the reactive world itself is synchronous
   * (blocking) and pull-like, that is, we have to call some get or read method to get the next
   * piece of data. One could, of course, turn that into an Iterable but when such sources are
   * associated with resources, we may leak those resources if the downstream unsubscribes the
   * sequence before it would end.
   */
  private static void binarySyncOnSubscribe() {

    SyncOnSubscribe<InputStream, Integer> binaryReader =
        SyncOnSubscribe.createStateful(new Func0<InputStream>() {
          @Override public InputStream call() {
            try {
              return new FileInputStream("data.bin");
            } catch (FileNotFoundException e) {
              RxJavaHooks.onError(e);
            }
            return null;
          }
        }, new Func2<InputStream, Observer<? super Integer>, InputStream>() {
          @Override
          public InputStream call(InputStream inputStream, Observer<? super Integer> observer) {

            try {
              int byteCode = inputStream.read();
              if (byteCode < 0) {
                observer.onCompleted();
              } else {
                observer.onNext(byteCode);
              }
            } catch (IOException ex) {
              observer.onError(ex);
            }

            return inputStream;
          }
        }, new Action1<InputStream>() {
          @Override public void call(InputStream inputStream) {
            try {
              inputStream.close();
            } catch (IOException e) {
              RxJavaHooks.onError(e);
            }
          }
        });

    Observable.create(binaryReader).subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {

      }
    });
  }

  private static void fromCallable() {
    Observable<Integer> observable = Observable.fromCallable(new Callable<Integer>() {
      @Override public Integer call() throws Exception {
        return computeValue();
      }
    });
    observable.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println("integer = [" + integer + "]");
      }
    });
    observable.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println("integer = [" + integer + "]");
      }
    });
  }

  private static int counter;

  static int computeValue() {
    return ++counter;
  }

  private static void just() {

    //Observable<Integer> observable = Observable.just(computeValue());

    //the same as
    final int temp = computeValue();
    Observable<Integer> observable = Observable.just(temp);

    observable.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println(integer);
      }
    });

    observable.subscribe(new Action1<Integer>() {
      @Override public void call(Integer integer) {
        System.out.println(integer);
      }
    });
  }
}
