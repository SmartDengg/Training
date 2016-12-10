package com.smartdengg.training;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 创建时间:  2016/11/30 11:28 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ConditionTest {

  private static class NumberWrapper {
    int value = 1;
  }

  public static void main(String[] args) {

    //conditionLock();

    //objectLock();
  }

  private static void objectLock() {
    final NumberWrapper num = new NumberWrapper();

    final Object reachThreeMute = new Object();
    final Object reachSixMute = new Object();

    Thread threadA = new Thread(new Runnable() {
      @Override public void run() {

        synchronized (reachThreeMute) {
          System.out.println("threadA start write");
          while (num.value <= 3) System.out.println(num.value++);
          reachThreeMute.notify();
        }

        synchronized (reachSixMute) {
          try {
            while (num.value < 6) reachSixMute.wait();
            System.out.println("threadA start write");
            while (num.value <= 9 && num.value >= 6) System.out.println(num.value++);
          } catch (InterruptedException ignored) {
          }
        }
      }
    });

    Thread threadB = new Thread(new Runnable() {
      @Override public void run() {

        try {
          synchronized (reachThreeMute) {
            while (num.value < 3) reachThreeMute.wait();
          }
        } catch (InterruptedException ignored) {
        }

        synchronized (reachSixMute) {

          System.out.println("threadB start write");
          while (num.value >= 3 && num.value <= 6) System.out.println(num.value++);
          reachSixMute.notify();
        }
      }
    });

    threadA.start();
    threadB.start();
  }

  private static void conditionLock() {
    final Lock lock = new ReentrantLock(true);

    final Condition reachThreeCondition = lock.newCondition();
    final Condition reachSixCondition = lock.newCondition();

    final NumberWrapper num = new NumberWrapper();

    Thread threadA = new Thread(new Runnable() {
      @Override public void run() {

        lock.lock();
        try {
          System.out.println("threadA start write");

          while (num.value <= 3) System.out.println(num.value++);

          reachThreeCondition.signal();
        } finally {
          lock.unlock();
        }

        lock.lock();
        try {
          reachSixCondition.await();
          System.out.println("threadA start write");

          while (num.value <= 9 && num.value >= 6) System.out.println(num.value++);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
        }
      }
    });

    Thread threadB = new Thread(new Runnable() {
      @Override public void run() {

        lock.lock();
        try {
          while (num.value <= 3) reachThreeCondition.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          lock.unlock();
        }

        lock.lock();
        try {
          System.out.println("threadB start write");
          while (num.value >= 3 && num.value <= 6) System.out.println(num.value++);
          reachSixCondition.signal();
        } finally {
          lock.unlock();
        }
      }
    });

    threadA.start();
    threadB.start();
  }
}
