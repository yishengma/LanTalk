package com.example.asus.lantalk.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by asus on 18-5-9.
 */

public class App extends Application {
    public static String sName;
    public static String sIP;
    public static int sProfilePicture;
    private static  Context sContext;
    private static File sCacheDir;
    private static Map<String,List<SocketBean>> sHistoryMap;


    @Override
    public void onCreate() {
        super.onCreate();
        sCacheDir = getExternalCacheDir();

        Intent intent = new Intent(getApplicationContext(),ReceiveService.class);
         startService(intent);
         sContext = getApplicationContext();
        sProfilePicture =0;
        sHistoryMap = new HashMap<>();

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
