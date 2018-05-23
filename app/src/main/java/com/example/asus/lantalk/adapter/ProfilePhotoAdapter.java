package com.example.asus.lantalk.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
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
    private static final String TAG = "ProfilePhotoAdapter";

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private OnClickListener mOnClickListener;
    public ProfilePhotoAdapter(Context context) {
        mContext = context;
    }
    public interface OnClickListener{
        void OnClick(int position);
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
    public Object instantiateItem(ViewGroup container, final int position) {
        CircleImageView view = new CircleImageView(mContext);
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (mOnClickListener!=null){
                     mOnClickListener.OnClick(position);
                 }
             }
         });
        view.setImageResource(Constant.PROFILEPHOTO[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
