package com.example.asus.lantalk.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.SERVER_PORT;

/**
 * Created by asus on 18-5-9.
 */

public class App extends Application {
    public static String sName;
    public static String sIP;
    private static  Context sContext;


    @Override
    public void onCreate() {
        super.onCreate();

         Intent intent = new Intent(getApplicationContext(),ReceiveService.class);
         startService(intent);
         sContext = getApplicationContext();
    }


    public static Context getsContext() {
        return sContext;
    }
}
