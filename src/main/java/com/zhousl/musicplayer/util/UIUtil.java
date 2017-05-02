package com.zhousl.musicplayer.util;

import android.content.Context;
import android.util.Pair;

import java.lang.reflect.Field;

/**
 * Created by shunli on 2017/4/28.
 */

public class UIUtil {

    /**
     * 获取屏幕尺寸
     * @param context
     * @return
     */
    public static Pair<Integer, Integer> getScreenDimen(Context context) {
        return null;
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Field field = clazz.getField("status_bar_height");
            Object obj = clazz.newInstance();
            int i = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
