package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.utils.ScanDeviceUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_FILE;
import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_MSG;
import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;
import static com.example.asus.lantalk.constant.Constant.SERVER_FILE_PORT;
import static com.example.asus.lantalk.constant.Constant.SERVER_MSG_PORT;


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
                    socket.connect((new InetSocketAddress(socketBean.getReceiveIP(), SERVER_MSG_PORT)), 3000);
                    os = new ObjectOutputStream(socket.getOutputStream());
                    os.writeObject(socketBean);
                    os.flush();
                    if (mSendListener != null) {
                        mSendListener.onSendSuccess(ACTION_SEND_MSG);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
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

                    if (os != null) {
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
                FileInputStream fileInputStream = null;
                DataOutputStream dataOutputStream = null;
                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(socketBean.getReceiveIP(), SERVER_FILE_PORT)), 3000);



                    File file = new File(socketBean.getFilePath());
                    if (file.exists()) {
                        fileInputStream = new FileInputStream(file);
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());



                        byte[] bytes = new byte[1024];
                        int length = 0;
                        while ((length = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                            dataOutputStream.write(bytes, 0, length);
                            dataOutputStream.flush();

                        }


                        if (mSendListener != null) {
                            mSendListener.onSendSuccess(ACTION_SEND_FILE);
                        }
                    }
                    } catch(IOException e){
                        if (mSendListener != null) {
                            mSendListener.onSendFail();
                        }
                    } finally{
                        if (socket != null) {
                            if (socket.isConnected()) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (dataOutputStream != null) {
                            try {
                                dataOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }).

            start();
        }
    }
