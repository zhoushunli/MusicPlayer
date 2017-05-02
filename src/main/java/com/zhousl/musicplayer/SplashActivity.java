package com.zhousl.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhousl.musicplayer.interf.SimpleAnimationListener;

/**
 * Created by shunli on 2017/4/29.
 */

public class SplashActivity extends Activity {

    private ImageView icon;
    private TextView title;
    private static final int MULTI_REQUEST_CODE = 100;
    private static final long SLEEP_TIME = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_splash);
        initView();
        doAnimation();
    }

    private void initView() {
        icon = (ImageView) findViewById(R.id.icon);
        title = (TextView) findViewById(R.id.title);
    }

    private void doAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_icon_anim);
        icon.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.splash_icon_anim);
        animation2.setStartOffset(100);
        title.startAnimation(animation2);
        animation2.setAnimationListener(animationListener);
    }

    private Animation.AnimationListener animationListener = new SimpleAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            } else {
                doNormalLeap();
            }
        }
    };

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MULTI_REQUEST_CODE);
        } else {
            doNormalLeap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MULTI_REQUEST_CODE){
            checkResults(grantResults);
        }
    }
    private void checkResults(int[] grantResults){
        if (grantResults.length==0)
            return;
        for (int result : grantResults) {
            if (result!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"需要必要权限!",Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        doNormalLeap();
    }
    private void doNormalLeap(){
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==0){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(SLEEP_TIME);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
