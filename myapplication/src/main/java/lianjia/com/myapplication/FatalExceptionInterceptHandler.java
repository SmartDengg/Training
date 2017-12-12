package lianjia.com.myapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.util.Log;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * 创建时间:  2017/11/14 15:36 <br>
 * 作者:  dengwei <br>
 * 描述:  对致命的，会造成程序退出的异常进行拦截，保证所拦截的异常不会上报给fabric
 * <p> fabric一定不能被混淆
 */
@Keep class FatalExceptionInterceptHandler implements Thread.UncaughtExceptionHandler {

  private static final String crashlyticsClassName =
      "com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler";
  private static final String crashlyticsHandlerName = "defaultHandler";

  private static final Looper mainLooper = Looper.getMainLooper();
  private static final Handler mainHandler = new Handler(mainLooper);
  private static final Set<Class<? extends Throwable>> FATAL = new LinkedHashSet<>();
  static final Stack<WeakActivityWrapper<Activity>> activities = new Stack<>();
  static final ReferenceQueue<Activity> referenceQueue = new ReferenceQueue<>();
  private Thread.UncaughtExceptionHandler defaultHandler;

  static {
    FATAL.add(NullPointerException.class);
    FATAL.add(ClassNotFoundException.class);
    FATAL.add(IllegalAccessException.class);
    FATAL.add(IllegalArgumentException.class);
    FATAL.add(IllegalStateException.class);
    FATAL.add(IndexOutOfBoundsException.class);
  }

  FatalExceptionInterceptHandler(Application application) {
    application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
      @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

      }

      @Override public void onActivityStarted(Activity activity) {
        activities.add(new WeakActivityWrapper<>(activity, referenceQueue));
      }

      @Override public void onActivityResumed(Activity activity) {

      }

      @Override public void onActivityPaused(Activity activity) {

      }

      @Override public void onActivityStopped(Activity activity) {
        //noinspection ForLoopReplaceableByForEach
        for (Iterator<WeakActivityWrapper<Activity>> iterator = activities.iterator();
            iterator.hasNext(); ) {

          final WeakActivityWrapper activityWrapper = iterator.next();
          final Activity maybeGCActivity = (Activity) activityWrapper.get();

          if (maybeGCActivity == null || maybeGCActivity == activity) {
            activities.remove(activityWrapper);
            break;
          }
        }
      }

      @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

      }

      @Override public void onActivityDestroyed(Activity activity) {
        if (activity.getClass().getCanonicalName().equals(MainActivity.class.getCanonicalName())) {
          WeakActivityWrapper wrapper;
          while ((wrapper = (WeakActivityWrapper) referenceQueue.poll()) != null) {
            activities.remove(wrapper);
          }
        }
      }
    });

    this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    mainHandler.post(new Runnable() {
      @Override public void run() {
        //noinspection InfiniteLoopStatement
        for (; ; ) {
          try {
            Looper.loop();
          } catch (Throwable throwable) {
            final Thread mainThread = mainLooper.getThread();
            FatalExceptionInterceptHandler.this.uncaughtException(mainThread, throwable);
          }
        }
      }
    });
  }

  public void uncaughtException(Thread thread, Throwable fatalThrowable) {
    final Class<? extends Throwable> fatalExceptionType = fatalThrowable.getClass();
    if (FATAL.contains(fatalExceptionType)) {

      final String classString = defaultHandler.getClass().getCanonicalName();

      if (classString.equals(crashlyticsClassName)) {

        try {
          final Class crashlyticsClass =
              Class.forName(crashlyticsClassName, false, this.getClass().getClassLoader());
          final Field defaultHandlerField =
              crashlyticsClass.getDeclaredField(crashlyticsHandlerName);

          if (!Modifier.isPublic(defaultHandlerField.getModifiers())) {
            defaultHandlerField.setAccessible(true);
          }

          this.defaultHandler =
              (Thread.UncaughtExceptionHandler) defaultHandlerField.get(this.defaultHandler);
        } catch (Exception ignored) {
          /*don'thread rethrow the exception*/
        }
      }

      Map<String, String> map = new HashMap<>();
      map.put("1", "11");
      map.put("2", "22");
      map.put("3", "33");
      map.put("4", "44");

      /*log non-fatal to fabric*/
      Log.e("deng",
          Log.getStackTraceString(new FatalInterceptException(map.toString(), fatalThrowable)));

      this.tryToFinishTopActivity();

      return;
    }
    defaultHandler.uncaughtException(thread, fatalThrowable);
  }

  private void tryToFinishTopActivity() {

    if (!activities.isEmpty()) {
      final WeakActivityWrapper<Activity> topActivityWrapper = activities.peek();
      if (topActivityWrapper != null) {
        final Activity activity = topActivityWrapper.get();
        if (activity != null && !activity.hasWindowFocus()) {
          activity.finish();
          Log.e("deng",
              activity.getClass().getCanonicalName() + "occur an exception, we try to finish it.");
          mainHandler.post(new Runnable() {
            @Override public void run() {
              activities.remove(topActivityWrapper);
            }
          });
        }
      }
    }
  }

  private static class WeakActivityWrapper<T> extends WeakReference<T> {

    WeakActivityWrapper(T referent, ReferenceQueue<? super T> q) {
      super(referent, q);
    }
  }

  private static class FatalInterceptException extends Exception {

    private static final long serialVersionUID = 7136344559571078425L;

    FatalInterceptException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}

