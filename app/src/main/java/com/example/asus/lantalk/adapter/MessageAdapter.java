package com.example.asus.lantalk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;

import java.util.List;

/**
 * Created by asus on 18-5-11.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SocketBean> mBeans;

    public MessageAdapter(List<SocketBean> beans) {
        mBeans = beans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.MINE){
           return  new MessageMine(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_mine,parent,false));

        }else if (viewType == Constant.PEER){
            return  new MessagePeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_peer,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SocketBean bean = mBeans.get(position);
        if (holder instanceof MessageMine) {
            ((MessageMine)holder).mNameTextView.setText(bean.getName());
            ((MessageMine)holder).mMsgTextView.setText(bean.getMessage());
            ((MessageMine)holder).mTimeTextView.setText(bean.getTime());
        }else if (holder instanceof  MessagePeer){
            ((MessagePeer)holder).mNameTextView.setText(bean.getName());
            ((MessagePeer)holder).mMsgTextView.setText(bean.getMessage());
            ((MessagePeer)holder).mTimeTextView.setText(bean.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mBeans.get(position).getType();
    }

    class MessageMine extends RecyclerView.ViewHolder{
        TextView mTimeTextView;
        TextView mMsgTextView;
        TextView mNameTextView;
        public MessageMine(View itemView) {
            super(itemView);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
            mMsgTextView = itemView.findViewById(R.id.tv_msg_content);
            mNameTextView = itemView.findViewById(R.id.tv_mine_name);
        }
    }



    class MessagePeer extends RecyclerView.ViewHolder{
        TextView mTimeTextView;
        TextView mMsgTextView;
        TextView mNameTextView;
        public MessagePeer(View itemView) {
            super(itemView);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
            mMsgTextView = itemView.findViewById(R.id.tv_msg_content);
            mNameTextView = itemView.findViewById(R.id.tv_peer_name);
        }
    }
}
