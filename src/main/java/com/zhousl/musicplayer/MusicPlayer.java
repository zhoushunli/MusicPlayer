package com.zhousl.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

import com.zhousl.musicplayer.interf.Player;
import com.zhousl.musicplayer.util.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shunli on 2017/4/27.
 */

public class MusicPlayer implements Player, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {

    //用于播放的播放器
    private MediaPlayer mPlayer;
    //播放出错监听
    private OnErrorListener onErrorListener;
    //定点播放监听
    private OnSeekCompleteListener onSeekCompleteListener;
    //播放完毕监听
    private OnCompleteListener onCompleteListener;
    //当前播放的音乐
    private Music mMusic;
    //默认播放状态为全部循环
    private State mState = State.STATE_LOOP_ALL;
    //内部维护的播放列队
    private ArrayList<Music> musicList;
    //当前播放的索引
    private int mIndex;
    //用于获得随机索引值
    private Random mRandom;
    public static final String LAST_PLAYED_SONG="last_played_song";

    public MusicPlayer() {
        mPlayer = new MediaPlayer();
        musicList = new ArrayList<>();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnErrorListener(this);
        mRandom = new Random();
    }

    public Music getMusic() {
        return mMusic;
    }

    public void setMusic(Music mMusic) {
        mIndex = getIndexInternal();
        musicReset();
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

    /**
     * 获取内部播放列队
     *
     * @return
     */
    public ArrayList<Music> getMusicList() {
        return musicList;
    }

    /**
     * 设置内部播放列队
     *
     * @param musicList
     */
    public void setMusicList(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    /**
     * 通过索引播放
     *
     * @param index
     */
    public void playIndex(int index) {
        if (index < 0 || index >= musicList.size())
            return;
        mIndex = index;
        musicReset();
        mMusic = musicList.get(mIndex);
        play();
    }

    private int getIndexInternal() {
        if (musicList.size() == 0 || mMusic == null)
            return mIndex;
        for (Music music : musicList) {
            if (mMusic.getId() == music.getId())
                return musicList.indexOf(music);
        }
        return mIndex;
    }

    /**
     * 下一曲
     */
    public void playNext() {
        mIndex++;
        if (mIndex >= musicList.size()) {
            mIndex = 0;
        }
        musicReset();
        mMusic = musicList.get(mIndex);
        play();
    }

    /**
     * 上一曲
     */
    public void playPrevious() {
        mIndex--;
        if (mIndex < 0)
            mIndex = musicList.size() - 1;
        musicReset();
        mMusic = musicList.get(mIndex);
        play();
    }

    /**
     * 随机播放
     */
    public void playRandom() {
        mIndex = mRandom.nextInt(musicList.size() - 1);
        musicReset();
        mMusic = musicList.get(mIndex);
        play();
    }

    private boolean setDataSource() {
        if (mMusic == null)
            throw new NullPointerException("music file cannot be " + mMusic);
        mPlayer.reset();
        try {
            mMusic.setState(Music.MusicState.STATE_PLAYING);
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
        mMusic.setState(Music.MusicState.STATE_PAUSE);
        mMusic.setCurPosition(mPlayer.getCurrentPosition());
    }

    @Override
    public void stop() {
        if (mMusic==null)
            return;
        Preferences.putLong(LAST_PLAYED_SONG,mMusic.getId());
        reset();
    }

    private void reset() {
        mPlayer.stop();
        mPlayer.reset();
        musicReset();
    }

    private void musicReset() {
        if (mMusic == null)
            return;
        mMusic.setState(Music.MusicState.STATE_IDLE);
        mMusic.setCurPosition(0);
    }

    @Override
    public void resume() {
        if (mMusic == null)
            return;
        mMusic.setState(Music.MusicState.STATE_PLAYING);
        long curPosition = mMusic.getCurPosition();
        if (curPosition < 0) {
            curPosition=0;
        }
        mPlayer.seekTo((int) curPosition);
        mPlayer.start();
    }

    @Override
    public void seekTo(long curPos) {
        seekToWithoutPlay(curPos);
        mPlayer.seekTo((int) curPos);
        mPlayer.start();
    }

    public void seekToWithoutPlay(long curPos) {
        mMusic.setCurPosition(curPos);
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
        mMusic.setState(Music.MusicState.STATE_PLAYING);
        mMusic = null;
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
        reset();
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
        if (this.onErrorListener != null) {
            return onErrorListener.onError();
        }
        release();
        return true;
    }
}
