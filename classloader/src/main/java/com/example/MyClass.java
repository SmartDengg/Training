package com.example;

import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilderFactory;

public class MyClass {

  private static int anInt = 0;
  private static int anInt1 = 11;
  private static int anInt2 = 22;
  public static final int v = 8080;

  public static void main(String[] args) throws ClassNotFoundException {

    /*引导类加载器，又称启动类加载器，是最顶层的类加载器，主要用来加载Java核心类，
    如rt.jar、resources.jar、charsets.jar等，Sun的JVM中，
    执行java的命令中使用-Xbootclasspath选项或使用- D选项指定sun.boot.class.path系统属性值可以指定附加的类，
    它不是 java.lang.ClassLoader的子类，而是由JVM自身实现的该类c 语言实现，
    Java程序访问不到该加载器。通过下面代码可以查看该加载器加载了哪些jar包*/
    System.out.println("=============");
    URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
    for (int i = 0; i < urls.length; i++) {
      System.out.println(urls[i].toExternalForm());
    }
    System.out.println("=============");

    /*扩展类加载器，主要负责加载Java的扩展类库，默认加载JAVA_HOME/jre/lib/ext/目下的所有jar包或者由java.ext.dirs系统属性指定的jar包。
    放入这个目录下的jar包对所有AppClassloader都是可见的（后面会知道ExtClassloader是AppClassloader的父加载器)*/
    //System.out.println(System.getProperty("java.ext.dirs"));
    System.out.println("=============");
    File[] extDirs = getExtDirs();
    for (int i = 0, n = extDirs.length; i < n; i++) {
      System.out.println(extDirs[i].toString());
    }
    System.out.println("=============");

    /*系统类加载器，又称应用加载器，本文说的SystemClassloader和APPClassloader是一个东西，它负责在JVM启动时，
    加载来自在命令java中的-classpath或者java.class.path系统属性或者 CLASSPATH操作系统属性所指定的JAR类包和类路径。
    调用ClassLoader.getSystemClassLoader()可以获取该类加载器。如果没有特别指定，则用户自定义的任何类加载器都将该类加载器作为它的父加载器,*/
    //System.out.println(System.getProperty("java.class.path"));
    System.out.println("=============");
    File[] classPath = getClassPath(System.getProperty("java.class.path"));
    for (int i = 0, n = classPath.length; i < n; i++) {
      System.out.println(classPath[i].toString());
    }
    System.out.println("=============");

    //采用双亲委派的一个好处是比如加载位于rt.jar包中的类java.lang.Object，不管是哪个加载器加载这个类，
    // 最终都是委托给顶层的启动类加载器进行加载，这样就保证了使用不同的类加载器最终得到的都是同样一个Object对象。
    ClassLoader classLoader = Object.class.getClassLoader();
    while (classLoader != null) {
      System.out.println(classLoader);
      classLoader = classLoader.getParent();
    }

    String cls = System.getProperty("java.system.class.loader");
    System.out.println(cls);

    System.out.println(
        "DocumentBuilderFactory classLoader = " + DocumentBuilderFactory.class.getClassLoader());

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    System.out.println(
        "DocumentBuilderFactory instance classLoader = " + documentBuilderFactory.getClass()
            .getClassLoader());

    Class<?> aClass =
        MyClass.class.getClassLoader().loadClass("javax.xml.parsers.DocumentBuilderFactory");

    for (; ; ) ;
  }

  private static File[] getExtDirs() {
    String var0 = System.getProperty("java.ext.dirs");
    File[] var1;
    if (var0 != null) {
      StringTokenizer var2 = new StringTokenizer(var0, File.pathSeparator);
      int var3 = var2.countTokens();
      var1 = new File[var3];

      for (int var4 = 0; var4 < var3; ++var4) {
        var1[var4] = new File(var2.nextToken());
      }
    } else {
      var1 = new File[0];
    }

    return var1;
  }

  private static File[] getClassPath(String var0) {
    File[] var1;
    if (var0 != null) {
      int var2 = 0;
      int var3 = 1;
      boolean var4 = false;

      int var5;
      int var7;
      for (var5 = 0; (var7 = var0.indexOf(File.pathSeparator, var5)) != -1; var5 = var7 + 1) {
        ++var3;
      }

      var1 = new File[var3];
      var4 = false;

      for (var5 = 0; (var7 = var0.indexOf(File.pathSeparator, var5)) != -1; var5 = var7 + 1) {
        if (var7 - var5 > 0) {
          var1[var2++] = new File(var0.substring(var5, var7));
        } else {
          var1[var2++] = new File(".");
        }
      }

      if (var5 < var0.length()) {
        var1[var2++] = new File(var0.substring(var5));
      } else {
        var1[var2++] = new File(".");
      }

      if (var2 != var3) {
        File[] var6 = new File[var2];
        System.arraycopy(var1, 0, var6, 0, var2);
        var1 = var6;
      }
    } else {
      var1 = new File[0];
    }

    return var1;
  }

  //public static void testContextClassLoader() {
  //  ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
  //  for (Driver driver : loader) {
  //    System.out.println(
  //        "driver:" + driver.getClass() + ",loader:" + driver.getClass().getClassLoader());
  //  }
  //  System.out.println(
  //      "current thread contextloader:" + Thread.currentThread().getContextClassLoader());
  //
  //  //System.out.println("current loader:" + MyContextClassLoad.class.getClassLoader());
  //  System.out.println("ServiceLoader loader:" + ServiceLoader.class.getClassLoader());
  //}
}
