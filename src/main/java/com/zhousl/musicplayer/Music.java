package com.zhousl.musicplayer;

/**
 * Created by shunli on 2017/4/28.
 */

public class Music {

    private String name;//名称
    private String artist;//艺术家
    private long duration;//时长
    private long curPosition;//当前已播放时长
    private long size;//大小
    private long id;//编号id
    private String filePath;//文件路径
    private String suffix;
    private String album;
    private boolean isPlaying;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public long getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(long curPosition) {
        this.curPosition = curPosition;
    }

    public enum State{
        STATE_PAUSE,//暂停
        STATE_PLAYING,//播放
        STATE_IDLE//闲置
    }
}
