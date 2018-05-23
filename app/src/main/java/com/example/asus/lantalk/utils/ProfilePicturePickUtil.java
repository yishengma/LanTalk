package com.example.asus.lantalk.utils;

import com.example.asus.lantalk.R;

/**
 * Created by asus on 18-5-23.
 */

public class ProfilePicturePickUtil {

    public static int getImageDrawable(int position) {
        int drawable = 0;
        switch (position) {
            case 0:
                drawable = R.drawable.iv_head_1;
                break;
            case 1:
                drawable = R.drawable.iv_head_2;
                break;
            case 2:
                drawable = R.drawable.iv_head_3;
                break;
            case 3:
                drawable = R.drawable.iv_head_4;
                break;
            case 4:
                drawable = R.drawable.iv_head_5;
                break;
            case 5:
                drawable = R.drawable.iv_head_6;
                break;
            case 6:
                drawable = R.drawable.iv_head_7;
                break;
            case 7:
                drawable = R.drawable.iv_head_8;
                break;
            case 8:
                drawable = R.drawable.iv_head_9;
                break;
            case 9:
                drawable = R.drawable.iv_head_10;
                break;
            case 10:
                drawable = R.drawable.iv_head_11;
                break;
            case 11:
                drawable = R.drawable.iv_head_12;
                break;
            default:
                drawable = R.drawable.iv_head_1;

        }

        return drawable;
    }
}
