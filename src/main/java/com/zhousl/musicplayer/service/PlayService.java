package com.zhousl.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.MusicPlayer;
import com.zhousl.musicplayer.R;
import com.zhousl.musicplayer.constants.Action;
import com.zhousl.musicplayer.interf.Player;

import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/27.
 */

public class PlayService extends Service implements Player.OnCompleteListener {

    private MusicPlayer mPlayer;
    public static final int REMOTE_VIEW_ID = 0x111;
    private RemoteViews mRemoteView;
    private PlayReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mPlayer == null)
            mPlayer = new MusicPlayer();
        mPlayer.setOnCompleteListener(this);
        initReceiver();
        launchRemoteView();
        initPendingIntent();
    }

    private void initReceiver() {
        mReceiver = new PlayReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Action.REMOTE_PLAY);
        filter.addAction(Action.REMOTE_NEXT);
        filter.addAction(Action.REMOTE_PREVIOUS);
        registerReceiver(mReceiver, filter);
    }

    private void launchRemoteView() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mRemoteView = new RemoteViews(getPackageName(), R.layout.remote_view);
        Notification notification = new Notification();
        notification.icon = R.mipmap.m_album_black;
        notification.contentView = mRemoteView;
        notification.flags=Notification.FLAG_ONGOING_EVENT;
        startForeground(REMOTE_VIEW_ID, notification);
    }

    private void initPendingIntent() {
        //播放和暂停的广播
        Intent intent = new Intent(Action.REMOTE_PLAY);
        intent.putExtra("something","something");
        PendingIntent pauseIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.play, pauseIntent);
        //下一首
        PendingIntent nextIntent = PendingIntent.getBroadcast(this, 1, new Intent(Action.REMOTE_NEXT), PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.next, nextIntent);
        //上一首
        PendingIntent lastIntent = PendingIntent.getBroadcast(this, 2, new Intent(Action.REMOTE_PREVIOUS), PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.last, lastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void play() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void stop() {
    }

    /**
     * 刷新远程view的正确状态
     */
    private void refreshRemoteView() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        stopForeground(true);
        if (mReceiver!=null)
            unregisterReceiver(mReceiver);
    }

    @Override
    public void onCompletion() {

    }

    private void notifyPlayingIndexChanged() {

    }

    public class MyBinder extends Binder {

        public void setMusicList(ArrayList<Music> musicList) {
        }

        public void play(int index) {
        }

        public void pause(Music music) {
        }

        public void resume(Music music) {
        }

        public void stop(Music music) {
        }
        public int getIndex() {
            return 0;
        }
    }

    class PlayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}
