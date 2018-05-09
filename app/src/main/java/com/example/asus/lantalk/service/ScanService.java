package com.example.asus.lantalk.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.asus.lantalk.entity.DeviceBean;
import com.example.asus.lantalk.utils.ScanDeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 18-5-9.
 */

public class ScanService extends Service {
    private List<String> mStringList;
    private DeviceBinder mBinder = new DeviceBinder();
    private static final String TAG = "ScanService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStringList = new ArrayList<>();
        mStringList = ScanDeviceUtil.scan();
    }


  public   class DeviceBinder extends Binder{

        public List<String> getIPList(){
            return mStringList;
        }
    }
}
