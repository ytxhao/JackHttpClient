package com.test.jackhttpclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerRequestInfo {

    /**
     * userId : 1
     * roomId : 2
     * selectLocation : IN_DEV
     * sdkVersion : 1.0.1
     * appId : starmaker
     * ownerId : 123456
     */

    @Expose
    @SerializedName(value = "userId")
    private String userId;
    @Expose
    @SerializedName(value = "roomId")
    private String roomId;
    @Expose
    @SerializedName(value = "selectLocation")
    private String location;
    @Expose
    @SerializedName(value = "sdkVersion")
    private String sdkVersion;
    @Expose
    @SerializedName(value = "appId")
    private String appId;
    @Expose
    @SerializedName(value = "isOwner")
    private int isOwner;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }
}
