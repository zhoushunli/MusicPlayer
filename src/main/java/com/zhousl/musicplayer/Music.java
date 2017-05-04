package com.zhousl.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shunli on 2017/4/28.
 */

public class Music implements Parcelable{

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

    protected Music(Parcel in) {
        name = in.readString();
        artist = in.readString();
        duration = in.readLong();
        curPosition = in.readLong();
        size = in.readLong();
        id = in.readLong();
        filePath = in.readString();
        suffix = in.readString();
        album = in.readString();
        isPlaying = in.readByte() != 0;
    }
    public Music(){}

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeLong(curPosition);
        dest.writeLong(size);
        dest.writeLong(id);
        dest.writeString(filePath);
        dest.writeString(suffix);
        dest.writeString(album);
        dest.writeByte((byte) (isPlaying ? 1 : 0));
    }

    public enum State{
        STATE_PAUSE,//暂停
        STATE_PLAYING,//播放
        STATE_IDLE//闲置
    }
}
