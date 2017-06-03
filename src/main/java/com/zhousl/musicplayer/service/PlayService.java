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

    private Player mPlayer;
    private ArrayList<Music> musicList = new ArrayList<>();
    private Music mMusic;
    private int mIndex;
    public static final int REMOTE_VIEW_ID = 0x111;
    private RemoteViews mRemoteView;
    private PlayReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mPlayer == null)
            mPlayer = new MusicPlayer();
        ((MusicPlayer) mPlayer).setOnCompleteListener(this);
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
        manager.notify(0,notification);
//        startForeground(REMOTE_VIEW_ID, notification);
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

    public void play(int index) {
        if (index<0){
            index=0;
        }
        if (index>musicList.size()-1){
            index=musicList.size()-1;
        }
        this.mIndex = index;
        this.mMusic = musicList.get(index);
        mPlayer.play(mMusic);
    }

    public void pause(Music music) {
        mPlayer.pause(music);
    }

    public Music getMusic() {
        return mMusic;
    }

    public void resume(Music music) {
        mPlayer.resume(music);
    }

    public void stop(Music music) {
        mPlayer.stop(music);
    }

    public void setMusicList(ArrayList<Music> musicList) {
        if (this.musicList.size() > 0)
            this.musicList.clear();
        this.musicList.addAll(musicList);
        refreshRemoteView();
    }

    /**
     * 将当前播放的音乐对象映射到新列表中对应的对象
     *
     * @return
     */
    private Music getMusicNewPosition() {
        if (mMusic == null || musicList.size() == 0)
            return null;
        for (Music music : musicList) {
            if (music.getId() == mMusic.getId())
                return music;
        }
        return null;
    }

    /**
     * 刷新远程view的正确状态
     */
    private void refreshRemoteView() {
        if (mMusic == null){
            mMusic = musicList.get(0);
            mIndex=0;
        }
        mRemoteView.setTextViewText(R.id.music_title, mMusic.getName());
        mRemoteView.setTextViewText(R.id.artist, mMusic.getArtist());
        if (mMusic.isPlaying()) {
            mRemoteView.setImageViewResource(R.id.play, R.mipmap.b_play);
        } else {
            mRemoteView.setImageViewResource(R.id.play, R.mipmap.b_pause);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            ((MusicPlayer) mPlayer).release();
            mPlayer = null;
        }
//        stopForeground(true);
        if (mReceiver!=null)
            unregisterReceiver(mReceiver);
    }

    @Override
    public void onCompletion() {
        if (musicList.size() == 0)
            return;
        mIndex++;
        if (mIndex == musicList.size())
            mIndex = 0;
        play(mIndex);
        notifyPlayingIndexChanged();
    }

    private void notifyPlayingIndexChanged() {
        Intent intent = new Intent();
        intent.setAction(Action.PLAY_NEXT);
        intent.putExtra(Action.IntentKey.INDEX, mIndex);
        sendBroadcast(intent);
    }

    public class MyBinder extends Binder {

        public void setMusicList(ArrayList<Music> musicList) {
            PlayService.this.setMusicList(musicList);
        }

        public void play(int index) {
            PlayService.this.play(index);
        }

        public void pause(Music music) {
            PlayService.this.pause(music);
        }

        public void resume(Music music) {
            PlayService.this.resume(music);
        }

        public void stop(Music music) {
            PlayService.this.stop(music);
        }

        public Music getMusic() {
            return PlayService.this.getMusic();
        }

        public int getIndex() {
            return PlayService.this.mIndex;
        }
    }

    class PlayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(PlayService.this,"收到了",Toast.LENGTH_SHORT).show();
            if (intent == null)
                return;
            if (mMusic == null)
                mMusic = musicList.get(0);
            String action = intent.getAction();
            Music newPosition = getMusicNewPosition();
            if (Action.REMOTE_PLAY.equals(action)) {
                if (mMusic.isPlaying()) {       //点击播放时处于播放状态则暂停
                    pause(mMusic);
                    if (newPosition != null)
                        newPosition.setPlaying(false);
                } else {        //点击时处于暂停状态，暂停有两种状态，播放暂停和未播放
                    if (mMusic.getCurPosition() == 0) {     //未播放状态，直接重新播放
                        play(musicList.indexOf(newPosition));
                    } else {        //播放状态，从播放位置开始播放
                        resume(mMusic);
                        if (newPosition != null)
                            newPosition.setPlaying(true);
                    }
                }
            } else if (Action.REMOTE_NEXT.equals(action)) {
                int index = musicList.indexOf(newPosition);
                if (++index >= musicList.size()) {
                    index = 0;
                }
                play(index);
            } else if (Action.REMOTE_PREVIOUS.equals(action)) {
                int index = musicList.indexOf(newPosition);
                if (--index < 0) {
                    index = musicList.size() - 1;
                }
                play(index);
            }
            refreshRemoteView();
        }
    }
}
