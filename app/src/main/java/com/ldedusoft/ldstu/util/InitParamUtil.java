package com.ldedusoft.ldstu.util;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wangjianwei on 2016/6/27.
 */
public class InitParamUtil {

    private SharedPreferences pref; //保存文件
    private Context mContent;
    public InitParamUtil(Context context){
     mContent = context;
    }

    public static InitParamUtil getInstance(Context context){
        return new InitParamUtil(context);
    }




}
