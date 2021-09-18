package com.hd.notification.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/9/18
 */
public class FormatUtil {
    /**
     * 时间格式化 12:21:33
     *
     * @param ms 毫秒
     * @return
     */
    public static String formatMs(long ms) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00"));
        return format.format(calendar.getTime());
    }
}
