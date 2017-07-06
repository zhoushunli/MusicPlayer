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
import android.util.Log;
import android.widget.RemoteViews;

import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.MusicPlayer;
import com.zhousl.musicplayer.R;
import com.zhousl.musicplayer.constants.Action;
import com.zhousl.musicplayer.interf.Player;

import com.zhousl.musicplayer.view.PlayingActivity;

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
//        launchRemoteView();
    }

    private void initReceiver() {
        mReceiver = new PlayReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Action.REMOTE_PLAY);
        filter.addAction(Action.REMOTE_NEXT);
        filter.addAction(Action.REMOTE_PREVIOUS);
        filter.addAction(Action.REMOTE_CLEAR);
        registerReceiver(mReceiver, filter);
    }

    private void launchRemoteView() {
        Music music = getMusic();
        if (music == null)
            return;
        mRemoteView = new RemoteViews(getPackageName(), R.layout.remote_view);
        mRemoteView.setTextViewText(R.id.music_title, music.getName());
        mRemoteView.setTextViewText(R.id.artist, music.getArtist());
        if (music.getState() == Music.MusicState.STATE_PLAYING) {
            mRemoteView.setImageViewResource(R.id.play, R.mipmap.b_play);
        } else {
            mRemoteView.setImageViewResource(R.id.play, R.mipmap.b_pause);
        }
        Notification notification = new Notification();
        notification.icon = R.mipmap.m_album_black;
        notification.contentView = mRemoteView;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        initPendingIntent();
        startForeground(REMOTE_VIEW_ID, notification);
    }

    private void initPendingIntent() {
        //播放和暂停的广播
        Intent intent = new Intent(Action.REMOTE_PLAY);
        PendingIntent pauseIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.play, pauseIntent);
        //下一首
        PendingIntent nextIntent = PendingIntent.getBroadcast(this, 1, new Intent(Action.REMOTE_NEXT), PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.next, nextIntent);
        //上一首
        PendingIntent lastIntent = PendingIntent.getBroadcast(this, 2, new Intent(Action.REMOTE_PREVIOUS), PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.last, lastIntent);
        //清除
        PendingIntent clearIntent = PendingIntent.getBroadcast(this, 3, new Intent(Action.REMOTE_CLEAR), PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.cancel, clearIntent);
        //进入activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 4, new Intent(this, PlayingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteView.setOnClickPendingIntent(R.id.controller, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void setMusic(Music music) {
        mPlayer.setMusic(music);
    }

    public void setMusicList(ArrayList<Music> musicList) {
        mPlayer.setMusicList(musicList);
    }

    public void play() {
        mPlayer.play();
        refreshRemoteView();
    }

    public void playNext() {
        mPlayer.playNext();
        refreshRemoteView();
    }

    public void playPrevious() {
        mPlayer.playPrevious();
        refreshRemoteView();
    }

    public void playIndex(int index) {
        mPlayer.playIndex(index);
        refreshRemoteView();
    }

    public void pause() {
        mPlayer.pause();
        refreshRemoteView();
    }

    public void resume() {
        mPlayer.resume();
        refreshRemoteView();
    }

    public void stop() {
        mPlayer.stop();
    }

    public Music getMusic() {
        return mPlayer.getMusic();
    }
    public ArrayList<Music> getMusicList(){
        return mPlayer.getMusicList();
    }

    /**
     * 刷新远程view的正确状态
     */
    private void refreshRemoteView() {
        launchRemoteView();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("destroy--", "des");
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        stopForeground(true);
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    @Override
    public void onCompletion() {
        playNext();
        notifyPlayingIndexChanged();
    }

    private void notifyPlayingIndexChanged() {
        Intent intent = new Intent();
        intent.setAction(Action.PLAY_NEXT);
        sendBroadcast(intent);
    }

    public class MyBinder extends Binder {

        public void setMusic(Music music) {
            PlayService.this.setMusic(music);
        }

        public void setMusicList(ArrayList<Music> musicList) {
            PlayService.this.setMusicList(musicList);
        }

        public void play() {
            PlayService.this.play();
        }

        public void playNext() {
            PlayService.this.playNext();
        }

        public void playPrevious() {
            PlayService.this.playPrevious();
        }

        public void playIndex(int index) {
            PlayService.this.playIndex(index);
        }

        public void pause() {
            PlayService.this.pause();
        }

        public void resume() {
            PlayService.this.resume();
        }

        public void stop() {
            PlayService.this.stop();
        }

        public Music getMusic() {
            return PlayService.this.getMusic();
        }
        public ArrayList<Music> getMusicList(){
            return PlayService.this.getMusicList();
        }
    }

    class PlayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;
            if (action.equals(Action.REMOTE_CLEAR)) {
                if (getMusic() != null){
                    stop();
                    stopForeground(true);
                    sendBroadcast(new Intent(Action.REMOTE_STOP));
                    return;
                }
            } else if (action.equals(Action.REMOTE_PREVIOUS)) {
                playPrevious();
                notifyPlayingIndexChanged();
            } else if (action.equals(Action.REMOTE_NEXT)) {
                playNext();
                notifyPlayingIndexChanged();
            } else if (action.equals(Action.REMOTE_PLAY)) {
                if (getMusic() == null) {
                    playIndex(0);
                    notifyPlayingIndexChanged();
                } else {
                    if (getMusic().getState() == Music.MusicState.STATE_PLAYING) {
                        pause();
                    } else if (getMusic().getState()== Music.MusicState.STATE_PAUSE){
                        resume();
                    }else {
                        play();
                    }
                }
            }
            refreshRemoteView();
        }
    }
}
