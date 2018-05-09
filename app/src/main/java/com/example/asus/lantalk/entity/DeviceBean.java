package com.example.asus.lantalk.entity;

import java.io.Serializable;

/**
 * Created by asus on 18-5-9.
 */

public class DeviceBean implements Serializable{
    private String mName;
    private String mLocalIP;
    private String mPeerIP;
    private String mMessage;
    private String mTime;
    private String mStatus;

    public String getName() {
        return mName == null ? "" : mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLocalIP() {
        return mLocalIP == null ? "" : mLocalIP;
    }

    public void setLocalIP(String localIP) {
        mLocalIP = localIP;
    }

    public String getPeerIP() {
        return mPeerIP == null ? "" : mPeerIP;
    }

    public void setPeerIP(String peerIP) {
        mPeerIP = peerIP;
    }

    public String getMessage() {
        return mMessage == null ? "" : mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getTime() {
        return mTime == null ? "" : mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getStatus() {
        return mStatus == null ? "" : mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
