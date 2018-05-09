package com.example.asus.lantalk.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.entity.DeviceBean;

import java.util.List;

/**
 * Created by asus on 18-5-9.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{
    private List<DeviceBean> mDeviceBeans;
    private static final String TAG = "DeviceAdapter";
    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(DeviceBean deviceBean);
    }
    public DeviceAdapter(List<DeviceBean> deviceBeans) {
        mDeviceBeans = deviceBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
           final DeviceBean deviceBean = mDeviceBeans.get(position);
           holder.mName.setText(deviceBean.getPeerIP());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener!=null){
                    mOnClickListener.OnClick(deviceBean);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDeviceBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
        }
    }
}
