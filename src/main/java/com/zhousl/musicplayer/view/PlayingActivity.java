package com.zhousl.musicplayer.view;

import android.os.Bundle;

import com.zhousl.musicplayer.BaseActivity;
import com.zhousl.musicplayer.R;

/**
 * Created by shunli on 2017/5/1.
 */

public class PlayingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusColor(R.color.transparent);
        changeStatusHeight(0);
        findViewById(R.id.toolbar).setFitsSystemWindows(true);
    }

    @Override
    protected int getRes() {
        return R.layout.act_play;
    }
}
