package com.example.asus.lantalk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.SERVER_PORT;

/**
 * Created by asus on 18-5-12.
 */

public class ReceiveService extends Service {
    private ServerSocket mServerSocket;
    private ObjectInputStream mInputStream;
    private Socket mSocket;


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
        initServer();
        super.onCreate();

    }

    public void initServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mServerSocket = new ServerSocket(SERVER_PORT);
                    while (true) {
                        mSocket = mServerSocket.accept();
                        try {

                            mInputStream = new ObjectInputStream(mSocket.getInputStream());
                            Object object = mInputStream.readObject();
                            receiveMsg(object);
                        }catch (StreamCorruptedException exception){
                            receiveFile(mSocket);
                        }

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
                        if (mSocket.isConnected()) {
                            if (mSocket != null) {
                                mSocket.close();
                            }
                        }

                        if (mDataInputStream != null) {
                            mDataInputStream.close();
                        }
                        if (mFileOutputStream != null) {
                            mFileOutputStream.close();
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

    public void receiveMsg(Object object ) throws Exception{

        SocketBean socketBeen = (SocketBean) object;
        if (mConnectListener != null && socketBeen.getStatus() == Constant.CONNECT) {
            mConnectListener.onAccessCallBack(socketBeen);
        } else if (mConnectListener != null && socketBeen.getStatus() == Constant.REFUSE) {
            mConnectListener.onRefuseCallBack();
        } else if (mConnectListener != null && socketBeen.getStatus() == Constant.REQUEST) {
            mConnectListener.onRequestCallBack(socketBeen);
        } else if (mReceiveListener != null && socketBeen.getStatus() == Constant.RECEIVE) {
            mReceiveListener.onReceiveCallBack(socketBeen);
        }
    }
    public void receiveFile(Socket socket) throws IOException {


        mDataInputStream = new DataInputStream(socket.getInputStream());

        // 文件名和长度

        File file = new File(App.getsCacheDir(), "image.jpg");
        mFileOutputStream = new FileOutputStream(file);

        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = mDataInputStream.read(bytes, 0, bytes.length)) != -1) {
            mFileOutputStream.write(bytes, 0, length);
            mFileOutputStream.flush();


        }
        Log.e(TAG, "receiveFile: "+file.getAbsolutePath() );

    }
}

