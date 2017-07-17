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
            return "00:" + seconds;
        } else {
            long minutes = getMinutes(time);
            if (minutes < 60) {
                return (minutes >= 10 ? minutes : ("0" + minutes)) + ":" + (seconds - minutes * 60);
            } else {
                long hour = getHour(time);
                return (hour >= 10 ? hour : ("0:" + hour)) + ":"
                        + ((minutes - hour * 60) >= 10 ? (minutes - hour * 60) : ("0:" + (minutes - hour * 60))) + ":"
                        + ((seconds - minutes * 60 - hour * 3600) >= 10 ? (seconds - minutes * 60 - hour * 3600) : ("0:" + (seconds - minutes * 60 - hour * 3600)));
            }
        }
    }

    private static long getSeconds(long time) {
        return time / 1000;
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
