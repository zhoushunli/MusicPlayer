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
    //当前播放index
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

    /**
     * 通过索引来查找到列表中的音乐
     *
     * @param index 需要播放的音乐位于列表中的索引值
     */
    public void play(int index) {
        if (index < 0)
            index = musicList.size() - 1;
        if (index > musicList.size() - 1)
            index = 0;
        this.mMusic = musicList.get(index);
        this.mIndex = index;
        mPlayer.play(mMusic);
    }

    public void pause(int index) {
        mPlayer.pause(musicList.get(index));
    }

    public Music getMusic() {
        return ((MusicPlayer) mPlayer).getMusic();
    }

    public void resume(int index) {
        mPlayer.resume(musicList.get(index));
    }

    public void stop(int index) {
        mPlayer.stop(musicList.get(index));
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
        mIndex += 1;
        if (mIndex >= musicList.size())
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

        public void pause(int index) {
            PlayService.this.pause(index);
        }

        public void resume(int index) {
            PlayService.this.resume(index);
        }

        public void stop(int index) {
            PlayService.this.stop(index);
        }

        public Music getMusic() {
            return PlayService.this.getMusic();
        }

        public int getIndex() {
            return PlayService.this.mIndex;
        }
    }
}
