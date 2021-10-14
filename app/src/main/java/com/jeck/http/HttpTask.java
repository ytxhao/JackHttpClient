package com.jeck.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeck.http.interfaces.IHttpListener;
import com.jeck.http.interfaces.IHttpService;

import java.util.concurrent.FutureTask;


public class HttpTask<T> implements Runnable {
    private IHttpService httpService;
    private FutureTask<String> futureTask;

    public HttpTask(RequestHolder<T> requestHolder) {
        httpService = requestHolder.getHttpService();
        httpService.setHttpListener(requestHolder.getHttpListener());
        httpService.setUrl(requestHolder.getUrl());
        //增加方法
        IHttpListener httpListener = requestHolder.getHttpListener();
        httpListener.addHttpHeader(httpService.getHttpHeadMap());
        try {
            T request = requestHolder.getRequestInfo();
            if (request != null) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String requestInfo = gson.toJson(request);
                httpService.setRequestData(requestInfo.getBytes("UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        httpService.execute();
    }

    /**
     * 新增方法
     */
    public void start() {
        futureTask = new FutureTask<>(this, null);
        try {
            ThreadPoolManager.getInstance().execute(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 新增方法
     */
    public void pause() {
        httpService.pause();
        if (futureTask != null) {
            ThreadPoolManager.getInstance().removeTask(futureTask);
        }

    }
}
