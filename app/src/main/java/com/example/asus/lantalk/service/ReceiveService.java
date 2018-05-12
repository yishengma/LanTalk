package com.example.asus.lantalk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;

import java.io.IOException;
import java.io.ObjectInputStream;
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
    private static final String TAG = "ReceiveService";

    private static OnReceiveListener mReceiveListener;
    private static OnConnectListener mConnectListener;

    public static void setConnectListener(OnConnectListener connectListener) {
        mConnectListener = connectListener;
    }

    public static void setReceiveListener(OnReceiveListener receiveListener) {
        mReceiveListener = receiveListener;
    }
     public interface OnReceiveListener{
        void onReceiveCallBack(SocketBean bean);
        void onReceiceFailed();
     }
    public interface OnConnectListener{
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
                        mInputStream = new ObjectInputStream(mSocket.getInputStream());
                        Object object = mInputStream.readObject();
                        SocketBean socketBeen = (SocketBean) object;
                        if (mConnectListener!=null&&socketBeen.getStatus()== Constant.CONNECT){
                            mConnectListener.onAccessCallBack(socketBeen);
                        }else if (mConnectListener!=null&&socketBeen.getStatus()==Constant.REFUSE){
                            mConnectListener.onRefuseCallBack();
                        }else if (mConnectListener!=null&&socketBeen.getStatus()==Constant.REQUEST){
                            mConnectListener.onRequestCallBack(socketBeen);
                        } else if (mReceiveListener!=null&&socketBeen.getStatus()==Constant.RECEIVE){
                            mReceiveListener.onReceiveCallBack(socketBeen);
                        }
                    }
                } catch (IOException e) {
                    if (mConnectListener!=null){
                        mConnectListener.onConnectFailed();
                    }
                    if (mReceiveListener!=null){
                        mReceiveListener.onReceiceFailed();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
