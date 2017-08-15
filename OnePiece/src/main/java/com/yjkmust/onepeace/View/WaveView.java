package com.yjkmust.onepeace.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by 11432 on 2017/8/13.
 */

public class WaveView extends View {
    private Path mAbovePath,mBelowPath;
    private Paint mAbovePaint,mBelowPaint;
    private DrawFilter drawFilter;
    private float φ;
    private onWaveAnimationListener listener;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化路径
        mAbovePath = new Path();
        mBelowPath = new Path();
        //初始化画笔
        mAbovePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAbovePaint.setAntiAlias(true);//设置抗锯齿
        mAbovePaint.setStyle(Paint.Style.FILL);
        mAbovePaint.setColor(Color.WHITE);

        mBelowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBelowPaint.setAntiAlias(true);//设置抗锯齿
        mBelowPaint.setStyle(Paint.Style.FILL);
        mBelowPaint.setColor(Color.WHITE);
        mBelowPaint.setAlpha(80);

        //画布抗锯齿
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(drawFilter);
        mAbovePath.reset();
        mBelowPath.reset();

        φ-=0.1f;
        float y,y2;
        double w = 2*Math.PI/getWidth();
        mAbovePath.moveTo(getLeft(),getBottom());
        mBelowPath.moveTo(getLeft(),getBottom());
        for (float x  = 0; x <= getWidth() ; x+=20){
            /**
             *  y=Asin(ωx+φ)+k
             *  A—振幅越大，波形在y轴上最大与最小值的差值越大
             *  ω—角速度， 控制正弦周期(单位角度内震动的次数)
             *  φ—初相，反映在坐标系上则为图像的左右移动。这里通过不断改变φ,达到波浪移动效果
             *  k—偏距，反映在坐标系上则为图像的上移或下移。
             */
            y = (float) (8 * Math.cos(w * x + φ) + 8);
            y2 = (float) (8 * Math.sin(w * x + φ));
            mAbovePath.lineTo(x, y);
            mBelowPath.lineTo(x, y2);
            //回调 把y值传出去 使图片随着一起摇摆
            listener.onWaveAnimation(y);
        }
        mAbovePath.lineTo(getRight(), getBottom());
        mBelowPath.lineTo(getRight(), getBottom());

        canvas.drawPath(mAbovePath, mAbovePaint);
        canvas.drawPath(mBelowPath, mBelowPaint);
        //20ms重绘一次
        postInvalidateDelayed(20);


    }

    public void setOnWaveAnimationListener(onWaveAnimationListener listener){
        this.listener = listener;
    }
    public interface onWaveAnimationListener{
        void onWaveAnimation(float y);
    }
}
