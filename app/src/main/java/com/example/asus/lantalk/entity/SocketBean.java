package com.example.asus.lantalk.entity;

import java.io.Serializable;

/**
 * Created by asus on 18-5-9.
 */

public class SocketBean implements Serializable{
    private String mSendName;
    private String mSendIP;
    private String mReceiveName;
    private String mReceiveIP;
    private String mMessage;
    private String mTime;
    private int mStatus;
    private int mType;

    private String mFilePath;
    private  boolean mIsFile ;

    public String getFilePath() {
        return mFilePath == null ? "" : mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public boolean isFile() {
        return mIsFile;
    }

    public void setFile(boolean file) {
        mIsFile = file;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }


    public String getSendName() {
        return mSendName == null ? "" : mSendName;
    }

    public void setSendName(String sendName) {
        mSendName = sendName;
    }

    public String getSendIP() {
        return mSendIP == null ? "" : mSendIP;
    }

    public void setSendIP(String sendIP) {
        mSendIP = sendIP;
    }

    public String getReceiveName() {
        return mReceiveName == null ? "" : mReceiveName;
    }

    public void setReceiveName(String receiveName) {
        mReceiveName = receiveName;
    }

    public String getReceiveIP() {
        return mReceiveIP == null ? "" : mReceiveIP;
    }

    public void setReceiveIP(String receiveIP) {
        mReceiveIP = receiveIP;
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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }
}
