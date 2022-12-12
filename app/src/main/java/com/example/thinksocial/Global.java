package com.example.thinksocial;

import android.app.Application;

//((Global)this.getApplication()).setMyDB(myDB)

public class Global extends Application {
    public Database.DataSource myDB;
    public void setMyDB(Database.DataSource paraDB){myDB = paraDB;}
    Database.DataSource getMyDB() {return myDB;}

    public int IdCurrentUser;
    public void setIdCurrentUser(int user){IdCurrentUser=user;}
    public int getIdCurrentUser(){return IdCurrentUser;}
}
