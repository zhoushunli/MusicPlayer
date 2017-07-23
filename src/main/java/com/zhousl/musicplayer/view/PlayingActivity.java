package com.zhousl.musicplayer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhousl.customui.CircularProgressBar;
import com.zhousl.musicplayer.Music;
import com.zhousl.musicplayer.MusicHelper;
import com.zhousl.musicplayer.MusicPlayer;
import com.zhousl.musicplayer.R;
import com.zhousl.musicplayer.constants.Action;
import com.zhousl.musicplayer.interf.Player;
import com.zhousl.musicplayer.util.TimeUtil;
import com.zhousl.musicplayer.util.UIUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by shunli on 2017/5/1.
 */

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Player.onPlayStateChangedListener {

    private ImageView album;
    private Toolbar toolbar;
    private TextView artist;
    private TextView lrc;
    private TextView timePlayed;
    private TextView timeTotal;
    private AppCompatSeekBar progress;
    private Player.State[] states = {Player.State.STATE_LOOP_ALL, Player.State.STATE_LOOP_ONE, Player.State.STATE_LOOP_ORDER, Player.State.STATE_LOOP_SHUFFLE};
    private Player.State curState;
    private int stateIndex;
    private AppCompatImageView playPause;
    private MusicPlayer player;
    private Handler mHandler;
    private long REFRESH_DELAY = 1000;
    private RemoteReceiver remoteReceiver;
    private AppCompatImageView playBackground;
    private CircularProgressBar circleProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        player = MusicPlayer.getPlayer();
        player.addOnPlayStateChangedListener(this);
        mHandler = new Handler(Looper.getMainLooper());
        init();
        initReceiver();
        refreshViewState();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Action.REMOTE_STOP);
        remoteReceiver = new RemoteReceiver();
        registerReceiver(remoteReceiver, filter);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            refreshProgress();
            mHandler.removeCallbacks(this);
            Log.i("--------", "uuuuuuuu");
            mHandler.postDelayed(this, REFRESH_DELAY);
        }
    };

    private void refreshViewState() {
        if (player == null || player.getMusic() == null || player.getMusicList() == null)
            return;
        curState = states[stateIndex];
        toolbar.setTitle(player.getMusic().getName());
        toolbar.setSubtitle(player.getMusic().getArtist());
        playPause.setSelected(player.getMusic().getState() == Music.MusicState.STATE_PLAYING);
        artist.setText(player.getMusic().getArtist());
        lrc.setText("");
        if (player.getMusic().getState() == Music.MusicState.STATE_PLAYING) {
            mHandler.post(r);
        } else if (player.getMusic().getState() == Music.MusicState.STATE_PAUSE) {
            refreshProgress();
        }
    }

    private void refreshProgress() {
        progress.setMax((int) player.getMusic().getDuration());
        progress.setProgress(player.getCurPlayPosition());
        circleProgress.setMax((int) player.getMusic().getDuration());
        circleProgress.setProgress(player.getCurPlayPosition());

        timePlayed.setText(TimeUtil.getFormattedTimeStr(player.getCurPlayPosition()));
        timeTotal.setText(TimeUtil.getFormattedTimeStr(player.getMusic().getDuration()));
    }

    private void init() {
        album = (ImageView) findViewById(R.id.album);
        artist = (TextView) findViewById(R.id.artist);
        lrc = (TextView) findViewById(R.id.lrc);
        circleProgress = (CircularProgressBar) findViewById(R.id.circle_progress);
        playBackground = (AppCompatImageView) findViewById(R.id.play_background);
        refreshArtworks();
        findViewById(R.id.play_state).setOnClickListener(this);
        findViewById(R.id.play_previous).setOnClickListener(this);
        findViewById(R.id.play_list).setOnClickListener(this);
        playPause = (AppCompatImageView) findViewById(R.id.play_pause);
        playPause.setOnClickListener(this);
        findViewById(R.id.play_next).setOnClickListener(this);
        timePlayed = (TextView) findViewById(R.id.time_played);
        timeTotal = (TextView) findViewById(R.id.time_total);
        progress = (AppCompatSeekBar) findViewById(R.id.play_progress);
        progress.setOnSeekBarChangeListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((RelativeLayout.LayoutParams) toolbar.getLayoutParams()).topMargin = UIUtil.getStatusBarHeight(this);
        toolbar.requestLayout();
    }

    private void refreshArtworks() {
        if (player != null && player.getMusic() != null) {
            byte[] thumb = MusicHelper.getThumb(player.getMusic().getFilePath());
            if (thumb != null) {
                Glide.with(this)
                        .load(thumb)
                        .centerCrop()
                        .crossFade(500)
                        .bitmapTransform(new BlurTransformation(this, 80))
                        .into(playBackground);
                Glide.with(this)
                        .load(thumb)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(album);
            } else {
                Glide.with(this)
                        .load(R.drawable.play_bg)
                        .centerCrop()
                        .crossFade(500)
                        .bitmapTransform(new BlurTransformation(this, 80))
                        .into(playBackground);
                Glide.with(this)
                        .load(R.drawable.play_bg)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(album);
            }
        } else {
            Glide.with(this)
                    .load(R.drawable.play_bg)
                    .centerCrop()
                    .crossFade(500)
                    .bitmapTransform(new BlurTransformation(this, 80))
                    .into(playBackground);
            Glide.with(this)
                    .load(R.drawable.play_bg)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(album);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_state:
                stateIndex++;
                if (stateIndex >= states.length)
                    stateIndex = 0;
                break;
            case R.id.play_previous:
                player.playPrevious();
                refreshViewState();
                break;
            case R.id.play_pause:
                doPlayOrPause();
                refreshViewState();
                break;
            case R.id.play_next:
                player.playNext();
                refreshViewState();
                break;
            case R.id.play_list:
                break;
        }
    }

    private void doPlayOrPause() {
        Music music = player.getMusic();
        if (music == null)
            return;
        if (music.getState() == Music.MusicState.STATE_PAUSE) {
            player.resume();
        } else if (music.getState() == Music.MusicState.STATE_PLAYING) {
            player.pause();
        } else {
            if (music == null) {
                if (player.getMusicList() == null || player.getMusicList().size() == 0)
                    return;
                player.playIndex(0);
            } else {
                player.play();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.removeOnPlayStateChangeListener(this);
        if (r != null)
            mHandler.removeCallbacks(r);
        if (remoteReceiver != null)
            unregisterReceiver(remoteReceiver);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        player.seekTo((long) (progress * 1.0f / seekBar.getMax() * player.getMusic().getDuration()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        player.seekTo((long) (seekBar.getProgress() * 1.0f / seekBar.getMax() * player.getMusic().getDuration()));
    }

    @Override
    public void onMusicPause(Music music) {
        if (r != null)
            mHandler.removeCallbacks(r);
    }

    @Override
    public void onMusicPlay(Music music) {
        refreshViewState();
        refreshArtworks();
    }

    @Override
    public void onMusicResume(Music music) {
        mHandler.post(r);
    }

    class RemoteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null || intent.getAction().equals(Action.REMOTE_STOP)) {
                Music music = player.getMusic();
                if (music == null)
                    return;
                playPause.setSelected(music.getState() == Music.MusicState.STATE_PLAYING);
            }
        }
    }
}
