package com.example.neeraj.chatit;

import android.content.Context;
import android.media.Image;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendFragmentViewHolder extends RecyclerView.ViewHolder {


    public View mView;
    public TextView mTextName;
    public TextView mTextSatus;
    public CircleImageView mImage;
    public ImageView mOnline;

    public FriendFragmentViewHolder(View itemView) {
        super(itemView);
        mView =itemView;
        mTextName = itemView.findViewById(R.id.single_layout_name);
        mTextSatus = itemView.findViewById(R.id.single_layout_status);
        mImage = itemView.findViewById(R.id.single_layout_image);
        mOnline = itemView.findViewById(R.id.user_single_layout_online);
    }

}
