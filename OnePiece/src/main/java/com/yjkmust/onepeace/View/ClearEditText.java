package com.yjkmust.onepeace.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.yjkmust.onepeace.R;


/**
 * Created by 11432 on 2017/8/12.
 */

public class ClearEditText extends EditText {
    private static final float DEFAUT_SCALE = 0.5f;
    private float scale;
    private Bitmap bitmap;
    private Paint mPaint;
    private Boolean showClose;
    private float padding;
    private int mWidth;
    private int mHeight;

    public ClearEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int clearIcon = 0;
        if (attrs != null) {
            //获得这个控件对应的属性值
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClearEditText);
            try {
                //获得属性值
                clearIcon = typedArray.getResourceId(R.styleable.ClearEditText_clearIcon, 0);
                scale = typedArray.getResourceId(R.styleable.ClearEditText_scaleSize, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
        //设置删除图标
        if (clearIcon != 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), clearIcon);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.delete);
        }
        if (scale == 0) {
            scale = DEFAUT_SCALE;
        }
        mPaint = new Paint();
        showClose = false;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showClose = !TextUtils.isEmpty(charSequence);
                invalidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //处理点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * ACTION_UP点击离开屏幕
         * getX（）是点击点相对于View的x坐标值
         * getWidth（）是View的宽度值
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (showClose && event.getX() > (getWidth() - getHeight() + padding) && event.getX() < (getWidth() - padding)
                    && event.getY() > padding
                    && event.getY() < (getHeight() - padding)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        /**
         * EXACTLY 精确尺寸xml中的width或者height=？dp
         * AT_MOST xml中的wrap_content
         */
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else if (widthMode == MeasureSpec.AT_MOST){
            width = Math.min(desiredWidth, widthSize);
        }else {
            width = desiredWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else if (heightMode == MeasureSpec.AT_MOST){
            height = Math.min(desiredHeight, heightSize);
        }else {
            height = desiredHeight;
        }
        mWidth = width;
        mHeight = height;
        //计算偏移量
        padding = ((float) mHeight)* (1-scale)/2;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showClose){
            Rect rect = new Rect(0,0,mHeight,mHeight);
            RectF rectF = new RectF(mWidth - mHeight + padding, padding, mWidth - padding, mHeight - padding);
            canvas.drawBitmap(bitmap,rect,rectF,mPaint);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
