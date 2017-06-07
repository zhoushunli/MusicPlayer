package com.zhousl.musicplayer.util;

import android.content.Context;
import android.provider.MediaStore;

import com.zhousl.musicplayer.MyApplication;

/**
 * Created by inshot-user on 2017/6/7.
 */

public class Preferences {

    public static void putString(String key,String value){
        if (value==null||key==null)
            return;
        MyApplication.getAppContext().getSharedPreferences("stringConfig", Context.MODE_PRIVATE).edit().putString(key,value).apply();
    }
    public static String getString(String key,String defaultValue){
        if (key==null)
            return key;
        return MyApplication.getAppContext().getSharedPreferences("stringConfig",Context.MODE_PRIVATE).getString(key,defaultValue);
    }

    public static void putInt(String key,int value){
        if (key==null)
            return;
        MyApplication.getAppContext().getSharedPreferences("intConfig",Context.MODE_PRIVATE).edit().putInt(key,value).apply();
    }
    public static int getInt(String key,int defaultValue){
        if (key==null)
            return 0;
        return MyApplication.getAppContext().getSharedPreferences("intConfig",Context.MODE_PRIVATE).getInt(key,defaultValue);
    }

    public static void putLong(String key,long value){
        if (key==null)
            return;
        MyApplication.getAppContext().getSharedPreferences("intConfig",Context.MODE_PRIVATE).edit().putLong(key,value).apply();
    }
    public static long getLong(String key,long defaultValue){
        if (key==null)
            return 0;
        return MyApplication.getAppContext().getSharedPreferences("intConfig",Context.MODE_PRIVATE).getLong(key,defaultValue);
    }
}
