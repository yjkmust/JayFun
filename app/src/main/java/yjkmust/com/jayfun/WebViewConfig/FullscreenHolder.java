package yjkmust.com.jayfun.WebViewConfig;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by GEOFLY on 2017/7/28.
 */

public class FullscreenHolder extends FrameLayout {
    public FullscreenHolder(@NonNull Context context) {
        super(context);
        setBackgroundColor(context.getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
