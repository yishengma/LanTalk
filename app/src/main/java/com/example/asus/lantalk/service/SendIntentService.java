package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.entity.SocketBean;

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
    private static OnSendListener mSendListener;

    public static void setSendListener(OnSendListener sendListener) {
        mSendListener = sendListener;
    }

    public interface OnSendListener {
        void onSendSuccess();
        void onSendFail();
    }

    public SendIntentService() {
        super("SendIntentService");
    }


    public SendIntentService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals(ACTION_SEND_FILE)) {

        } else if (intent.getAction().equals(ACTION_SEND_MSG)) {
            SocketBean socketBean = (SocketBean) intent.getSerializableExtra(SEND_PEER_BEAN);
            send(socketBean);
        }

    }

    public void send(final SocketBean socketBean) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = new Socket();
                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(socketBean.getReceiveIP(), SERVER_PORT)), 3000);
                    ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                    os.writeObject(socketBean);
                    os.flush();
                    if (mSendListener!=null){
                        mSendListener.onSendSuccess();
                    }

                } catch (IOException e) {
                    if (mSendListener!=null){
                        mSendListener.onSendFail();
                    }
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
