package com.ldedusoft.ldstu.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wangjianwei on 2016/6/22.
 */
public class HttpUtil {

    public  static void sendHttpRequest(final String path,final String xml, final com.ldedusoft.ldstu.util.HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] entity = xml.getBytes("UTF-8");
                    HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    //指定发送的内容类型为xml
                    conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                    conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
                    OutputStream outStream = conn.getOutputStream();
                    outStream.write(entity);
                    if (conn.getResponseCode() == 200) {
                        listener.onFinish(StreamTool.streamToString(conn.getInputStream()));
                    } else {
                        listener.onWarning("网络返回异常:" + conn.getResponseCode());
                    }
                }catch (Exception e){
                    listener.onError(e);
                }
            }
        }).start();
    }
}
