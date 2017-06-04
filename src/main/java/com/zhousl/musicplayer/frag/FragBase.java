package com.zhousl.musicplayer.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shunli on 2017/4/28.
 */

public abstract class FragBase extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            if (getRes()==0)
                throw new RuntimeException("you must configure content resource !");
            rootView = inflater.inflate(getRes(), container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    protected boolean isAlive(){
        return getActivity()!=null&&isAdded()&&!isRemoving();
    }
    protected abstract int getRes();
    protected abstract void findView(View rootView);
}
