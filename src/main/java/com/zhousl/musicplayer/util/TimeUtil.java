package com.zhousl.musicplayer.util;

/**
 * Created by shunli on 2017/7/16.
 */

public class TimeUtil {

    public static String getFormattedTimeStr(long time) {
        if (time <= 0)
            return "";
        if (time > 1000) {
            long seconds = time / 1000;
            if (seconds >= 60) {
                long minutes = seconds / 60;
                if (minutes >= 60) {
                    long hour = minutes / 60;
                    return hour + ":" + (minutes - hour * 60) + ":" + (time - hour * 3600 - minutes * 60);
                } else {
                    return seconds + ":" + (time - minutes * 60);
                }
            } else {
                return "00:" + seconds;
            }
        } else {
            return "";
        }
    }

    private static long getSeconds(long time) {
        return time / 1000;
    }

}
