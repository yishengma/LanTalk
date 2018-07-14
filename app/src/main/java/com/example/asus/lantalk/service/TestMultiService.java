package com.example.asus.lantalk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.utils.NetIPUtil;
import com.example.asus.lantalk.utils.TransfromUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by asus on 18-7-14.
 */

public class TestMultiService extends Service {

    private MulticastSocket mSocket;
    private InetAddress mAddress;
    private static final String TAG = "TestMultiService";

    @Override
    public void onCreate() {
        try {
            mAddress = InetAddress.getByName(NetIPUtil.getBroadcastIPAddress());
            mSocket = new MulticastSocket(Constant.SERVER_MULTI_PORT);
            mSocket.setTimeToLive(255);
            new WorkThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate();
    }
    private class WorkThread extends Thread {

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 1024);
            while (true) {
                try {
                    mSocket.receive(datagramPacket);
                    SocketBean socketBean = TransfromUtil.ByteToObject(datagramPacket.getData());
                    Log.e(TAG, "run: "+socketBean.getImageId());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
