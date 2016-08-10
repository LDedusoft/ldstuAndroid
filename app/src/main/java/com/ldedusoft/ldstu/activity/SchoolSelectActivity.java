package com.ldedusoft.ldstu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ldedusoft.ldstu.R;

/**
 * Created by wangjianwei on 2016/8/6.
 */
public class SchoolSelectActivity extends  FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_schoole_select);
    }

    @Override
    public void onBackPressed() {
       finish();
    }
}
