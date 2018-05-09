package com.example.asus.lantalk.thread;



import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;


public class ClientThread extends Thread{
    BufferedReader bufferedReader = null;
    private static final String TAG = "ClientThread";

    public ClientThread(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;

    }

    @Override
    public void run() {
        try{
            String line = null;
            while ((line=bufferedReader.readLine())!=null){
                Log.e(TAG,line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        finally {
            try{
                if (bufferedReader!=null){
                    bufferedReader.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}