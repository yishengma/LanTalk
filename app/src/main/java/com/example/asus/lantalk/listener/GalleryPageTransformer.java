package com.example.asus.lantalk.listener;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager 的 PageTransformer
 */

public class GalleryPageTransformer implements ViewPager.PageTransformer{
    private static final float min_scale = 0.7f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            page.setScaleX(min_scale);
            page.setScaleY(min_scale);

        } else if (position <= 0) {
            float scaleFactorForFront = 1 + 0.3f * position;
            page.setScaleX(scaleFactorForFront);
            page.setScaleY(scaleFactorForFront);
        } else {
            float scaleFactorForAfter = 1 - position * 0.3f;
            page.setScaleX(scaleFactorForAfter);
            page.setScaleY(scaleFactorForAfter);
        }
    }
}
