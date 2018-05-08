package com.example.asus.lantalk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.utils.ScanDeviceUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScanDeviceUtil.getLocalIPAddress();
        ScanDeviceUtil.getIPAddress();
    }
}
