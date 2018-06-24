package com.example.asus.lantalk.entity;

import java.io.Serializable;

/**
 * 通信Socket 的内容
 */

public class SocketBean implements Serializable{
    private String mSendName;
    private String mSendIP;
    private String mReceiveName;
    private String mReceiveIP;
    private String mMessage;
    private String mTime;
    private int mProfilePicture;
    private int mStatus;
    private int mType;

    private String mFilePath;
    private  boolean mIsFile ;

    private int mImageStatus;
    private int mImageId;

    @Override
    public boolean equals(Object obj) {
        return getSendIP().equals(((SocketBean)obj).getSendIP());
    }

    public int getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        mProfilePicture = profilePicture;
    }

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

    public int getImageStatus() {
        return mImageStatus;
    }

    public void setImageStatus(int imageStatus) {
        mImageStatus = imageStatus;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }
}
