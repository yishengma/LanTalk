package com.example.asus.lantalk.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.lantalk.R;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.ui.ImageDialogFragment;

import java.util.List;

/**
 * Created by asus on 18-5-11.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SocketBean> mBeans;
    private Context mContext;
    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(String path);
    }

    public MessageAdapter(List<SocketBean> beans,Context context) {
        mBeans = beans;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.MINEMSG){
           return  new MessageMine(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_mine,parent,false));

        }else if (viewType == Constant.PEERMSG){
            return  new MessagePeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_peer,parent,false));
        }else if (viewType==Constant.MINEFILE){
            return  new FileMine(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_mine,parent,false));

        }else if (viewType==Constant.PEERFILE){
            return  new FilePeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_peer,parent,false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SocketBean bean = mBeans.get(position);
        if (holder instanceof MessageMine) {
            ((MessageMine)holder).mNameTextView.setText(bean.getSendName());
            ((MessageMine)holder).mMsgTextView.setText(bean.getMessage());
            ((MessageMine)holder).mTimeTextView.setText(bean.getTime());
        }else if (holder instanceof  MessagePeer){
            ((MessagePeer)holder).mNameTextView.setText(bean.getSendName());
            ((MessagePeer)holder).mMsgTextView.setText(bean.getMessage());
            ((MessagePeer)holder).mTimeTextView.setText(bean.getTime());
        }else if (holder instanceof FileMine){
            ((FileMine)holder).mNameTextView.setText(bean.getSendName());
            Glide.with(mContext).load(bean.getFilePath()).into(((FileMine)holder).mFileImageView);
            ((FileMine)holder).mFileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (mOnClickListener!=null){
                       mOnClickListener.OnClick(bean.getFilePath());
                   }

                }
            });
            ((FileMine)holder).mTimeTextView.setText(bean.getTime());
        }else if (holder instanceof FilePeer){
            ((FilePeer)holder).mNameTextView.setText(bean.getSendName());
            Glide.with(mContext).load(bean.getFilePath()).into(((FilePeer)holder).mFileImageView);
            ((FilePeer)holder).mFileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  if (mOnClickListener!=null){
                      mOnClickListener.OnClick(bean.getFilePath());
                  }
                }
            });
            ((FilePeer)holder).mTimeTextView.setText(bean.getTime());
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


    class FileMine extends RecyclerView.ViewHolder{
        TextView mTimeTextView;
        ImageView mFileImageView;
        TextView mNameTextView;
        public FileMine(View itemView) {
            super(itemView);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
            mFileImageView = itemView.findViewById(R.id.iv_photo);
            mNameTextView = itemView.findViewById(R.id.tv_mine_name);
        }
    }



    class FilePeer extends RecyclerView.ViewHolder{
        TextView mTimeTextView;
        ImageView mFileImageView;
        TextView mNameTextView;
        public FilePeer(View itemView) {
            super(itemView);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
            mFileImageView = itemView.findViewById(R.id.iv_photo);
            mNameTextView = itemView.findViewById(R.id.tv_peer_name);
        }
    }
}
