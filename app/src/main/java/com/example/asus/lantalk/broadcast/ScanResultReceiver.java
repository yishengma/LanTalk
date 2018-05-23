package com.example.asus.lantalk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.asus.lantalk.utils.LoadingDialogUtil;

/**
 * Created by asus on 18-5-23.
 */

public class ScanResultReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"搜索结束",Toast.LENGTH_SHORT).show();
        LoadingDialogUtil.closeDialog();
    }
}