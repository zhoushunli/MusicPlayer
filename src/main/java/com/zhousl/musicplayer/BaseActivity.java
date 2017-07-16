package com.zhousl.musicplayer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhousl.musicplayer.util.UIUtil;

/**
 * Created by shunli on 2017/4/23.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        makeStatusBarTranslucent();
    }

    private void init() {
        if (getRes()<=0)
            throw new IllegalStateException("content cannot be null");
        setContentView(getRes());
    }

    /**
     * 子类重写提供子布局
     *
     * @return
     */
    protected abstract int getRes();

    private void makeStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
