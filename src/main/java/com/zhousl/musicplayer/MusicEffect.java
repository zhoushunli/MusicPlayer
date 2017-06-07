package com.zhousl.musicplayer;

import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;

/**
 * Created by inshot-user on 2017/6/7.
 */

public class MusicEffect {

    private static final MusicEffect mEffect = new MusicEffect();
    //均衡器
    private Equalizer mEqualizer;
    //低音炮
    private BassBoost mBassBoost;
    //频谱
    private Virtualizer mVirtualizer;
    //预设音场
    private PresetReverb mPresetReverb;
    //环境混响
    private EnvironmentalReverb mEnvironmentReverb;

    private MusicEffect(){

    }

    public static MusicEffect getEffect() {
        return mEffect;
    }
}
