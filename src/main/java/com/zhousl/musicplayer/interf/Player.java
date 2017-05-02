package com.zhousl.musicplayer.interf;

import com.zhousl.musicplayer.Music;

/**
 * Created by shunli on 2017/4/27.
 */

public interface Player {
    //开始从头播放一首音乐
    void play(Music music);

    //暂停音乐
    void pause(Music music);

    //停止播放音乐
    void stop(Music music);

    //暂停后恢复播放，从原来的位置接着播放
    void resume(Music music);

    //从某个位置开始播放音乐
    void seekTo(long curPos);

    boolean isPlaying();

    //    boolean isLooping();
    interface OnCompleteListener {
        void onCompletion();
    }
    interface OnErrorListener{
        boolean onError();
    }
    interface OnSeekCompleteListener{
        void onSeekComplete();
    }
}
