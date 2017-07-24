package com.zhousl.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shunli on 2017/7/23.
 */

public class ViewHolder {

    private View itemView;
    private SparseArray<View> views;

    public ViewHolder(View itemView){
        if (views==null)
            views=new SparseArray<>();
        this.itemView=itemView;
    }
    public <T extends View> T getView(int viewId){
        if (viewId<=0)
            return null;
        View view = views.get(viewId);
        if (view==null){
            view=itemView.findViewById(viewId);
            if (view!=null)
                views.put(viewId,view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId){
        return getView(viewId);
    }
    public ImageView getImageView(int viewId){
        return getView(viewId);
    }
    public View getItemView(){
        return itemView;
    }
}
