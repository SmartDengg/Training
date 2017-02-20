//package com.smartdengg.training;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
//
///**
// * 创建时间:  2016/12/13 21:34 <br>
// * 作者:  SmartDengg <br>
// * 描述:
// */
//public class GenericType<E> {
//
//  public static void main(String[] args) {
//
//    List<Object> list = new ArrayList<>();
//    list.add(1);
//
//    List<?> list1 = new ArrayList<>();
//    list1.add("");
//    list1.add(1);
//    list1.add(null);
//
//    List list2 = new ArrayList();
//    list2.add(1);
//    list2.add("");
//
//    Object o = null;
//
//    if (o instanceof Set<?>) {
//      Set<?> set = (Set<?>) o;
//    }
//
//    Map<Class<?>, Object> map = new HashMap<>();
//    map.put(String.class, "");
//
//    String cast = String.class.cast("");
//
//    /**/
//    List<String>[] lists = new ArrayList<String>[1];/*协变，可具体化的*/
//    List<Integer> integers = new ArrayList<>();
//
//    Object[] objects = lists;
//    objects[0] = integers;
//
//    String s = lists[0].get(0);
//
//    HashMap<Integer, String> hashMap = GenericType.newHashMap();
//  }
//
//  private static <K, V> HashMap<K, V> newHashMap() {
//    return new HashMap<>();
//  }
//
//  static <E> E reduce(List<E> list, Function<E> function, E value) {
//
//    List<E> list1 = list;
//    list1.size();
//
//    E[] array = (E[]) list1.toArray();
//
//    List<E> es = new ArrayList<>(list);
//    E result = value;
//
//    for (E e : es) {
//      result = function.apply(result, e);
//    }
//    return result;
//  }
//
//  interface Function<T> {
//    T apply(T agr1, T arg2);
//  }
//
//  private transient Object[] elementData;
//
//  public E get(int index) {
//
//    return (E) elementData[index];
//  }
//
//  class Generic {
//
//    public Generic() {
//
//      List<?> list = new ArrayList<String>();
//      generic(list);
//
//      getEntity(new Inner<Integer>());
//    }
//
//    private void generic(List<?> list) {
//
//      list.add(null);
//
//      if (list.contains("")) {
//      }
//
//      String o = (String) list.get(0);
//    }
//
//    private void getEntity(Inner<?> inner) {
//      //inner.list.add("");
//    }
//
//    private void cast(Object o) {
//      if (o instanceof List) {
//        List<?> l = (List<?>) o;
//      }
//    }
//
//    class Inner<T> {
//
//      T entity;
//
//      List<T> list;
//    }
//  }
//}
