package com.zhousl.musicplayer;

import android.os.Build;
import android.os.Bundle;
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

    //作为根布局
    private LinearLayout mBaseContainer;
    //用于占据系统状态栏，避免因高版本的状态栏透明后布局内容顶到了状态栏里
    private View mStatusBarFillView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);
        initView();
        makeStatusBarTranslucent();
    }
    private void initView(){
        mBaseContainer= (LinearLayout) findViewById(R.id.base_container);
        mStatusBarFillView=findViewById(R.id.status_bar_fill_view);
        if (getRes()==0)
            return;
        mBaseContainer.addView(View.inflate(this,getRes(),null));
    }
    /**
     * 子类重写提供子布局
     * @return
     */
    protected abstract int getRes();
    private void makeStatusBarTranslucent(){
        if (Build.VERSION.SDK_INT>=19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = UIUtil.getStatusBarHeight(this);
            mStatusBarFillView.getLayoutParams().height=statusBarHeight;
            mStatusBarFillView.requestLayout();
        }
    }
}
