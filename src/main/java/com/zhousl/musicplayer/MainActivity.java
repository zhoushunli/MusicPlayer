package com.zhousl.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.zhousl.musicplayer.adapter.HomePageAdapter;
import com.zhousl.musicplayer.constants.Action;
import com.zhousl.musicplayer.frag.LocalFrag;
import com.zhousl.musicplayer.frag.NetFrag;
import com.zhousl.musicplayer.service.PlayService;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    //用于和Playservice进行通信的binder对象
    private PlayService.MyBinder mBinder;
    private Intent mService;
    private MyConn mServiceConnection;
    private TextView localTab;
    private TextView remoteTag;
    private ViewPager pagerContent;
    private static final float SCALE_FACTOR = 1.2f;
    private static final long DEFAULT_ANIMATE_DURATION = 200;
    //当前正在播放的音乐
    private Music mMusic;
    private PlayingChangeReceiver mReceiver;
    private LocalFrag mLocalFrag;
    private NetFrag mNetFrag;
    private View mPlay;
    private View mNext;
    private View mLast;
    private TextView mTitle;
    private TextView mArtist;
    private View mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initService();
    }

    @Override
    protected int getRes() {
        return R.layout.activity_main;
    }

    private void initView() {
        localTab = (TextView) findViewById(R.id.local);
        remoteTag = (TextView) findViewById(R.id.remote);
        pagerContent = (ViewPager) findViewById(R.id.pager_content);
        mTitle = (TextView) findViewById(R.id.music_title);
        mArtist = (TextView) findViewById(R.id.artist);
        mController = findViewById(R.id.controller);
        mLast = findViewById(R.id.last);
        mNext = findViewById(R.id.next);
        mPlay = findViewById(R.id.play);
        localTab.setOnClickListener(this);
        remoteTag.setOnClickListener(this);
        mLast.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        pagerContent.setAdapter(new HomePageAdapter(getSupportFragmentManager(), prepareFrag()));
        pagerContent.addOnPageChangeListener(this);
        localTab.setSelected(true);
        localTab.setScaleX(SCALE_FACTOR);
        localTab.setScaleY(SCALE_FACTOR);
    }

    private ArrayList<Fragment> prepareFrag() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        mLocalFrag = new LocalFrag();
        fragments.add(mLocalFrag);
        mNetFrag = new NetFrag();
        fragments.add(mNetFrag);
        return fragments;
    }

    private void initService() {
        if (mService == null) {
            mService = new Intent(this, PlayService.class);
            startService(mService);
        }
        mServiceConnection = new MyConn();
        bindService(mService, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new PlayingChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Action.PLAY_NEXT);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
    }

    public void onPlayStart(int index, Music music) {
        refreshControllerStatus(music);
    }

    @Override
    public void onClick(View v) {
        if (getMusic() == null)
            return;
        if (v == mLast) {
            getBinder().stop(getMusic());
            mLocalFrag.getMusic().setPlaying(false);
            mLocalFrag.doPlayMusic(getCurIndex() - 1);
            mLocalFrag.notifyPlayingChanged();
        } else if (v == mNext) {
            getBinder().stop(getMusic());
            mLocalFrag.getMusic().setPlaying(false);
            mLocalFrag.doPlayMusic(getCurIndex() + 1);
            mLocalFrag.notifyPlayingChanged();
        } else if (v == mPlay) {
            if (getMusic().isPlaying()) {
                getBinder().pause(getMusic());
                mPlay.setSelected(false);
            } else {
                if (getMusic().getCurPosition() == 0) {
                    mLocalFrag.doPlayMusic(getCurIndex());
                } else {
                    getBinder().resume(getMusic());
                }
                mPlay.setSelected(true);
                mLocalFrag.getMusic().setPlaying(true);
                mLocalFrag.notifyPlayingChanged();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("onpages--", positionOffset + "");
        if (positionOffset == 0)
            return;
        localTab.setScaleX((SCALE_FACTOR - 1) * (1 - positionOffset) + 1);
        localTab.setScaleY((SCALE_FACTOR - 1) * (1 - positionOffset) + 1);
        remoteTag.setScaleX((SCALE_FACTOR - 1) * positionOffset + 1);
        remoteTag.setScaleY((SCALE_FACTOR - 1) * positionOffset + 1);
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            localTab.setSelected(true);
            remoteTag.setSelected(false);
        } else {
            localTab.setSelected(false);
            remoteTag.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void doScale(View target, float factor) {
        ScaleAnimation sa = new ScaleAnimation(1, factor, 1, factor);
        sa.setDuration(DEFAULT_ANIMATE_DURATION);
        sa.setFillAfter(false);
        target.startAnimation(sa);
    }

    public Music getMusic() {
        return mBinder.getMusic();
    }

    public int getCurIndex() {
        return mBinder.getIndex();
    }

    public PlayService.MyBinder getBinder() {
        return mBinder;
    }

    public Intent getService() {
        return mService;
    }

    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service == null)
                return;
            if (service instanceof PlayService.MyBinder) {
                MainActivity.this.mBinder = (PlayService.MyBinder) service;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    public class PlayingChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(Action.PLAY_NEXT)) {
                mLocalFrag.notifyPlayingChanged();
                int index = intent.getIntExtra(Action.IntentKey.INDEX, -1);
                refreshControllerStatus(mLocalFrag.getMusicList().get(index));
            }
        }
    }

    private void refreshControllerStatus(Music music) {
        mTitle.setText(music.getName());
        mArtist.setText(music.getArtist());
        if (music.isPlaying()) {
            mPlay.setSelected(true);
        } else {
            mPlay.setSelected(false);
        }
    }
}
