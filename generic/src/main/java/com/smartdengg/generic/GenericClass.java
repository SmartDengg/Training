package com.smartdengg.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 创建时间:  2017/04/24 13:44 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class GenericClass<T extends Runnable> {

  private List<T> list;                     // 2
  private Map<String, T> map;               // 3

  public static void main(String[] args) {

    List<String> list = null;       // 1
    list = new ArrayList<>(); // 2
    list.add("s");

    //List<Integer> integerList = new ArrayList<>();
    //Class<? extends List> aClass = integerList.getClass();
    //popAll(integerList);

    //Class<? extends List> c = list.getClass();
    //TypeVariable[] genericSuperclass = c.getTypeParameters();
    //
    //Field field = null;
    //Type genericType = field.getGenericType();

    //List<String>[] lists = new ArrayList[1];
    //
    //List<Integer> integerList = Arrays.asList(1, 2);
    //
    ////noinspection UnnecessaryLocalVariable
    //Object[] objects = lists;
    //
    //objects[0] = integerList;
    //
    //String s = lists[0].get(0);


    Long[] longs  ={0L,1L,2L,4L,5L,6L,7L};

    List<Long> arrayList = new ArrayList<>();
    arrayList.add(1L);
    arrayList.add(2L);
    arrayList.add(3L);
    arrayList.add(4L);
    Long[] l = arrayList.toArray(longs);//这个语句会出现ClassCastException

    for (; ; ) ;
  }

  public <U extends CharSequence> U genericMethod(Map<T, U> m, List<String> list) { // 4

    List<?>[] lists = new List[1];

    return null;
  }

  public static void popAll(Collection<? extends Number> collection) {
  }

  public static void as(int... ints) {

    List<int[]> ints1 = Arrays.asList(ints);
  }
}
