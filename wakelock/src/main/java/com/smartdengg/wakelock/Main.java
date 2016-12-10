package com.smartdengg.wakelock;

import java.util.Calendar;
import java.util.Date;

/**
 * 创建时间:  2016/12/06 22:25 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Main {

  public static void main(String[] args) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 24);
    Date time = calendar.getTime();

    System.out.println(time);
  }
}
