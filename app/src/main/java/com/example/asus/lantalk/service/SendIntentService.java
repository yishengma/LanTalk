package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.entity.PeerBean;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_FILE;
import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_MSG;
import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;
import static com.example.asus.lantalk.constant.Constant.SERVER_PORT;

/**
 * Created by asus on 18-5-9.
 */

public class SendIntentService extends IntentService {
    private static final String TAG = "SendIntentService";


    public SendIntentService(String name) {
        super(name);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(ACTION_SEND_FILE)){

        }else if (intent.getAction().equals(ACTION_SEND_MSG)){
              PeerBean peerBean = (PeerBean) intent.getSerializableExtra(SEND_PEER_BEAN);
               send(peerBean);
        }

    }

    public void send(final PeerBean peerBean){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = new Socket();
                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(peerBean.getPeerIP(), SERVER_PORT)), 3000);
                    ObjectOutputStream os  = new ObjectOutputStream(socket.getOutputStream());
                    os.writeObject(peerBean);
                    os.flush();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    if (socket != null) {
                        if (socket.isConnected()) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }
}
