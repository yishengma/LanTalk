package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.asus.lantalk.listener.OnScanListener;
import com.example.asus.lantalk.utils.ScanDeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 18-5-9.
 */

public class ScanService extends IntentService {
    private static final String TAG = "ScanService";

    public ScanService() {
        super("ScanService");
    }
    public ScanService(String name) {
        super(name);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ScanDeviceUtil.scan();
            }
        }).start();
    }




}
