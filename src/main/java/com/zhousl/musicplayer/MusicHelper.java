package com.zhousl.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/28.
 */

public class MusicHelper {

    private static MusicHelper helper;

    private MusicHelper() {
    }
    public static MusicHelper getInstance(){
        if (helper==null)
            helper=new MusicHelper();
        return helper;
    }

    /**
     * 获取音乐文件
     * @param context
     * @return
     */
    public ArrayList<Music> getMusic(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<Music> musicList = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0)
                continue;
            Music music = new Music();
            music.setArtist(artist);
            music.setDuration(duration);
            music.setFilePath(path);
            music.setId(id);
            music.setName(title);
            music.setSize(size);
            music.setSuffix(path.substring(path.lastIndexOf(".")));
            music.setAlbum(album);
            musicList.add(music);
        }
        return musicList;
    }
    public static byte[] getThumb(String path){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        return art;
    }
    //用来扫描所有文件夹
    public ArrayList<Music> scanMusic(){
        return null;
    }
    //用来扫描指定文件夹下音乐文件
    public ArrayList<Music> scanMusic(File rootDir){
        return null;
    }
}
