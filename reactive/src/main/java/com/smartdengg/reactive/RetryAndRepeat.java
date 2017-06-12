package com.smartdengg.reactive;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 创建时间:  2017/06/01 17:25 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class RetryAndRepeat {

  public static void main(String[] args) throws NoSuchMethodException {

    Method test = RetryAndRepeat.class.getMethod("test");
    Annotation[] annotations = test.getAnnotations();

    System.out.println(Arrays.toString(annotations));

    System.out.println("The count in the \'@Retry\' is less than zero");

    for (; ; ) ;
  }

  @Retention(RetentionPolicy.SOURCE) @interface SourceType {
  }

  @Retention(RetentionPolicy.CLASS) @interface ClassType {
  }

  @Retention(RetentionPolicy.RUNTIME) @interface RuntimeType {
  }

  @SourceType @ClassType @RuntimeType public void test() {
  }
}
