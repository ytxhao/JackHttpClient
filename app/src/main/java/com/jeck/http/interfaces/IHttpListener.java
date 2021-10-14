package com.jeck.http.interfaces;


import java.util.Map;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public interface IHttpListener {
    /**
     * 网络访问
     * 处理结果  回调
     *
     * @param httpEntity
     */
    void onSuccess(IHttpEntity httpEntity);

    void onFail();

    void addHttpHeader(Map<String, String> headerMap);
}
