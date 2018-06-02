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
import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.ui.ImageDialogFragment;
import com.example.asus.lantalk.utils.ProfilePicturePickUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 信息 RecyclerView 的 Adapter
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SocketBean> mBeans;
    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(String path);
    }

    public MessageAdapter(List<SocketBean> beans) {
        mBeans = beans;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //聊天界面
        // 信息的ViewHolder
        // 我的消息
        if (viewType == Constant.MINEMSG){
           return  new MessageMine(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_mine,parent,false));
        //对等方的消息
        }else if (viewType == Constant.PEERMSG){
            return  new MessagePeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk_peer,parent,false));
        //我的文件
        }else if (viewType==Constant.MINEFILE){
            return  new FileMine(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_mine,parent,false));
        //对等方的文件
        }else if (viewType==Constant.PEERFILE){
            return  new FilePeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_peer,parent,false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SocketBean bean = mBeans.get(position);
        //我的消息
        if (holder instanceof MessageMine) {
            ((MessageMine)holder).mNameTextView.setText(bean.getSendName());
            ((MessageMine)holder).mMsgTextView.setText(bean.getMessage());
            //加载头像
            Glide.with(App.getsContext()).load(ProfilePicturePickUtil.getImageDrawable(bean.getProfilePicture())).into(((MessageMine)holder).mCircleImageView);
        //对等方的消息
        }else if (holder instanceof  MessagePeer){
            ((MessagePeer)holder).mNameTextView.setText(bean.getSendName());
            ((MessagePeer)holder).mMsgTextView.setText(bean.getMessage());
            Glide.with(App.getsContext()).load(ProfilePicturePickUtil.getImageDrawable(bean.getProfilePicture())).into(((MessagePeer)holder).mCircleImageView);
          //我的文件
        }else if (holder instanceof FileMine){
            ((FileMine)holder).mNameTextView.setText(bean.getSendName());
            Glide.with(App.getsContext()).load(bean.getFilePath()).into(((FileMine)holder).mFileImageView);
            ((FileMine)holder).mFileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (mOnClickListener!=null){
                       mOnClickListener.OnClick(bean.getFilePath());
                   }

                }
            });
            Glide.with(App.getsContext()).load(ProfilePicturePickUtil.getImageDrawable(bean.getProfilePicture())).into(((FileMine)holder).mCircleImageView);
            //对等方的文件
        }else if (holder instanceof FilePeer){
            ((FilePeer)holder).mNameTextView.setText(bean.getSendName());
            Glide.with(App.getsContext()).load(bean.getFilePath()).into(((FilePeer)holder).mFileImageView);
            ((FilePeer)holder).mFileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  if (mOnClickListener!=null){
                      mOnClickListener.OnClick(bean.getFilePath());
                  }
                }
            });
            Glide.with(App.getsContext()).load(ProfilePicturePickUtil.getImageDrawable(bean.getProfilePicture())).into(((FilePeer)holder).mCircleImageView);

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

        TextView mMsgTextView;
        TextView mNameTextView;
        CircleImageView mCircleImageView;
        public MessageMine(View itemView) {
            super(itemView);
            mMsgTextView = itemView.findViewById(R.id.tv_msg_content);
            mNameTextView = itemView.findViewById(R.id.tv_mine_name);
            mCircleImageView = itemView.findViewById(R.id.iv_mine_picture);
        }
    }



    class MessagePeer extends RecyclerView.ViewHolder{

        TextView mMsgTextView;
        TextView mNameTextView;
        CircleImageView mCircleImageView;
        public MessagePeer(View itemView) {
            super(itemView);
            mMsgTextView = itemView.findViewById(R.id.tv_msg_content);
            mNameTextView = itemView.findViewById(R.id.tv_peer_name);
            mCircleImageView = itemView.findViewById(R.id.iv_peer_picture);
        }
    }


    class FileMine extends RecyclerView.ViewHolder{

        ImageView mFileImageView;
        TextView mNameTextView;
        CircleImageView mCircleImageView;
        public FileMine(View itemView) {
            super(itemView);
            mFileImageView = itemView.findViewById(R.id.iv_photo);
            mNameTextView = itemView.findViewById(R.id.tv_mine_name);
            mCircleImageView = itemView.findViewById(R.id.iv_mine_picture);
        }
    }



    class FilePeer extends RecyclerView.ViewHolder{

        ImageView mFileImageView;
        TextView mNameTextView;
        CircleImageView mCircleImageView;
        public FilePeer(View itemView) {
            super(itemView);
            mFileImageView = itemView.findViewById(R.id.iv_photo);
            mNameTextView = itemView.findViewById(R.id.tv_peer_name);
            mCircleImageView = itemView.findViewById(R.id.iv_peer_picture);
        }
    }
}
