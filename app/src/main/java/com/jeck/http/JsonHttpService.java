package com.jeck.http;


import android.text.TextUtils;

import com.jeck.http.bean.JsonHttpEntity;
import com.jeck.http.interfaces.IHttpListener;
import com.jeck.http.interfaces.IHttpService;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class JsonHttpService implements IHttpService {
//    private static final String[] methods = {
//            "GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE"};

    private String method;
    private IHttpListener httpListener;
    private String url;

    private byte[] requestData;
    private Map<String, String> headerMap = Collections.synchronizedMap(new HashMap<String, String>());

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void execute() {
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            JsonHttpEntity jsonHttpEntity = new JsonHttpEntity();
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(method);
            if (!headerMap.isEmpty()) {
                for (String key : headerMap.keySet()) {
                    String value = headerMap.get(key);
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        conn.setRequestProperty(key, value);
                    }
                }
            } else {
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
            }

            if (requestData != null && requestData.length > 0) {
                os = conn.getOutputStream();
                //  os.write("userId=1&roomId=2&selectLocation=IN_DEV&sdkVersion=1.0.1&appId=starmaker".getBytes());
                os.write(requestData);
                os.flush();
            }

            int code = conn.getResponseCode();

            if (code == 200) {
                is = conn.getInputStream();
                jsonHttpEntity.setWrappedStream(is);
                if (httpListener != null) {
                    httpListener.onSuccess(jsonHttpEntity);
                }
            } else if (httpListener != null) {
                httpListener.onFail();
            }

            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpListener.onFail();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    @Override
    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    @Override
    public void pause() {

    }

    @Override
    public Map<String, String> getHttpHeadMap() {
        return headerMap;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public boolean isCancel() {
        return false;
    }

    @Override
    public boolean isPause() {
        return false;
    }

}
