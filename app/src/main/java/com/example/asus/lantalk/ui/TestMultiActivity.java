package com.example.asus.lantalk.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.service.TestMultiService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class TestMultiActivity extends AppCompatActivity {
    public static final String BASE_IP_ADDRESS = "10.21.20.255";
    public static final int PORT = 3333;
    private MulticastSocket mSocket;
    private static final String TAG = "TestMultiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_multi);

        Intent intent = new Intent(this,TestMultiService.class);
        startService(intent);


        try {
            mSocket = new MulticastSocket(PORT);
            mSocket.setTimeToLive(255);
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendThread().start();
            }
        });

    }

    private class SendThread extends Thread {
        @Override
        public void run() {
            DatagramPacket datagramPacket = null;
            byte[] data = "Hello".getBytes();
            try {
                InetAddress a = InetAddress.getByName(BASE_IP_ADDRESS);
//                if (!a.isMulticastAddress()){
//                    throw new  Exception("NoMulticast");
//                }
                datagramPacket = new DatagramPacket(data,data.length,a,PORT);
                mSocket.send(datagramPacket);
                Log.e(TAG, "SendThread run: " );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
