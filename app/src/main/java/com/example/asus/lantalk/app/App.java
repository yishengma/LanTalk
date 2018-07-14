package com.example.asus.lantalk.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;
import com.example.asus.lantalk.service.SendIntentService;
import com.example.asus.lantalk.ui.MainActivity;
import com.example.asus.lantalk.utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;


/**
 * Created by asus on 18-5-9.
 */

public class App extends Application {
    public static String sName;//名字
    public static String sIP;//本地ip
    public static int sProfilePicture;//头像的标识
    private static  Context sContext;
    private static File sCacheDir;
    private static Map<String,List<SocketBean>> sHistoryMap;//消息的存储列表
    public static int sImageId;
    private static final String TAG = "App";
    private static List<SocketBean> mSocketBeanList;

    @Override
    public void onCreate() {
        super.onCreate();
        sCacheDir = getExternalCacheDir();
         sContext = getApplicationContext();
        sProfilePicture =0;
        sHistoryMap = new HashMap<>();
        mSocketBeanList = new ArrayList<>();
        sImageId = 0;

    }



    public static List<SocketBean> getmSocketBeanList() {
        return mSocketBeanList;
    }

    public static void setmSocketBeanList(List<SocketBean> mSocketBeanList) {
        App.mSocketBeanList = mSocketBeanList;
    }

    public static Map<String, List<SocketBean>> getsHistoryMap() {
        return sHistoryMap;
    }

    public static File getsCacheDir() {
        return sCacheDir;
    }

    public static Context getsContext() {
        return sContext;
    }


}
