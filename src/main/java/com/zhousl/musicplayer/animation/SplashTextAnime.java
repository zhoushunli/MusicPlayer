package com.zhousl.musicplayer.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by shunli on 2017/4/29.
 */

public class SplashTextAnime extends Animation {
    private float mCenterX;
    private float mCenterY;
    private Camera mCamera;

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCenterX=width/2;
        mCenterY=height/2;
        mCamera=new Camera();
        setDuration(1500);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        mCamera.save();
        mCamera.translate(0,0,720-720.0f*interpolatedTime);
        mCamera.rotateY(180.0f*interpolatedTime);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-mCenterX,-mCenterY);
        matrix.postTranslate(mCenterX,mCenterY);
        mCamera.restore();
    }
}
