package com.example.asus.lantalk.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.lantalk.constant.Constant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 18-5-22.
 */

public class ProfilePhotoAdapter extends PagerAdapter {
    private Context mContext;


    public ProfilePhotoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Constant.PROFILEPHOTO.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CircleImageView view = new CircleImageView(mContext);
        view.setImageResource(Constant.PROFILEPHOTO[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
