package com.example.thinksocial;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Query {
    public static class ThinkSocial{
        public static void GenerateTables(Database.DataSource myDB){
            ArrayList<Pair<String, String>> fields = new ArrayList<Pair<String, String>>();
            fields.add(new Pair<String, String>("userId", "INTEGER primary key"));
            fields.add(new Pair<String, String>("Name", "TEXT not null"));
            fields.add(new Pair<String, String>("Gender", "TEXT"));
            fields.add(new Pair<String, String>("Username", "TEXT not null"));
            fields.add(new Pair<String, String>("Password", "TEXT not null"));
            ThinkSocial.CreateTable(myDB, "User", fields);

            fields.clear();
            fields.add(new Pair<String, String>("userId", "INTEGER"));
            fields.add(new Pair<String, String>("textId", "INTEGER"));
            fields.add(new Pair<String, String>("targetId", "INTEGER"));
            fields.add(new Pair<String, String>("Content", "TEXT"));
            fields.add(new Pair<String, String>("PRIMARY KEY", "(userId, textId, targetId)"));
            fields.add(new Pair<String, String>("FOREIGN KEY (userId)", "REFERENCES User(userId)"));
            fields.add(new Pair<String, String>("FOREIGN KEY (targetId)", "REFERENCES User(userId)"));
            ThinkSocial.CreateTable(myDB, "Messenger", fields);

            fields.clear();
            fields.add(new Pair<String, String>("postId", "INTEGER"));
            fields.add(new Pair<String, String>("userId", "INTEGER"));
            fields.add(new Pair<String, String>("Header", "TEXT"));
            fields.add(new Pair<String, String>("Content", "TEXT"));
            fields.add(new Pair<String, String>("ImageSrc", "TEXT"));
            fields.add(new Pair<String, String>("NumbLike", "INTEGER"));
            fields.add(new Pair<String, String>("NumbCmt", "INTEGER"));
            fields.add(new Pair<String, String>("PRIMARY KEY", "(userId, postId)"));
            fields.add(new Pair<String, String>("FOREIGN KEY (userId)", "REFERENCES User(userId)"));
            ThinkSocial.CreateTable(myDB, "Post", fields);

            fields.clear();
            fields.add(new Pair<String, String>("postId", "INTEGER"));
            fields.add(new Pair<String, String>("userId", "INTEGER"));
            fields.add(new Pair<String, String>("cmtId", "INTEGER"));
            fields.add(new Pair<String, String>("Content", "TEXT"));
            fields.add(new Pair<String, String>("PRIMARY KEY", "(userId, postId, cmtId)"));
            fields.add(new Pair<String, String>("FOREIGN KEY (userId)", "REFERENCES User(userId)"));
            fields.add(new Pair<String, String>("FOREIGN KEY (postId)", "REFERENCES Post(postId)"));
            ThinkSocial.CreateTable(myDB, "Comment", fields);
        }

        public static void CreateTable(Database.DataSource myDB, String TableName, ArrayList<Pair<String, String>> fields){
            if (!myDB.createTable(TableName, fields)){
                Log.e("SQL", "Create table "+TableName+" failed!");
                return;
            } else {
                Log.e("SQL", "Create table "+TableName+" successfully!");
                if (TableName=="User") ThinkSocial.Test_AddAccount(myDB);
            }
        }

        private static void Test_DeleteTable(Database.DataSource myDB, String TableName){
            if (myDB.deleteTable(TableName)){
                Log.e("SQL", "delete " + TableName + " successfully!");
            } else {
                Log.e("SQL", "delete " + TableName + " failed!");
            }
        }

        private static void Test_AddPost(Database.DataSource myDB){
            String link = "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F13%2F2015%2F04%2F05%2Ffeatured.jpg&q=60";
            String sql = "insert into Post(userId, postId, ImageSrc, Header, Content, NumbLike, NumbCmt) ";
            sql += "values (1, 1, '"+link+"', 'Hello World', 'This is content of post 1', 0, 0), ";
            link = "https://wallpaperaccess.com/full/27838.jpg";
            sql += "(2, 1, '"+link+"', 'Hi Univers', 'This is content of post 2', 0, 0)";
            myDB.conductSQL(sql);
            Log.e("SQL", "Add 2 posts into DB successfully!");
        }

        private static void Test_AddAccount(Database.DataSource myDB){
            String sql = "insert into User(userId, Name, Gender, Username, Password) ";
            sql += "values (1, 'Nguyen Duy Thinh', 'Male', 'akito66', 'knife513755')";
            myDB.conductSQL(sql);
            Log.e("SQL", "Add information successfully!");
        }
    }

    public static int checkSignIn(Database.DataSource sqlDB, String username, String password) {
        String sql = "Select userId from User where Username='" + username + "' and Password='" + password + "'";
        ArrayList<String> result = sqlDB.getAllValueReturn(sql, 1);
        if (result.size() == 0) return 0;
        else return Integer.parseInt(result.get(0));
    }

    public static boolean addUser(Database.DataSource sqlDB, int id, String fullnamne, String gender, String username, String password) {
        try {
            String sql = "insert into User(userId, Name, Gender, Username, Password) ";
            sql += "values (" + String.valueOf(id) + ", '" + fullnamne + "', '" + gender + "', '" + username + "', '" + password + "')";
            sqlDB.conductSQL(sql);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static int getNextId(Database.DataSource sqlDB, String tableName, String colName){
        String sql = "select distinct " + colName + " from " + tableName + " order by "+ colName +" DESC LIMIT 1";
        ArrayList<String> results = sqlDB.getAllValueReturn(sql, 1);
        if (results.size()==0) return 0;
        int a = -1;
        try{a = Integer.parseInt(results.get(0)) + 1;}
        catch (Exception ex){
            Log.e("getNextId", "Value cannot be converted to int!");
            a = -1;
        }
        return a;
    }

    public static boolean checkDuplicateInfor(Database.DataSource sqlDB, String victim, String tableName, String colName){
        String sql = "select " + colName + " from " + tableName + " where " + colName +"='"+ victim +"'";
        ArrayList<String> results = sqlDB.getAllValueReturn(sql, 1);
        if (results.size()==0) return false;
        else return true;
    }

    public static ArrayList<Post> getAllPosts(Database.DataSource sqlDB){
        int cols = 6;
        ArrayList<Post> postList = new ArrayList<>();
        String sql = "select u.Username, p.ImageSrc, p.Header, p.Content, p.NumbLike, p.NumbCmt ";
        sql += "from Post as p inner join User as u on u.userId = p.userId";
        ArrayList<String> results = sqlDB.getAllValueReturn(sql, cols);

        for (int i=0; i<results.size(); i+=cols){
            String username = results.get(i);
            String imgSrc = results.get(i+1);
            String header = results.get(i+2);
            String content = results.get(i+3);
            int like = Integer.parseInt(results.get(i+4));
            int cmt = Integer.parseInt(results.get(i+5));

            postList.add(new Post(1, imgSrc, username, header, content, like, cmt));
        }
        return postList;
    }

    public static String getUserInfor(Database.DataSource sqlDB, int id, String infor){
        String sql = "select "+infor+" from User where userId="+id;
        ArrayList<String> result = sqlDB.getAllValueReturn(sql, 1);
        return String.valueOf(result.get(0));
    }

    public static boolean createPost(Database.DataSource sqlDB, int UserId, String header, String content, String imgSrc){
        int nextId = Query.getNextId(sqlDB, "Post", "postId");
        String sql = "insert into Post(postId, userId, Header, Content, ImgSrc, NumbLike, NumbCmt) ";
        sql += "values ("+String.valueOf(nextId)+", "+String.valueOf(UserId)+", '"+header+"', '"+content+"', '"+imgSrc+"', 0, 0)";
        try{sqlDB.conductSQL(sql);}
        catch (Exception ex) {return false;}
        return true;
    }
}
