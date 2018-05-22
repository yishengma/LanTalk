package com.example.asus.lantalk.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.ProfilePhotoAdapter;

/**
 * Created by asus on 18-5-22.
 */

public class PickHeadDialogUtil {
    private static Dialog sDialog;

    public static boolean createDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_pick_head, null);// 得到加载view
        LinearLayout layout =view.findViewById(R.id.linear);// 加载布局
        ViewPager viewPager = view.findViewById(R.id.vp_head_portrait);
        viewPager.setPageMargin(80);
        viewPager.setOffscreenPageLimit(3);
        ProfilePhotoAdapter adapter = new ProfilePhotoAdapter(context);
        viewPager.setAdapter(adapter);
        sDialog= new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        sDialog.setCancelable(true); // 是否可以按“返回键”消失
        sDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        sDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        Window window = sDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        sDialog.show();
        return sDialog.isShowing();
    }


    public static void closeDialog() {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }
    }

}

