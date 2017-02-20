package com.smartdengg.training;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SmartDengg on 2016/12/11.
 */
public class ExtendsTest {

  public static void main(String[] args) {

    Entity entity = new Entity("deng");
    List<Entity> singletonList = Collections.singletonList(entity);

    List<Entity> list = new ArrayList<>(singletonList);
    list.get(0).name = "wei";

    System.out.println(entity.name);
    System.out.println(list.get(0).name);

    Map<String, List<String>> map = new HashMap<>();

    Map<String, List<String>> map1 = newInstance();
  }

  public static <K, V> HashMap<K, V> newInstance() {
    return new HashMap<K, V>();
  }

  static class Entity {

    String name;

    public Entity(String name) {
      this.name = name;
    }
  }

  class Parent {

    Object test(String s) {
      return null;
    }
  }

  class Son extends Parent {

    @Override String test(String s) {
      super.test(s);
      return null;
    }

    String test(CharSequence s) {
      return null;
    }
  }




}
