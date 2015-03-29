package com.shadev.easyloadingdemo.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.shadev.easyloading.R;

public class LoadingButton extends View{

    /** 圆环背景的画笔 **/
    private Paint mCirclePaint;
    /** 圆环进度条的画笔 **/
    private Paint mArcPaint;
    /** 圆形实心背景的画笔 **/
    private Paint mFillCirclePaint;
    /** View的宽度 **/
    private int width;
    /** View的高度，这里View应该是正方形，所以宽高是一样的 **/
    private int height;
    /** View的中心坐标x **/
    private int centerX;
    /** View的中心坐标y **/
    private int centerY;
    /** 是否显示旋转的圆环 **/
    private boolean isShowArc = false;
    /** 是否为首次绘制 **/
    private boolean isDefaultDraw = true;
    private int drawableWidth;
    /** 是否在绘制completeDrawable **/
    private boolean isDrawableStart = false;
    /** 是否处于完成状态 **/
    private boolean isCompleted = false;
    /** 用于展示icon **/
    private Drawable mCommonDrawable;
    /** 完成时的Drawable **/
    private Drawable mCompleteDrawable;
    /** 展示icon的默认宽度 **/
    private int defaultCommonDrawableWidth;

    public LoadingButton(Context context) {
        this(context, null, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(0x1FFFFFFF);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setAntiAlias(true);

        mArcPaint = new Paint();
        mArcPaint.setColor(0xFF4BB390);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(10);
        mArcPaint.setAntiAlias(true);

        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFF4BB390);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(10);
        mFillCirclePaint.setAntiAlias(true);

        mCommonDrawable = getResources().getDrawable(R.drawable.download_text);
        mCompleteDrawable = getResources().getDrawable(R.drawable.complete);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCompleted)    return;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setDuration(500);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isShowArc = true;
                        invalidate();
                        ScaleAnimation scaleAnimation = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setFillAfter(true);
                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override public void onAnimationStart(Animation animation) {}
                            @Override public void onAnimationRepeat(Animation animation) {}
                            @Override public void onAnimationEnd(Animation animation) {
                                isRotate = true;
                                invalidate();
                            }

                        });
                        if(isDefaultDraw) LoadingButton.this.startAnimation(scaleAnimation);
                        isDefaultDraw = false;

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if(isDefaultDraw) LoadingButton.this.startAnimation(scaleAnimation);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        centerX = width/2;
        centerY = height/2;

        defaultCommonDrawableWidth = (int) (width*0.5);//(int) Math.round(getWidth()/10.0);
        mCommonDrawable.setBounds(0,0,defaultCommonDrawableWidth,defaultCommonDrawableWidth);
        mCompleteDrawable.setBounds(0,0,defaultCommonDrawableWidth,defaultCommonDrawableWidth);
        radius = centerX - 10;
        oval = new RectF(centerX-radius,centerX - radius,centerX+radius,centerX+radius);
    }


    int radius;
    RectF oval;
    int arcStartAngle = -90;
    float currentProgress = 10;
    float targetProgress = currentProgress;
    float totalProgress = 360;
    boolean isRotate = false;
    int arcStartAngleOffset = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean isKeepDraw = false;
        if(isDefaultDraw){
            canvas.drawCircle(centerX, centerY, radius, mFillCirclePaint);
            canvas.translate(centerX-defaultCommonDrawableWidth/2,centerY-defaultCommonDrawableWidth/2);
            mCommonDrawable.draw(canvas);
        }else if(isDrawableStart){
            defaultCommonDrawableWidth = Math.min(defaultCommonDrawableWidth+10,width);
            mCompleteDrawable.setBounds(0,0,defaultCommonDrawableWidth,defaultCommonDrawableWidth);
            canvas.drawCircle(centerX, centerY, Math.min(defaultCommonDrawableWidth,radius), mFillCirclePaint);
            canvas.translate(centerX-defaultCommonDrawableWidth/2,centerX-defaultCommonDrawableWidth/2);
            mCompleteDrawable.draw(canvas);
            if(defaultCommonDrawableWidth>=width*0.5){
                isDrawableStart = false;
                isCompleted = true;
                invokeComplete();
            }
            invalidate();
        } else if (isRotate) {
            isKeepDraw = true;
            if (currentProgress != targetProgress) {
                isKeepDraw = true;
                float addedValue = 2;
                currentProgress = Math.min(currentProgress + addedValue, targetProgress);
                if (currentProgress == totalProgress) {
                    startDrawable();
                }
            }
            canvas.drawCircle(centerX, centerX, radius, mCirclePaint);
            canvas.drawArc(oval, arcStartAngle + getArcStartOffset(), currentProgress / totalProgress * 360, false, mArcPaint);
        }else if (isShowArc) {
            canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
            isShowArc = false;
        }else if(isCompleted){
            canvas.drawCircle(centerX, centerY, Math.min(defaultCommonDrawableWidth,radius), mFillCirclePaint);
            canvas.translate(centerX-defaultCommonDrawableWidth/2,centerX-defaultCommonDrawableWidth/2);
            mCompleteDrawable.draw(canvas);
        }
        if(isKeepDraw)
            invalidate();
    }

    Callback callback;
    public interface  Callback{
        void complete();
    }

    private void startDrawable() {
        defaultCommonDrawableWidth = 0;
        isRotate = false;
        isDrawableStart = true;
        invalidate();
    }

    private void invokeComplete() {
        if(callback!=null){
            callback.complete();
        }
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private int getArcStartOffset() {
        if(arcStartAngleOffset>=360){
            arcStartAngleOffset = 0;
        }else{
            arcStartAngleOffset+=2;
        }
        return arcStartAngleOffset;
    }

    public void setTargetProgress(float targetProgress){
        this.targetProgress = targetProgress;
        invalidate();
    }

}
