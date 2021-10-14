package com.test.jackhttpclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResponseInfo {

    /**
     * signalServer : {"domain":"zorro-dev.starmakerstudios.com","hostAddress":"zorro-dev.starmakerstudios.com","port":5222}
     * mediaServerLocation : Mumbai
     */

    @Expose
    @SerializedName(value = "signalServer")
    private SignalServerBean signalServer;
    @Expose
    @SerializedName(value = "mediaServerLocation")
    private String mediaServerLocation;

    public SignalServerBean getSignalServer() {
        return signalServer;
    }

    public void setSignalServer(SignalServerBean signalServer) {
        this.signalServer = signalServer;
    }

    public String getMediaServerLocation() {
        return mediaServerLocation;
    }

    public void setMediaServerLocation(String mediaServerLocation) {
        this.mediaServerLocation = mediaServerLocation;
    }

    public static class SignalServerBean {
        /**
         * domain : zorro-dev.starmakerstudios.com
         * hostAddress : zorro-dev.starmakerstudios.com
         * port : 5222
         */

        @Expose
        @SerializedName(value = "domain")
        private String domain;
        @Expose
        @SerializedName(value = "hostAddress")
        private String hostAddress;
        @Expose
        @SerializedName(value = "port")
        private int port;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getHostAddress() {
            return hostAddress;
        }

        public void setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
