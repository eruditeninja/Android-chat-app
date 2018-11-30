package com.example.neeraj.chatit;

public class User {


    private String Image ;
    private String Name;

    public User() {
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

    public User(String image, String name, String status) {
        Image = image;
        Name = name;
        Status = status;
    }
}
