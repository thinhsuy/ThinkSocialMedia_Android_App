package com.example.thinksocial;

import android.util.Log;

public class Post {
    public int Id;
    public String ImgLink;
    public String Username;
    public String Header;
    public String Content;
    public int NumbLike;
    public int NumbCmt;

    public Post(int id, String imgLink, String username, String header, String content, int like, int cmt){
        Id = id;
        ImgLink = imgLink;
        Username = username;
        Header = header;
        Content = content;
        NumbCmt = like;
        NumbLike = cmt;
    }
    public int getId() {return Id;}
    public String getImgLink() {return ImgLink;}
    public String getUsername() {return Username;}
    public String getHeader() {return Header;}
    public String getContent() {return Content;}
    public int getNumbLike() {return NumbLike;}
    public int getNumbCmt() {return NumbCmt;}

    public void LogOut(){
        Log.e("Id", String.valueOf(getId()));
        Log.e("Username", getUsername());
        Log.e("ImgLink", getImgLink());
        Log.e("Header", getHeader());
        Log.e("Content", getContent());
        Log.e("Like", String.valueOf(getNumbLike()));
        Log.e("Cmt", String.valueOf(getNumbCmt()));
    }
}
