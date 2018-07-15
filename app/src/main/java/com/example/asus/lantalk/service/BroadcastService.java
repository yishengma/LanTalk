package com.example.asus.lantalk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.utils.NetIPUtil;
import com.example.asus.lantalk.utils.TransfromUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 发送局域网广播，数据为 ip 地址，昵称， 头像标识
 */
public class BroadcastService extends Service {
    private MulticastSocket mSocket;
    private DatagramPacket mDatagramPacket;
    private SocketBean mSocketBean ;
    private static final String TAG = "BroadcastIntentService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSocketBean();
        initDatagramPacket();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new BroadcastThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initSocketBean(){
        mSocketBean = new SocketBean();
        mSocketBean.setSendIP(App.sIP);
        mSocketBean.setSendName(App.sName);
        mSocketBean.setProfilePicture(App.sProfilePicture);
    }


    private void initDatagramPacket() {
        try {
            mSocket = new MulticastSocket(Constant.SERVER_MULTI_PORT);
            mSocket.setTimeToLive(255);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] data = TransfromUtil.ObjectToByte(mSocketBean);
        try {
            InetAddress a = InetAddress.getByName(NetIPUtil.getBroadcastIPAddress());
            mDatagramPacket = new DatagramPacket(data, data.length, a, Constant.SERVER_MULTI_PORT);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

   private class BroadcastThread extends Thread {

       @Override
       public void run() {

          while(true){
              try {
                  mSocket.send(mDatagramPacket);

                  sleep(5000);
              } catch (Exception e) {
                  e.printStackTrace();

              }

          }
       }

   }
}
