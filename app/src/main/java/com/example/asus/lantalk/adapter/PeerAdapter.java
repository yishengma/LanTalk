package com.example.asus.lantalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.lantalk.R;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.utils.ProfilePicturePickUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.asus.lantalk.constant.Constant.sEMPTY;

import static com.example.asus.lantalk.constant.Constant.sITEM;

/**
 * Created by asus on 18-5-9.
 */

public class PeerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SocketBean> mSocketBeans;
    private static final String TAG = "PeerAdapter";
    private OnClickListener mOnClickListener;
    private Context mContext;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void OnClick(SocketBean socketBean);
    }

    public PeerAdapter(List<SocketBean> socketBeans, Context context) {
        mSocketBeans = socketBeans;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==sEMPTY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(view);
        }else if (viewType==sITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peer, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            final SocketBean socketBean = mSocketBeans.get(position);
            ((ViewHolder)holder).mName.setText(socketBean.getSendName());
            Glide.with(mContext).load(ProfilePicturePickUtil.getImageDrawable(socketBean.getProfilePicture())).into(((ViewHolder)holder).mProfileImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnClickListener != null) {
                        mOnClickListener.OnClick(socketBean);
                    }
                }
            });
            ((ViewHolder)holder).mMsgView.setText(socketBean.getMessage());
        }
    }




    @Override
    public int getItemCount() {
        return mSocketBeans.size()==0?1 :mSocketBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSocketBeans.size()==0? sEMPTY : sITEM;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        CircleImageView mProfileImageView;
        TextView mMsgView;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mProfileImageView = itemView.findViewById(R.id.iv_profile_picture);
            mMsgView  =itemView.findViewById(R.id.tv_msg_newly);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder{
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
