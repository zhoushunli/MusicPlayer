package com.zhousl.musicplayer.interf;

import com.zhousl.musicplayer.Music;

/**
 * Created by shunli on 2017/4/27.
 */

public interface Player {
    //开始从头播放一首音乐
    void play();

    //暂停音乐
    void pause();

    //停止播放音乐
    void stop();

    //暂停后恢复播放，从原来的位置接着播放
    void resume();

    //从某个位置开始播放音乐
    void seekTo(long curPos);

    /**
     * 判断是否正在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 返回当前播放模式，为{@link State.STATE_LOOP_ONE,State.STATE_LOOP_ALL,State.STATE_LOOP_NONE,State.STATE_LOOP_ORDER,State.STATE_LOOP_SHUFFLE}
     * 之一
     * @return
     */
    State getLoopState();

    //设置播放模式
    void setLoopState(State state);

    enum State{
        //单曲循环
        STATE_LOOP_ONE,
        //全部循环
        STATE_LOOP_ALL,
        //播完当前就暂停，不循环
        STATE_LOOP_NONE,
        //顺序播放，播完全部就停止，不继续
        STATE_LOOP_ORDER,
        //随机播放
        STATE_LOOP_SHUFFLE
    }

    interface OnCompleteListener {
        void onCompletion();
    }

    interface OnErrorListener {
        boolean onError();
    }

    interface OnSeekCompleteListener {
        void onSeekComplete(long curPosition);
    }
    interface onPlayStateChangedListener{
        void onMusicPause(Music music);
        void onMusicPlay(Music music);
        void onMusicResume(Music music);
    }
}
