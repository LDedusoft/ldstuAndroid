package com.ldedusoft.ldstu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.ldedusoft.ldstu.model.UserProperty;
import com.ldedusoft.ldstu.util.ActivityCollector;

import java.util.ArrayList;

/**
 * Created by wangjianwei on 2016/6/22.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getClass().getName().indexOf("WelcomeViewActivity")==-1&&this.getClass().getName().indexOf("LoginActivity")==-1){
           try {
               if(UserProperty.getInstance().getUserName()==null){
                   Log.d("BaseActivity","进程被销毁，重新登录");
                   Intent intent = new Intent("activity.LoginActivity");
                   startActivity(intent);
               }
           }catch (Exception e){
               Intent intent = new Intent("activity.LoginActivity");
               startActivity(intent);
           }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityCollector.addActivity(this);
    }




    protected String TAG = getClass().getSimpleName();

    private static ArrayList<Activity> mActivityList = new ArrayList<Activity>();



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityList.remove(this);
    }

    /**
     * @param view
     *            标题栏左侧按钮点击事件
     */
    public void goBack(View view) {
        onBackPressed();
    }

    /**
     * @param view
     *            标题栏右侧按钮点击事件
     */
    public void goNext(View view) {
        showShortToast("自定义跳转页面");
    }

    /**
     * @return 返回栈顶Activity
     */
    public static Activity GetTopActivity() {
        if (mActivityList.size() <= 0)
            return null;
        return mActivityList.get(mActivityList.size() - 1);
    }

    /**
     * @param str
     *            显示短时间toast信息
     */
    protected void showShortToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param str
     *            显示长时间toast信息
     */
    protected void showLongToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

}
