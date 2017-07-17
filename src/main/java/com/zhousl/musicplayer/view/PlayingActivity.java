package com.zhousl.musicplayer.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhousl.musicplayer.BaseActivity;
import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.MusicPlayer;
import com.zhousl.musicplayer.R;
import com.zhousl.musicplayer.interf.Player;
import com.zhousl.musicplayer.util.TimeUtil;
import com.zhousl.musicplayer.util.UIUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by shunli on 2017/5/1.
 */

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageView album;
    private Toolbar toolbar;
    private TextView artist;
    private TextView lrc;
    private TextView timePlayed;
    private TextView timeTotal;
    private AppCompatSeekBar progress;
    private Player.State[] states = {Player.State.STATE_LOOP_ALL, Player.State.STATE_LOOP_ONE, Player.State.STATE_LOOP_ORDER, Player.State.STATE_LOOP_SHUFFLE};
    private Player.State curState;
    private AppCompatImageView playPause;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        init();
        attachViewState();
    }

    private void attachViewState() {
        MusicPlayer player = MusicPlayer.getPlayer();
        if (player == null || player.getMusic() == null || player.getMusicList() == null)
            return;
        if (curState == null) {
            curState = states[0];
        }
        toolbar.setTitle(player.getMusic().getName());
        toolbar.setSubtitle(player.getMusic().getArtist());
        ((RelativeLayout.LayoutParams) toolbar.getLayoutParams()).topMargin= UIUtil.getStatusBarHeight(this);
        toolbar.requestLayout();
        if (player.getMusic().getState() == Music.MusicState.STATE_PLAYING) {
            playPause.setImageResource(R.mipmap.m_play);
        } else {
            playPause.setImageResource(R.mipmap.m_pause);
        }
        artist.setText(player.getMusic().getArtist());
        timePlayed.setText(TimeUtil.getFormattedTimeStr(player.getMusic().getCurPosition()));
        timeTotal.setText(TimeUtil.getFormattedTimeStr(player.getMusic().getDuration()));
        progress.setMax((int) player.getMusic().getDuration());
        progress.setProgress((int) player.getMusic().getCurPosition());
        progress.setOnSeekBarChangeListener(this);
    }

    private void init() {
        album = (ImageView) findViewById(R.id.album);
        artist = (TextView) findViewById(R.id.artist);
        lrc = (TextView) findViewById(R.id.lrc);
        AppCompatImageView playBackground = (AppCompatImageView) findViewById(R.id.play_background);
        Glide.with(this)
                .load(R.drawable.play_bg)
                .centerCrop()
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this,30))
                .into(playBackground);
        findViewById(R.id.play_state).setOnClickListener(this);
        findViewById(R.id.play_previous).setOnClickListener(this);
        playPause = (AppCompatImageView) findViewById(R.id.play_pause);
        playPause.setOnClickListener(this);
        findViewById(R.id.play_next).setOnClickListener(this);
        timePlayed = (TextView) findViewById(R.id.time_played);
        timeTotal = (TextView) findViewById(R.id.time_total);
        progress = (AppCompatSeekBar) findViewById(R.id.play_progress);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.play_bg));
//        roundedBitmapDrawable.setCircular(true);
//        album.setImageDrawable(roundedBitmapDrawable);
        Glide.with(this)
                .load(R.drawable.play_bg)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(album);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_state:
                break;
            case R.id.play_previous:
                break;
            case R.id.play_pause:
                break;
            case R.id.play_next:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
