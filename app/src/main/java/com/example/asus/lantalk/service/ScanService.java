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
    private List<String> mStringList;
    private PeerBinder mBinder = new PeerBinder();
    private static final String TAG = "ScanService";

    public ScanService() {
        super("ScanService");
    }
    public ScanService(String name) {
        super(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mStringList = new ArrayList<>();
        mStringList = ScanDeviceUtil.scan();
    }

    public   class PeerBinder extends Binder{

        public List<String> getIPList(){
            return mStringList;
        }
    }
}
