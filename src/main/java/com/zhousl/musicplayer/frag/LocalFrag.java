package com.zhousl.musicplayer.frag;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zhousl.musicplayer.MainActivity;
import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.MusicHelper;
import com.zhousl.musicplayer.R;
import com.zhousl.musicplayer.adapter.LocalMusicAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/28.
 */

public class LocalFrag extends FragBase implements AdapterView.OnItemClickListener {

    private ListView mList;
    private LocalMusicAdapter mAdapter;
    private ArrayList<Music> musicList;
    //当前正在播放的音乐
    private Music mMusic;
    private MyTask mQueryTask;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (musicList == null)
            musicList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (musicList != null) {
            mQueryTask = new MyTask();
            mQueryTask.execute();
        }
    }

    @Override
    protected int getRes() {
        return R.layout.local_frag;
    }

    @Override
    protected void findView(View rootView) {
        mList = (ListView) rootView.findViewById(R.id.list_content);
        mAdapter = new LocalMusicAdapter(musicList);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Music music = musicList.get(position);
        if (mMusic==null){//完全没有播放过
            doPlayMusic(position);
        }else {//有播放过，但是状态可能是播放状态和暂停状态
            if (music.getId()==mMusic.getId()){
                if (!mMusic.isPlaying()){//点击的是当前正在pause的
                    doResumeMusic(music);
                }else {//点击的是当前正在play的
                    openPlayingPane(music);
                }
                return;
            }else {
                if (mMusic != null) {//清除上次播放状态
                    mMusic.setPlaying(false);
                }
                doPlayMusic(position);
            }
        }
        int childCount = mList.getChildCount();//设置当前点击的条目为选中状态，
        for (int i = 0; i < childCount; i++) { // 其余的均设置为非选中状态
            View child = mList.getChildAt(i);
            if (child == view) {
                child.findViewById(R.id.music_title).setSelected(true);
                child.findViewById(R.id.artist).setSelected(true);
            } else {
                child.findViewById(R.id.music_title).setSelected(false);
                child.findViewById(R.id.artist).setSelected(false);
            }
        }
        mMusic = music;
    }

    public void doPlayMusic(int index) {
        mMusic=musicList.get(index);
        ((MainActivity) getActivity()).getBinder().play(index);
        ((MainActivity) getActivity()).onPlayStart(index,musicList.get(index));
    }
    public void doResumeMusic(Music music){
        ((MainActivity) getActivity()).getBinder().resume(music);
        ((MainActivity) getActivity()).onPlayStart(musicList.indexOf(music),music);
    }

    /**
     * 如果点击了正在播放的音乐，则打开播放界面
     *
     * @param music
     */
    private void openPlayingPane(Music music) {

    }

    public void notifyPlayingChanged() {
        int selectedItemPosition = mList.getSelectedItemPosition();
        mAdapter.notifyDataSetChanged();
        mList.setSelection(selectedItemPosition);
    }

    public ArrayList<Music> getMusicList() {
        return musicList;
    }

    public Music getMusic() {//返回当前播放的music
        return mMusic;
    }

    class MyTask extends AsyncTask<Void, File, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            ArrayList<Music> list = MusicHelper.getInstance().getMusic(getActivity());
            if (list != null) {
                musicList.addAll(list);
                return 1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                FragmentActivity activity = getActivity();
                if (activity != null && activity instanceof MainActivity) {
                    MainActivity act = (MainActivity) activity;
                    mMusic = act.getMusic();
                    act.getBinder().setMusicList(musicList);
                    if (mMusic != null)
                        checkForPlaying();//mMusic不为null，可能存在两种状态，即播放状态和暂停状态，需要判断
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        private void checkForPlaying() {
            for (Music music : musicList) {
                if (music.getId() == mMusic.getId()) {
                    if (mMusic.isPlaying()){
                        //处于播放状态
                        music.setPlaying(true);
                    }else {
                        //处于暂停状态
                        music.setPlaying(false);
                    }
                    music.setCurPosition(mMusic.getCurPosition());
                    mMusic = music;
                    ((MainActivity) getActivity()).onPlayStart(musicList.indexOf(mMusic),mMusic);
                    return;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mQueryTask != null && !mQueryTask.isCancelled())
            mQueryTask.cancel(true);
    }
}
