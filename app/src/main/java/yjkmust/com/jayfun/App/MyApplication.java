package yjkmust.com.jayfun.App;

import android.app.Application;
import android.content.Context;

/**
 * Created by GEOFLY on 2017/7/7.
 */

public class MyApplication extends Application {
    private static Context context ;
    public static MyApplication application;
    public static Context getContext(){
        return context;
    }
    public static MyApplication getInstance(){
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
    }
}
