package com.example.asus.lantalk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.utils.TimeUtil;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.PEERFILE;


import static com.example.asus.lantalk.constant.Constant.SERVER_FILE_PORT;
import static com.example.asus.lantalk.constant.Constant.SERVER_MSG_PORT;


/**
 * Created by asus on 18-5-12.
 */

public class ReceiveService extends Service {
    private ServerSocket mMsgServerSocket;
    private Socket mMsgSocket;

    private ServerSocket mFileServerSocket;
    private Socket mFileSocket;
    private ObjectInputStream mObjectInputStream;



    private DataInputStream mDataInputStream = null;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
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
        void onReceiveCallBack(SocketBean bean);

        void onReceiceFailed();
    }

    public interface OnConnectListener {
        void onRefuseCallBack();

        void onAccessCallBack(SocketBean bean);

        void onRequestCallBack(SocketBean bean);

        void onConnectFailed();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        initMsgServer();
        initFileServer();
        super.onCreate();

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
                    if (mConnectListener != null) {
                        mConnectListener.onConnectFailed();
                    }
                    if (mReceiveListener != null) {
                        mReceiveListener.onReceiceFailed();
                    }

                } finally {
                    try {
                        if (mMsgSocket.isConnected()) {
                            if (mMsgSocket != null) {
                                mMsgSocket.close();
                            }
                        }

                        if (mDataInputStream != null) {
                            mDataInputStream.close();
                        }
                        if (mFileOutputStream != null) {
                            mFileOutputStream.close();
                        }
                        if (mObjectInputStream != null) {
                            mObjectInputStream.close();
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
                        receiveFile(mFileSocket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mReceiveListener != null) {
                        mReceiveListener.onReceiceFailed();
                    }

                } finally {
                    try {
                        if (mFileSocket.isConnected()) {
                            if (mFileSocket != null) {
                                mFileSocket.close();
                            }
                        }

                        if (mOutputStream != null) {
                            mOutputStream.close();
                        }

                        if (mInputStream != null) {
                            mInputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void receiveMsg(Object object) throws Exception {

        SocketBean socketBeen = (SocketBean) object;
        if (mConnectListener != null && socketBeen.getStatus() == Constant.CONNECT) {
            mConnectListener.onAccessCallBack(socketBeen);
        } else if (mConnectListener != null && socketBeen.getStatus() == Constant.REFUSE) {
            mConnectListener.onRefuseCallBack();
        } else if (mConnectListener != null && socketBeen.getStatus() == Constant.REQUEST) {
            mConnectListener.onRequestCallBack(socketBeen);
        } else if (mReceiveListener != null && socketBeen.getStatus() == Constant.RECEIVE) {
            socketBeen.setType(Constant.PEERMSG);
            mReceiveListener.onReceiveCallBack(socketBeen);
        }
    }

    public void receiveFile(Socket socket) throws IOException {
        File file = new File(App.getsCacheDir(), TimeUtil.getCurrentTime() + "image.jpg");
        File dirs = new File(file.getParent());
        if (!dirs.exists())
            dirs.mkdirs();
        file.createNewFile();
        mFileOutputStream = new FileOutputStream(file);
        mDataInputStream = new DataInputStream(socket.getInputStream());

        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = mDataInputStream.read(bytes, 0, bytes.length)) != -1) {
            mFileOutputStream.write(bytes, 0, length);
            mFileOutputStream.flush();


        }
        SocketBean socketBean = new SocketBean();
        socketBean.setFilePath(file.getAbsolutePath());
        socketBean.setFile(true);
        socketBean.setType(PEERFILE);
        if (mReceiveListener!=null){
            mReceiveListener.onReceiveCallBack(socketBean);
        }

    }
}

