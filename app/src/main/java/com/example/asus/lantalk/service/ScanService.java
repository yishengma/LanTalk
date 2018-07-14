package com.example.asus.lantalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.utils.NetIPUtil;

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
        //  new Thread(new Runnable() {
        //      @Override
        //     public void run() {
//                NetIPUtil.scan();
//                Intent receive = new Intent(SERVICE_RECEIVER);
//                App.getsContext().sendBroadcast(receive);
        //   }
        //  }).start();
        // }
    }

}
