package com.ldedusoft.ldstu.Application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by wangjianwei on 2016/7/16.
 */
public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {
    private Context myContext;
    private static MyApplication myApplication;


    public static MyApplication getInstance(){
        if(myApplication==null){
            myApplication = new MyApplication();
        }
        return myApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setContext(Context context){
        this.myContext = context;
    }

    public  String getStr(int id){
        return myContext.getResources().getString(id);
    }

    public String[] getArray(int id){
        return myContext.getResources().getStringArray(id);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d("MyApplication","FC异常！");
        thread.setDefaultUncaughtExceptionHandler(this);
    }
}
