package com.smartdengg.reactive2;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * 创建时间:  2017/03/06 15:56 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Parallel {

  public static void main(String[] args) throws InterruptedException {

    //Entity entity = null;
    //
    //if (entity instanceof Entity) {
    //  System.out.printf("ssssss");
    //} else {
    //  System.out.printf("sssaaaaaaaa");
    //}

    //ParallelFlowable<Integer> source = Flowable.range(1, 1000).parallel();
    //
    //Flowable<Integer> sequential = source.runOn(Schedulers.io()).sequential();

    // Strong references
    Foo foo = new Foo();
    //
    //// Soft References
    //SoftReference<Foo> softReference = new SoftReference<>(foo);
    //Foo softRef;
    //
    //if ((softRef = softReference.get()) != null) {
    //  /*do stuff*/
    //  System.out.println(softRef.toString());
    //}
    //
    //// Weak Reference
    //ReferenceQueue<Foo> referenceQueue = new ReferenceQueue<>();
    //WeakReference<Foo> weakReference = new WeakReference<>(foo, referenceQueue);
    //
    //System.out.println(weakReference.toString());
    //
    //System.gc();
    //System.gc();
    //System.gc();
    //System.gc();
    //
    //WeakReference weakReference;
    //while ((weakReference = (WeakReference) referenceQueue.poll()) != null) {
    //  System.out.println(weakReference.toString());
    //}
    //
    //WeakReference poll = (WeakReference) referenceQueue.poll();
    //Reference<? extends Foo> remove = referenceQueue.remove();

    // Phantom References
    ReferenceQueue<Foo> referenceQueue = new ReferenceQueue<>();
    PhantomReference<Foo> phantomReference = new PhantomReference<>(foo, referenceQueue);
    Foo ref = phantomReference.get();

    //foo = null;
    System.gc();

    Reference<? extends Foo> reference = referenceQueue.poll();
    if (reference != null) {
      System.out.println("reference.toString() = null");
    } else {
      System.out.println("reference.toString() = ssssasdasdasdasdsdasdasdsa ");
    }

    //while ((reference = (PhantomReference) referenceQueue.poll()) != null) {
    //  System.out.println(reference.toString());
    //}

    //ReferenceQueue<Foo> referenceQueue = new ReferenceQueue<>();
    //WeakReference<Foo> weakReference = new WeakReference<>(foo, referenceQueue);

    //foo = null;
    //System.gc();

    //Foo ref = weakReference.get();

    for (; ; ) ;
  }

  class Entity {

  }

  static class Foo {
  }

  private void test1() { // GC无法回收，b还在局部变量表中

    byte[] b = new byte[6 * 1024 * 1024];

    System.gc();
  }

  private void test2() { // GC可以回收，赋值null将销毁局部变量表中数据

    byte[] b = new byte[6 * 1024 * 1024];

    b = null;
    System.gc();
  }
}
