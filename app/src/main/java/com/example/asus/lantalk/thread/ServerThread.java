package com.example.asus.lantalk.thread;

import android.content.Context;
import android.util.Log;

import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.utils.CrazyitMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by asus on 18-5-9.
 */

public class ServerThread extends Thread {
   private Socket mSocket = null;
   private BufferedReader mBufferedReader = null;
   private PrintStream mPrintStream = null;
   private  CrazyitMap<String,PrintStream> clients = new CrazyitMap<>();
    private static final String TAG = "ServerThread";
    public ServerThread(Socket socket) {
        this.mSocket = socket;
    }

    @Override
    public void run() {

        try{
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mPrintStream = new PrintStream(mSocket.getOutputStream());
            String line = null;
            while ((line=mBufferedReader.readLine())!=null){
                if (line.startsWith(Constant.USER_ROUND)&&line.startsWith(Constant.USER_ROUND)){
                    String userName = getRealMsg(line);
                    if (clients.map.containsKey(userName)){
                        Log.e(TAG,"重复");
                        mPrintStream.println(Constant.NAME_RPE);
                    }
                    else {
                        Log.e(TAG,"成功");
                        mPrintStream.println(Constant.LOGIN_SUCCESS);
                        clients.put(userName,mPrintStream);

                    }
                }


                else if (line.startsWith(Constant.PRIVATE_ROUND)&&line.endsWith(Constant.PRIVATE_ROUND)){
                    String userAndMsg = getRealMsg(line);
                    Log.e(TAG,userAndMsg);
                    String user = userAndMsg.split(Constant.SPLIT_SIGN)[0];
                    String msg = userAndMsg.split(Constant.SPLIT_SIGN)[1];

                    clients.map.get(user).println(msg);


                }
                else {
                    String msg = getRealMsg(line);
                    for (PrintStream clentPs :clients.valueSet()){
                        clentPs.println(clients.getKeyByValue(mPrintStream)+"说"+msg);
                    }
                }
            }
        }catch (IOException e){
            clients.removeByValue(mPrintStream);
            Log.e(TAG,""+clients.map.size());
            try{
                if (mBufferedReader!=null){
                    mBufferedReader.close();
                }
                if (mPrintStream!=null){
                    mPrintStream.close();
                }
                if (mSocket!=null){
                    mSocket.close();
                }
            }catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }


    private String getRealMsg(String line){

        return line.substring(Constant.PROTOCOL_LEN,line.length()-Constant.PROTOCOL_LEN);
    }
}
