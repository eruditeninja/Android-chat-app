package com.example.neeraj.chatit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    public TextView mTextName;
    public TextView mTextSatus;
    public CircleImageView mImage;

    public ViewHolder(View itemView) {
        super(itemView);
        mView =itemView;
        mTextName = itemView.findViewById(R.id.single_layout_name);
        mTextSatus = itemView.findViewById(R.id.single_layout_status);
        mImage = itemView.findViewById(R.id.single_layout_image);
    }

}
