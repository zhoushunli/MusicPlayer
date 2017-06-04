package com.zhousl.musicplayer.frag;

import android.content.Context;
import android.content.Intent;
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
import com.zhousl.musicplayer.view.PlayingActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/28.
 */

public class LocalFrag extends FragBase implements AdapterView.OnItemClickListener {

    private ListView mList;
    private LocalMusicAdapter mAdapter;
    private ArrayList<Music> musicList;

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
        if (music.getState() == Music.MusicState.STATE_IDLE) {//闲置状态，直接播放
            doPlayNew(position);
        } else if (music.getState() == Music.MusicState.STATE_PAUSE) {//暂停状态
            doResume();
        } else {//处于正在播放状态
            openPlayingPane();
        }
        refreshSelectedState(view);
    }

    public void doResume() {
        ((MainActivity) getActivity()).doMusicResume();
    }

    /**
     * 如果点击了正在播放的音乐，则打开播放界面
     */
    private void openPlayingPane() {
        startActivity(new Intent(getActivity(), PlayingActivity.class));
    }

    private void doPlayNew(int position) {
        ((MainActivity) getActivity()).doPlayNew(position);
    }

    private void refreshSelectedState(View view) {
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
            if (list != null && list.size() > 0) {
                musicList.addAll(list);
                return 1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {//数据拿到，设置给player的队列
                ((MainActivity) getActivity()).setMusicList(musicList);
                restoreMusicState();
                notifyActivityRefreshViewState();
            } else {
                //TODO 没有拿到数据或者数据为空，这里应当显示空数据页面
            }
            //通知刷新
            mAdapter.notifyDataSetChanged();
        }
    }

    private void restoreMusicState() {
        Music music = ((MainActivity) getActivity()).getMusic();
        if (music==null)
            return;
        for (Music mc : musicList) {
            if (mc.getId()==music.getId()){
                mc.setState(music.getState());
                ((MainActivity) getActivity()).setMusic(mc);
                return;
            }
        }
    }

    private void notifyActivityRefreshViewState(){
        if (!isAlive())
            return;
        MainActivity activity = (MainActivity) getActivity();
        activity.refreshControllerStatus(activity.getMusic());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mQueryTask != null && !mQueryTask.isCancelled())
            mQueryTask.cancel(true);
    }
}
