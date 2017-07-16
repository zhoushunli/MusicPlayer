package com.zhousl.musicplayer.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhousl.musicplayer.R;

import java.util.Random;

/**
 * Created by shunli on 2017/4/29.
 */

public class PlayingView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Random random = new Random();

    public PlayingView(Context context) {
        super(context);
        init();
    }

    public PlayingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.themeColor2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimen(widthMeasureSpec);
        int height = measureDimen(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }
    private int measureDimen(int measureSpec){
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result;
        if (mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result=100;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        random.setSeed(mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
