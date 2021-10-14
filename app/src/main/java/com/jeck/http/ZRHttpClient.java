package com.jeck.http;

import android.text.TextUtils;
import android.util.Log;

import com.jeck.http.interfaces.IDataListener;
import com.jeck.http.interfaces.IHttpListener;
import com.jeck.http.interfaces.IHttpService;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;


public class ZRHttpClient {

    private final String TAG = "ZRHttpClient";

    private boolean isUrlEncodingEnabled = true;
    private volatile static ZRHttpClient mZRHttpClient;

    public enum METHODS {
        GET("GET"),
        POST("POST"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS"),
        PUT("PUT"),
        DELETE("DELETE");

        private String method;

        METHODS(String method) {
            this.method = method;
        }
    }

    private ZRHttpClient() {
    }

    public static ZRHttpClient getInstance() {
        if (mZRHttpClient == null) {
            synchronized (ZRHttpClient.class) {
                if (mZRHttpClient == null) {
                    mZRHttpClient = new ZRHttpClient();
                }
            }

        }

        return mZRHttpClient;
    }

    /**
     * @param <T> 请求参数类型
     * @param <M> 响应参数类型
     *            暴露给调用层
     */
    public <T, M> void sendRequest(METHODS method, String url, Map<String, String> headMap,
                                   T requestInfo,
                                   Class<M> response, IDataListener<M> dataListener) {
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(url);
        IHttpService httpService = new JsonHttpService();
        IHttpListener httpListener = new JsonResponseListener<>(response, dataListener);
        httpService.setMethod(method.name());
        if (headMap != null) {
            Map<String, String> map = httpService.getHttpHeadMap();
            for (String key : headMap.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
                String value = headMap.get(key);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            }
        }

        requestHolder.setHttpService(httpService);
        requestHolder.setHttpListener(httpListener);
        //将请求参数赋值
        requestHolder.setRequestInfo(requestInfo);

        HttpTask<T> httpTask = new HttpTask<>(requestHolder);
        try {
            ThreadPoolManager.getInstance().execute(new FutureTask<Object>(httpTask, null));
        } catch (InterruptedException e) {
            dataListener.onError();
        }
    }

    public <T> void getRequest(String url, Map<String, String> params, Class<T> response, IDataListener<T> dataListener) {
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(getUrlWithQueryString(isUrlEncodingEnabled, url, params));
        IHttpService httpService = new JsonHttpService();
        IHttpListener httpListener = new JsonResponseListener<>(response, dataListener);
        httpService.setMethod(METHODS.GET.name());
        requestHolder.setHttpService(httpService);
        requestHolder.setHttpListener(httpListener);

        HttpTask<T> httpTask = new HttpTask<>(requestHolder);
        try {
            ThreadPoolManager.getInstance().execute(new FutureTask<Object>(httpTask, null));
        } catch (InterruptedException e) {
            dataListener.onError();
        }

    }

    /**
     * @param <T> 请求参数类型
     * @param <M> 响应参数类型
     *            暴露给调用层
     */
    public <T, M> void postRequest(String url, Map<String, String> headMap, T requestInfo, Class<M> response, IDataListener<M> dataListener) {
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(getUrlWithQueryString(isUrlEncodingEnabled, url, null));
        IHttpService httpService = new JsonHttpService();
        IHttpListener httpListener = new JsonResponseListener<>(response, dataListener);
        httpService.setMethod(METHODS.POST.name());
        requestHolder.setHttpService(httpService);
        requestHolder.setHttpListener(httpListener);
        requestHolder.setRequestInfo(requestInfo);
        if (headMap != null) {
            Map<String, String> map = httpService.getHttpHeadMap();
            for (String key : headMap.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
                String value = headMap.get(key);
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            }
        }

        HttpTask<T> httpTask = new HttpTask<>(requestHolder);
        try {
            ThreadPoolManager.getInstance().execute(new FutureTask<Object>(httpTask, null));
        } catch (InterruptedException e) {
            dataListener.onError();
        }

    }

    public <T, M> void postRequest(String url, T requestInfo, Class<M> response, IDataListener<M> dataListener) {
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(getUrlWithQueryString(isUrlEncodingEnabled, url, null));
        IHttpService httpService = new JsonHttpService();
        IHttpListener httpListener = new JsonResponseListener<>(response, dataListener);
        httpService.setMethod(METHODS.POST.name());
        requestHolder.setHttpService(httpService);
        requestHolder.setHttpListener(httpListener);
        requestHolder.setRequestInfo(requestInfo);
        Map<String, String> headMap = httpService.getHttpHeadMap();
        headMap.put("connection", "Keep-Alive");
        headMap.put("accept", "application/json");
        headMap.put("content-type", "application/json; charset=utf-8");
        HttpTask<T> httpTask = new HttpTask<>(requestHolder);
        try {
            ThreadPoolManager.getInstance().execute(new FutureTask<Object>(httpTask, null));
        } catch (InterruptedException e) {
            dataListener.onError();
        }

    }


    private String getUrlWithQueryString(boolean shouldEncodeUrl, String url, Map<String, String> params) {
        if (url == null)
            return null;

        if (shouldEncodeUrl) {
            try {
                String decodedURL = URLDecoder.decode(url, "UTF-8");
                URL _url = new URL(decodedURL);
                URI _uri = new URI(_url.getProtocol(), _url.getUserInfo(), _url.getHost(), _url.getPort(), _url.getPath(), _url.getQuery(), _url.getRef());
                url = _uri.toASCIIString();
            } catch (Exception ex) {
                Log.e(TAG, "getUrlWithQueryString encoding URL", ex);
            }
        }

        if (params != null) {
            StringBuilder urlTail = new StringBuilder();
            for (String key : params.keySet()) {
                urlTail.append(key + "=" + params.get(key) + "&");
            }

            if (!urlTail.toString().equals("") && !urlTail.toString().equals("?")) {
                url += url.contains("?") ? "&" : "?";
                url += urlTail.substring(0, urlTail.length() - 1);
            }
        }

        return url;
    }

}
