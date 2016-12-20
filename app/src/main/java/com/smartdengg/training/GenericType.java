package com.smartdengg.training;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间:  2016/12/13 21:34 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class GenericType<E> {

  public static void main(String[] args) {

    /*List<Object> list = new ArrayList<>();
    list.add(1);

    List<?> list1 = new ArrayList<>();
    list1.add("");
    list1.add(1);
    list1.add(null);

    List list2 = new ArrayList();
    list2.add(1);
    list2.add("");

    Object o = null;

    if (o instanceof Set<?>) {
      Set<?> set = (Set<?>) o;
    }*/

  }

  static <E> E reduce(List<E> list, Function<E> function, E value) {

    List<E> list1 = list;
    list1.size();

    E[] array = (E[]) list1.toArray();

    List<E> es = new ArrayList<>(list);
    E result = value;

    for (E e : es) {
      result = function.apply(result, e);
    }
    return result;
  }

  interface Function<T> {
    T apply(T agr1, T arg2);
  }

  private transient Object[] elementData;

  public E get(int index) {

    return (E) elementData[index];
  }
}
