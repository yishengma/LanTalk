package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.entity.SocketBean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        void onSendSuccess(String type);

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
            SocketBean socketBean = (SocketBean) intent.getSerializableExtra(SEND_PEER_BEAN);
            sendFile(socketBean);

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
                ObjectOutputStream os = null;
                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(socketBean.getReceiveIP(), SERVER_PORT)), 3000);
                     os = new ObjectOutputStream(socket.getOutputStream());
                    os.writeObject(socketBean);
                    os.flush();
                    if (mSendListener != null) {
                        mSendListener.onSendSuccess(ACTION_SEND_MSG);
                    }

                } catch (IOException e) {
                    if (mSendListener != null) {
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

                    if (os!=null){
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void sendFile(final SocketBean socketBean) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = new Socket();
                FileInputStream fis = null;
                DataOutputStream dos = null;
                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(App.sIP, SERVER_PORT)), 3000);

                    File file = new File(socketBean.getFilePath());
                    if (file.exists()) {
                         fis = new FileInputStream(file);
                         dos = new DataOutputStream(socket.getOutputStream());

                        // 文件名和长度
                        dos.writeUTF(file.getName());
                        dos.flush();
                        dos.writeLong(file.length());
                        dos.flush();

                        byte[] bytes = new byte[1024];
                        int length = 0;

                        while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                            dos.write(bytes, 0, length);
                            dos.flush();

                        }

                    }

                    if (mSendListener != null) {
                        mSendListener.onSendSuccess(ACTION_SEND_FILE);
                    }
                } catch (IOException e) {
                    if (mSendListener != null) {
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

                    if (fis!=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (dos!=null){
                        try {
                            dos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }
}
