package com.zhousl.musicplayer;

import android.app.Application;
import android.content.Context;

/**
 * Created by inshot-user on 2017/6/7.
 */

public class MyApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
    }

    public static Context getAppContext(){
        return appContext;
    }
}
