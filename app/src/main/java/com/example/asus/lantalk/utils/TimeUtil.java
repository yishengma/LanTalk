package com.example.asus.lantalk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间的选择
 */

public class TimeUtil {

    public static String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return format.format(curDate);
    }

}
