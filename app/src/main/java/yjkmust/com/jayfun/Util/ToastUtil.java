package yjkmust.com.jayfun.Util;

import android.content.Context;
import android.widget.Toast;


import yjkmust.com.jayfun.App.CloudReaderApplication;
import yjkmust.com.jayfun.App.MyApplication;

/**
 * Created by JayFun on 2016/12/14.
 * 单例Toast
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showToast(Context context,String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}
