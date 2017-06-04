package com.zhousl.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhousl.musicplayer.constants.Action;

/**
 * Created by shunli on 2017/5/2.
 */

public class PlayingChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"next",Toast.LENGTH_LONG).show();
        String action = intent.getAction();
        if (action!=null&&action.equals(Action.PLAY_NEXT)){
            Toast.makeText(context,"next",Toast.LENGTH_LONG).show();
        }
    }
}
