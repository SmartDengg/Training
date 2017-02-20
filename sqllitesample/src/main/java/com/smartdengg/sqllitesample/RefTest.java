package com.smartdengg.sqllitesample;

/**
 * 创建时间:  2017/02/15 15:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class RefTest {

  public static void main(String[] args) {

    Person person = new Person();
    person.name = "deng";

    System.out.println(person.name);

    transfer(person);

    System.out.println(person.name);
  }

  private static void transfer(Person person) {

    Person another = person;

    another.name = "wei";
  }

  static class Person {

    public String name;
  }
}
