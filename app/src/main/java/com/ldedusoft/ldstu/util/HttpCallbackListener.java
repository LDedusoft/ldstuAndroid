package com.ldedusoft.ldstu.util;

/**
 * Created by wangjianwei on 2016/6/22.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onWarning(String warning);

    void onError(Exception e);

}
