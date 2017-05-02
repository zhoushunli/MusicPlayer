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
        if (music.isPlaying()) {
            openPlayingPane(music);
            return;
        }
        doPlayMusic(position);
        if (mMusic != null) {
            mMusic.setPlaying(false);
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
        ((MainActivity) getActivity()).getBinder().play(index);
        ((MainActivity) getActivity()).onPlayStart(index,musicList.get(index));
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
                        checkForPlaying();
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        private void checkForPlaying() {
            for (Music music : musicList) {
                if (music.getId() == mMusic.getId()) {
                    music.setPlaying(true);
                    mMusic = music;
                    ((MainActivity) getActivity()).onPlayStart(musicList.indexOf(music),music);
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
