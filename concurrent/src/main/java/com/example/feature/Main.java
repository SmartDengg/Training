package com.example.feature;

public class Main {

  public static void main(String[] args) {

    Client client = new Client();
    Data data = client.request("Hello world");

    try {
      /*模拟其他业务逻辑*/
      System.out.println("--> 模拟业务耗时操作");
      Thread.sleep(2000);
      System.out.println("<-- 模拟业务耗时操作");
    } catch (InterruptedException ignored) {
    }

    System.out.println(data.getResult());

    for (; ; ) ;
  }
}
