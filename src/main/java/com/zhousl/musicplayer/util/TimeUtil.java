package com.zhousl.musicplayer.util;

/**
 * Created by shunli on 2017/7/16.
 */

public class TimeUtil {

    public static String getFormattedTimeStr(long time) {
        if (time <= 0)
            return "00:00";
        long seconds = getSeconds(time);
        if (seconds < 60) {
            return getFormattedSubTimeStr(seconds);
        } else {
            long minutes = getMinutes(time);
            if (minutes < 60) {
                return getFormattedSubTimeStr(minutes) + ":" + getFormattedSubTimeStr(seconds - minutes * 60);
            } else {
                long hour = getHour(time);
                return getFormattedSubTimeStr(hour) + ":"
                        + getFormattedSubTimeStr(minutes - hour * 60) + ":"
                        + getFormattedSubTimeStr(seconds - minutes * 60 - hour * 3600);
            }
        }
    }

    private static long getSeconds(long time) {
        return time / 1000;
    }

    private static String getFormattedSubTimeStr(long time) {
        return time >= 10 ? (time + "") : ("0" + time);
    }

    private static long getMinutes(long time) {
        long seconds = getSeconds(time);
        return seconds / 60;
    }

    private static long getHour(long time) {
        long minutes = getMinutes(time);
        return minutes / 60;
    }

}
