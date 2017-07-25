package com.zhousl.musicplayer.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.zhousl.musicplayer.MyApplication;

import java.util.TimeZone;

/**
 * Created by inshot-user on 2017/7/24.
 */

public class IntentUtil {

    private static final String MY_EMAIL = "zhousl.check@gmail.com";

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

    public static Intent getEmailIntent(Activity activity) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + MY_EMAIL));
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            CharSequence label = packageInfo.applicationInfo.loadLabel(activity.getPackageManager());
            data.putExtra(Intent.EXTRA_SUBJECT, label+"-v"+packageInfo.versionName+ " Feedback");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        data.putExtra(Intent.EXTRA_TEXT, deviceInfo());
        return data;
    }

    private static String deviceInfo() {
        StringBuffer buffer = new StringBuffer();
        String model = Build.MODEL;
        int sdkInt = Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;
        String cpuAbi = Build.CPU_ABI;
        String display = Build.DISPLAY;
        buffer.append("Phone:")
                .append(model).append("\n")
                .append("Android Version:")
                .append(sdkInt + "").append("\n")
                .append("Android Release:")
                .append(release).append("\n")
                .append("CPU:")
                .append(cpuAbi).append("\n")
                .append("ScreenSize:")
                .append(display);
        return buffer.toString();
    }
}
