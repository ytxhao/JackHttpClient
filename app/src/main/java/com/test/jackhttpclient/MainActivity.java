package com.test.jackhttpclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jeck.http.ZRHttpClient;
import com.jeck.http.interfaces.IDataListener;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.bt_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("userId", "userId");
                map.put("roomId", "roomId");
                map.put("selectLocation", "location");
                map.put("sdkVersion", "getSDKVersionNative");
                map.put("appId", "appId");
                map.put("isOwner", "isOwner");

                ZRHttpClient.getInstance().getRequest(Const.OFFICIAL_WEBSITES_URL, map, ServerResponseInfo.class, new IDataListener<ServerResponseInfo>() {
                    @Override
                    public void onSuccess(ServerResponseInfo serverResponseInfo) {
                        Log.d(TAG, "Get Server Info onSuccess");
                        String domain = serverResponseInfo.getSignalServer().getDomain();
                        String hostAddr = serverResponseInfo.getSignalServer().getHostAddress();
                        int hostPort = serverResponseInfo.getSignalServer().getPort();
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "Get Server Info Error");

                    }
                });
            }
        });
    }
}