package com.example.asus.lantalk.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.lantalk.R;

/**
 * Created by asus on 18-5-11.
 */

public class LoadingDialogUtil {
         private static Dialog sDialog;

        /**
         * 显示加载框
         * @param context
         * @param msg
         * @return
         */
        public static Dialog createLoadingDialog(Context context, String msg) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
            LinearLayout layout =view.findViewById(R.id.dialog_loading_view);// 加载布局
            TextView tipTextView =  view.findViewById(R.id.tipTextView);// 提示文字
            tipTextView.setText(msg);// 设置加载信息

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
            return sDialog;
        }


        public static void closeDialog() {
            if (sDialog != null && sDialog.isShowing()) {
                sDialog.dismiss();
            }
        }

    }

