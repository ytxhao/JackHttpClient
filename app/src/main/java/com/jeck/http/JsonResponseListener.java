package com.jeck.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeck.http.interfaces.IDataListener;
import com.jeck.http.interfaces.IHttpEntity;
import com.jeck.http.interfaces.IHttpListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * M  对应响应类
 */

public class JsonResponseListener<M> implements IHttpListener {
    private Class<M> response;
    /**
     * 回调调用层 的接口
     */
    private IDataListener<M> dataListener;

    Handler handler = new Handler(Looper.getMainLooper());

    public JsonResponseListener(Class<M> response, IDataListener<M> dataListener) {
        this.response = response;
        this.dataListener = dataListener;
    }

    @Override
    public void onSuccess(IHttpEntity httpEntity) {
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            String content = getContent(inputStream);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            final M m = gson.fromJson(content, response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dataListener.onSuccess(m);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFail() {
        dataListener.onError();
    }

    @Override
    public void addHttpHeader(Map<String, String> headerMap) {

    }

    private String getContent(InputStream inputStream) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                dataListener.onError();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    dataListener.onError();
                }
            }
            content = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            dataListener.onError();
        }
        return content;
    }
}
