package com.example.concurrent;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * 创建时间:  2017/07/17 17:24 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Main {

  public static void main(String[] args) {

    TreeMap<Person, Person> treeMap = new TreeMap<>(new Comparator<Person>() {
      @Override public int compare(Person o1, Person o2) {
        int i = o1.age - o2.age;
        return i == 0 ? 1 : i;
      }
    });

    Person person1 = new Person("1", 1);
    Person person2 = new Person("2", 2);
    Person person3 = new Person("3", 3);
    Person person4 = new Person("4", 3);
    treeMap.put(person1, person1);
    treeMap.put(person2, person2);
    treeMap.put(person3, person3);
    treeMap.put(person4, person4);

    Set<Person> persons = treeMap.keySet();
    for (Iterator<Person> iterator = persons.iterator(); iterator.hasNext(); ) {
      Person next = iterator.next();
      System.out.println(next);
    }

    for (; ; ) ;
  }

  private static class Person {

    String name;
    int age;

    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override public String toString() {
      return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
  }
}
