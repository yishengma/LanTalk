package com.example.asus.lantalk.app;

import android.app.Application;
import android.util.Log;

import com.example.asus.lantalk.entity.PeerBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.SERVER_PORT;

/**
 * Created by asus on 18-5-9.
 */

public class App extends Application {
    private Socket mSocket;
    public static String sName;
    public static String sIP;
    private ServerSocket mServerSocket;
    ObjectInputStream is;
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        initServer();


    }



    public void initServer() {
         new Thread(new Runnable() {
             @Override
             public void run() {
                 Log.e(TAG, "run: " );
                 try {
                     mServerSocket = new ServerSocket(SERVER_PORT);
                     while (true) {
                         mSocket = mServerSocket.accept();
                         is = new ObjectInputStream(mSocket.getInputStream());
                        Object object = is.readObject();

                        PeerBean socketBeen = (PeerBean) object;
                         Log.e(TAG, "run: " + socketBeen.getMessage());
                     }


                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (ClassNotFoundException e) {
                     e.printStackTrace();
                 }
             }
         }).start();
    }



}
