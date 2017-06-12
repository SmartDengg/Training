package com.smartdengg.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 创建时间:  2017/05/23 17:55 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class CollectionClassifier {

  public static String classifier(Set<?> set) {
    return "Set";
  }

  public static String classifier(List<?> list) {
    return "List";
  }

  public static String classifier(Collection<?> collection) {
    return "Unknown Collection";
  }

  public static void main(String[] args) {

    Classifier classifier = new Classifier();

    for (; ; ) ;
  }

  private static class Classifier {

    Classifier() {

      Collection[] collections =
          { new HashSet(), new ArrayList(), new HashMap<String, String>().values() };

      for (Collection collection : collections) {
        System.out.println(classifier(collection));
      }
    }

    String classifier(Set<?> set) {
      return "Set";
    }

    String classifier(List<?> list) {
      return "List";
    }

    String classifier(Collection<?> collection) {
      return "Unknown Collection";
    }
  }
}
