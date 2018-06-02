package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.utils.ScanDeviceUtil;
import com.example.asus.lantalk.utils.TimeUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.example.asus.lantalk.constant.Constant.SERVER_MSG_PORT;
import static com.example.asus.lantalk.constant.Constant.SERVICE_RECEIVER;

/**
 * 扫描对等方的服务
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
    protected void onHandleIntent(@Nullable final Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ScanDeviceUtil.scan();
                Intent receive = new Intent(SERVICE_RECEIVER);
                App.getsContext().sendBroadcast(receive);
            }
        }).start();
    }


}
