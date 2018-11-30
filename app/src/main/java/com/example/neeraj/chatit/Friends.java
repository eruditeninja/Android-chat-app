package com.example.neeraj.chatit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friends {

    private String Image ;
    private String Name;

    public Friends() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private  String Status;

    public Friends(String image, String name, String status) {
        Image = image;
        Name = name;
        Status = status;
    }

}
