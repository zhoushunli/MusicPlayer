package com.zhousl.musicplayer.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.zhousl.musicplayer.MyApplication;

/**
 * Created by inshot-user on 2017/7/24.
 */

public class IntentUtil {

    private static final String MY_EMAIL="zhousl.check@gmail.com";

    public static Intent getMarketIntent(String marketPkg) {
        Uri uri = Uri.parse("market://details?id=" + MyApplication.getAppContext().getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (marketPkg != null) {// 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
            intent.setPackage(marketPkg);
        }
        return intent;
    }

    public static Intent getMarketIntent() {
        return getMarketIntent(null);
    }

    public static Intent getEmailIntent(){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:"+MY_EMAIL));
        data.putExtra(Intent.EXTRA_SUBJECT, MyApplication.getAppContext().getApplicationInfo().name+"Feedback");
        data.putExtra(Intent.EXTRA_TEXT, deviceInfo());
        return data;
    }

    private static String deviceInfo(){
        String board = Build.BOARD;
        String bootloader = Build.BOOTLOADER;
        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String display = Build.DISPLAY;
        return null;
    }
}
