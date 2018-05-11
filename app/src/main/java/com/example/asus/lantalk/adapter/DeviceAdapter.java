package com.example.asus.lantalk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.entity.SocketBean;

import java.util.List;

/**
 * Created by asus on 18-5-9.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{
    private List<SocketBean> mSocketBeans;
    private static final String TAG = "DeviceAdapter";
    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(SocketBean socketBean);
    }
    public DeviceAdapter(List<SocketBean> socketBeans) {
        mSocketBeans = socketBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
           final SocketBean socketBean = mSocketBeans.get(position);
           holder.mName.setText(socketBean.getPeerIP());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener!=null){
                    mOnClickListener.OnClick(socketBean);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSocketBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
        }
    }
}
