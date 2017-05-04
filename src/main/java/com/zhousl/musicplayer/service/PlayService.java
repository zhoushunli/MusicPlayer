package com.zhousl.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.MusicPlayer;
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

    @Override
    public void onCreate() {
        super.onCreate();
        if (mPlayer == null)
            mPlayer = new MusicPlayer();
        ((MusicPlayer) mPlayer).setOnCompleteListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void play(int index) {
        this.mIndex=index;
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
        if (this.musicList.size()>0)
            this.musicList.clear();
        this.musicList.addAll(musicList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            ((MusicPlayer) mPlayer).release();
            mPlayer = null;
        }
    }

    @Override
    public void onCompletion() {
        if (musicList.size() == 0)
            return;
        mIndex++;
        if (mIndex==musicList.size())
            mIndex=0;
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
}
