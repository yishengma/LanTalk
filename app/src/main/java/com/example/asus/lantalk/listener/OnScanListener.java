package com.example.asus.lantalk.listener;

import java.util.List;

/**
 * Created by asus on 18-5-11.
 */

public interface OnScanListener {
    void OnSuccess(List<String> list);
    void OnFailed();
}
