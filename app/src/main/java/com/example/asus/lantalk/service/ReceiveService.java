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
import com.example.asus.lantalk.utils.TimeUtil;
import com.example.asus.lantalk.utils.TransfromUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import static com.example.asus.lantalk.constant.Constant.PEERFILE;


import static com.example.asus.lantalk.constant.Constant.PEERMSG;
import static com.example.asus.lantalk.constant.Constant.SERVER_FILE_PORT;
import static com.example.asus.lantalk.constant.Constant.SERVER_MSG_PORT;


/**
 * 接收消息的服务
 */

public class ReceiveService extends Service {
    private ServerSocket mMsgServerSocket;
    private Socket mMsgSocket;
    private MulticastSocket mSocket;
    private InetAddress mAddress;
    private ServerSocket mFileServerSocket;
    private Socket mFileSocket;
    private ObjectInputStream mObjectInputStream;


    private DataInputStream mDataInputStream = null;
    private FileOutputStream mFileOutputStream = null;
    private static final String TAG = "ReceiveService";

    private static OnReceiveListener mReceiveListener;
    private static OnConnectListener mConnectListener;

    public static void setConnectListener(OnConnectListener connectListener) {
        mConnectListener = connectListener;
    }

    public static void setReceiveListener(OnReceiveListener receiveListener) {
        mReceiveListener = receiveListener;
    }

    public interface OnReceiveListener {
        //聊天界面收到的消息
        void onReceiveCallBack(SocketBean bean);
    }

    public interface OnConnectListener {
        //显示新的成员
        void onConnectCallBack(SocketBean bean);

        //显示收到的大致消息
        void onReceiveCallBack(SocketBean bean);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initMsgServer();
        initFileServer();
        initBroadcastServer();


    }

    /**
     * 局域网广播接收器
     */
    private void initBroadcastServer() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mAddress = InetAddress.getByName(NetIPUtil.getBroadcastIPAddress());
                    mSocket = new MulticastSocket(Constant.SERVER_MULTI_PORT);
                    mSocket.setTimeToLive(Constant.sTTL);
                    byte[] buffer = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(buffer, 1024);
                    while (true) {
                        mSocket.receive(datagramPacket);
                        SocketBean socketBean = TransfromUtil.ByteToObject(datagramPacket.getData());
                        String newIP = socketBean.getSendIP();
                        if (!newIP.equals(App.sIP)&&!App.getsHistoryMap().keySet().contains(newIP)) {

                            App.getsHistoryMap().put(newIP, new ArrayList<SocketBean>());
                            if (mConnectListener!=null){
                                mConnectListener.onConnectCallBack(socketBean);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (mSocket.isConnected()) {
                    if (mSocket != null) {
                        mSocket.close();
                    }
                }


            }
        }).start();


    }

    public void initMsgServer() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    mMsgServerSocket = new ServerSocket(SERVER_MSG_PORT);
                    while (true) {
                        mMsgSocket = mMsgServerSocket.accept();
                        mObjectInputStream = new ObjectInputStream(mMsgSocket.getInputStream());
                        Object object = mObjectInputStream.readObject();
                        receiveMsg(object);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (mObjectInputStream != null) {
                            mObjectInputStream.close();
                        }
                        if (!mMsgSocket.isConnected()) {
                            if (mMsgSocket != null) {
                                mMsgSocket.close();

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }


    public void initFileServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mFileServerSocket = new ServerSocket(SERVER_FILE_PORT);
                    while (true) {
                        mFileSocket = mFileServerSocket.accept();
                        File file = new File(App.getsCacheDir(), TimeUtil.getCurrentTime() + "image.jpg");
                        mFileOutputStream = new FileOutputStream(file);
                        mDataInputStream = new DataInputStream(mFileSocket.getInputStream());

                        byte[] bytes = new byte[1024];
                        int length = 0;
                        while ((length = mDataInputStream.read(bytes, 0, bytes.length)) != -1) {
                            mFileOutputStream.write(bytes, 0, length);
                            mFileOutputStream.flush();
                        }
                        String ip = ("" + mFileSocket.getInetAddress()).replace("/", "");
                        receiveFile(ip, file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                } finally {
                    try {
                        if (!mFileSocket.isConnected()) {
                            if (mFileSocket != null) {
                                mFileSocket.close();

                            }
                        }
                        if (mFileOutputStream != null) {
                            mFileOutputStream.close();
                        }
                        if (mDataInputStream != null) {
                            mDataInputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void receiveMsg(Object object) {
        SocketBean socketBeen = (SocketBean) object;
        socketBeen.setType(PEERMSG);
        for (String ip : App.getsHistoryMap().keySet()) {
            if (ip.equals(socketBeen.getSendIP())) {
                App.getsHistoryMap().get(ip).add(socketBeen);

            }
        }
        if (mReceiveListener != null) {
            mReceiveListener.onReceiveCallBack(socketBeen);
        }

        if (mConnectListener != null) {
            mConnectListener.onReceiveCallBack(socketBeen);
        }


    }

    public void receiveFile(String ip, String filePath) {


        SocketBean socketBean = new SocketBean();
        socketBean.setFilePath(filePath);
        socketBean.setFile(true);
        socketBean.setType(PEERFILE);
        socketBean.setSendIP(ip);
        socketBean.setMessage("[图片]");

        //保存记录
        for (String peer : App.getsHistoryMap().keySet()) {
            if (peer.equals(socketBean.getSendIP())) {
                App.getsHistoryMap().get(peer).add(socketBean);
            }
        }


        if (mReceiveListener != null) {

            mReceiveListener.onReceiveCallBack(socketBean);
        }
        if (mConnectListener != null) {

            mConnectListener.onReceiveCallBack(socketBean);
        }

    }
}

