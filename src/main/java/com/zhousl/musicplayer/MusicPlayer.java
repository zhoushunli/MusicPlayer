package com.zhousl.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

import com.zhousl.musicplayer.interf.Player;

import java.io.IOException;

/**
 * Created by shunli on 2017/4/27.
 */

public class MusicPlayer implements Player, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {

    private final MediaPlayer mPlayer;
    private OnErrorListener onErrorListener;
    private OnSeekCompleteListener onSeekCompleteListener;
    private OnCompleteListener onCompleteListener;
    private Music mMusic;

    public MusicPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public Music getMusic() {
        return mMusic;
    }

    @Override
    public void play(Music music) {
        if (music == null)
            return;
        if (mPlayer.isPlaying())
            stop(mMusic);
        try {
            music.setPlaying(true);
            mPlayer.setDataSource(music.getFilePath());
            mPlayer.prepare();
            mPlayer.start();
            mMusic=music;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause(Music music) {
        mPlayer.pause();
        music.setPlaying(false);
        music.setCurPosition(mPlayer.getCurrentPosition());
    }

    @Override
    public void stop(Music music) {
        music.setPlaying(false);
        music.setCurPosition(0);
        mPlayer.stop();
        mPlayer.reset();
    }

    @Override
    public void resume(Music music) {
        if (music == null)
            return;
        music.setPlaying(true);
        long curPosition = music.getCurPosition();
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

    public void release() {
        mPlayer.release();
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
        if (this.onCompleteListener!=null)
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
