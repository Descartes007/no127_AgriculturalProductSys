package com.agricultural.em.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {


    // 获取当前时间的方法，返回格式为yyyy-MM-dd HH:mm:ss
    public static String getCurrentTime() {
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 定义时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 格式化当前时间并返回
        return currentTime.format(formatter);
    }

}
