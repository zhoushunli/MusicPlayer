package com.zhousl.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

import com.zhousl.musicplayer.interf.Player;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/27.
 */

public class MusicPlayer implements Player, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {

    //用于播放的播放器
    private MediaPlayer mPlayer;
    private OnErrorListener onErrorListener;
    private OnSeekCompleteListener onSeekCompleteListener;
    private OnCompleteListener onCompleteListener;
    //当前播放的音乐
    private Music mMusic;
    //默认播放状态为全部循环
    private State mState=State.STATE_LOOP_ALL;
    //内部维护的播放列队
    private ArrayList<Music> musicList;

    public MusicPlayer() {
        mPlayer = new MediaPlayer();
        musicList = new ArrayList<>();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public Music getMusic() {
        return mMusic;
    }

    public void setMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    @Override
    public void play() {
        if (setDataSource()) {
            try {
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                release();
            }
        }
    }

    private boolean setDataSource() {
        if (mMusic == null)
            throw new NullPointerException("music file cannot be "+mMusic);
        try {
            mMusic.setPlaying(true);
            mPlayer.setDataSource(mMusic.getFilePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
        return false;
    }

    @Override
    public void pause() {
        mPlayer.pause();
        mMusic.setPlaying(false);
        mMusic.setCurPosition(mPlayer.getCurrentPosition());
    }

    @Override
    public void stop() {
        mMusic.setPlaying(false);
        mMusic.setCurPosition(0);
        reset();
    }

    private void reset() {
        mPlayer.stop();
        mPlayer.reset();
    }

    @Override
    public void resume() {
        if (mMusic == null)
            return;
        mMusic.setPlaying(true);
        long curPosition = mMusic.getCurPosition();
        if (curPosition > 0) {
            mPlayer.seekTo((int) curPosition);
            mPlayer.start();
        }
    }

    @Override
    public void seekTo(long curPos) {
        mPlayer.seekTo((int) curPos);
        mPlayer.start();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public State getLoopState() {
        return mState;
    }

    @Override
    public void setLoopState(State state) {
        mState = state;
    }

    public void release() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        mMusic.setPlaying(false);
        mMusic=null;
        musicList.clear();
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener) {
        this.onSeekCompleteListener = onSeekCompleteListener;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        mMusic.setPlaying(false);
        if (this.onCompleteListener != null)
            this.onCompleteListener.onCompletion();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (this.onSeekCompleteListener != null) {
            this.onSeekCompleteListener.onSeekComplete();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        if (this.onErrorListener != null) {
            return onErrorListener.onError();
        }
        return true;
    }
}
