package com.zhousl.musicplayer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.R;

import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/28.
 */

public class LocalMusicAdapter extends BaseAdapter {

    private ArrayList<Music> musicList;

    public LocalMusicAdapter(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList == null ? 0 : musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.musci_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.music_title);
            holder.artist = (TextView) convertView.findViewById(R.id.artist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Music music = musicList.get(position);
        holder.title.setSelected(music.isPlaying());
        holder.title.setText(music.getName());
        holder.artist.setSelected(music.isPlaying());
        holder.artist.setText(music.getArtist());
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView artist;
    }
}
