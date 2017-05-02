package com.zhousl.musicplayer.util;

import com.zhousl.musicplayer.Music;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/28.
 */

public class FileUtil {

    private static ArrayList<File> files = new ArrayList<>();
    private static ArrayList<File> musicFiles=new ArrayList<>();

    private static ArrayList<File> getFile(String filePath) {
        if (filePath == null || filePath.trim() == null)
            return null;
        File file = new File(filePath);
        if (!file.exists())
            return null;
        if (file.isFile()) {
            files.add(file);
        } else {
            File[] fi = file.listFiles();
            for (File f : fi) {
                getFile(f.getPath());
            }
        }
        return files;
    }

//    public static ArrayList<Music> getMusicList(String path){
//        if (musicFiles.size()>0)
//            musicFiles.clear();
//        getMusicFiles(path);
//        return convertToMusic(musicFiles);
//    }
//    private static ArrayList<Music> convertToMusic(ArrayList<File> files){
//        if (files.size()==0)
//            return null;
//        for (File file : files) {
//            Music music = new Music();
//        }
//    }

    private static ArrayList<File> getMusicFiles(String filePath) {
        if (filePath == null || filePath.trim() == null)
            return null;
        File file = new File(filePath);
        if (!file.exists())
            return null;
        if (file.isDirectory()) {
            File[] files = file.listFiles(filter);
            for (File fi : files) {
                if (fi.isFile()){
                    musicFiles.add(fi);
                }else {
                    getMusicFiles(fi.getAbsolutePath());
                }
            }
        }
        return musicFiles;
    }
    private static FileFilter filter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()){
                return true;
            }
            else
                return pathname.getAbsolutePath().endsWith(".mp3");
        }
    };
}
