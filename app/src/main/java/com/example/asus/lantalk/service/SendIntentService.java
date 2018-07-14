package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_FILE;
import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_MSG;
import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;
import static com.example.asus.lantalk.constant.Constant.SERVER_FILE_PORT;
import static com.example.asus.lantalk.constant.Constant.SERVER_MSG_PORT;

/**
 * 发送消息的服务
 */
public class SendIntentService extends IntentService {
    private static final String TAG = "SendIntentService";
    private static OnSendListener mSendListener;

    public static void setSendListener(OnSendListener sendListener) {
        mSendListener = sendListener;
    }

    public interface OnSendListener {
        void onSendSuccess(String type,int id);

        void onSendFail(String type,int id);
    }

    public SendIntentService() {
        super("SendIntentService");
    }


    public SendIntentService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent==null||intent.getAction()==null){
            return;
        }
        if (intent.getAction().equals(ACTION_SEND_FILE)) {
            SocketBean socketBean = (SocketBean) intent.getSerializableExtra(SEND_PEER_BEAN);
            socketBean.setType(Constant.PEERFILE);
            sendFile(socketBean);

        } else if (intent.getAction().equals(ACTION_SEND_MSG)) {
            SocketBean socketBean = (SocketBean) intent.getSerializableExtra(SEND_PEER_BEAN);
            socketBean.setType(Constant.PEERMSG);
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
                        mSendListener.onSendSuccess(ACTION_SEND_MSG,0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    if (mSendListener != null) {
                        mSendListener.onSendFail(ACTION_SEND_MSG,0);
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
                            mSendListener.onSendSuccess(ACTION_SEND_FILE,socketBean.getImageId());
                        }
                    }
                    } catch(IOException e){
                        if (mSendListener != null) {
                            mSendListener.onSendFail(ACTION_SEND_FILE,socketBean.getImageId());
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
