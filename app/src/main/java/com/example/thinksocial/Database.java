package com.example.thinksocial;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;


//public class Pair<L,R> {
//    private L l;
//    private R r;
//    public Pair(L l, R r){
//        this.l = l;
//        this.r = r;
//    }
//    public L getL(){ return l; }
//    public R getR(){ return r; }
//    public void setL(L l){ this.l = l; }
//    public void setR(R r){ this.r = r; }
//}

public class Database{
    public static Database.DataSource OpenDB(Context context, String dbName){
        File file = new File(dbName);
        if (file.exists()) {
            Log.e("SQL", "The DB "+dbName+" already exist!");
            return null;
        }
        Database.DataSource myDB = new Database.DataSource(context, dbName);
        myDB.open();
        return myDB;
    }

    public static final int DB_VERSION = 1;
    public static String DB_NAME = "";

    public static class DBConnection extends SQLiteOpenHelper {
        public DBConnection(Context context, String db_name) {
            super(context, db_name, null, DB_VERSION);
            DB_NAME = db_name;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }


        public void deleteAllTable(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            List<String> tables = new ArrayList<>();

            while (c.moveToNext()) {
                tables.add(c.getString(0));
            }

            for (String table : tables) {
                String dropQuery = "DROP TABLE IF EXISTS " + table;
                db.execSQL(dropQuery);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DBConnection.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            deleteAllTable(db);
            onCreate(db);
        }
    }

    public static class DataSource{
        private SQLiteDatabase sqlDB;
        private Database.DBConnection DBConnection;

        public DataSource(Context context, String nameDB) {
            DBConnection = new DBConnection(context, nameDB);
        }

        public void open() throws SQLException {
            sqlDB = DBConnection.getWritableDatabase();
        }

        public void close() {
            DBConnection.close();
        }

        public boolean checkTableExist(String tableName){
            Cursor c = sqlDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+tableName+"';", null);
            if (c==null || c.getCount()==0) return false;
            else return true;
        }

        public boolean deleteTable(String TableName){
            Cursor c = sqlDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+TableName+"';", null);
            if (c==null || c.getCount()==0) return false;
            String sql = "DROP TABLE IF EXISTS " + TableName;
            sqlDB.execSQL(sql);
            return true;
        }

        public void deleteAllTables(){
            DBConnection.deleteAllTable(sqlDB);
        }

        public boolean createTable(String TableName, ArrayList<Pair<String, String>> fields){
            if (checkTableExist(TableName)){
                return false;
            }
            String sql = "create table " + TableName + "( ";
            for (int i=0;i< fields.size(); i++) {
                sql += fields.get(i).getL() + " " + fields.get(i).getR() + ", ";
            }
            sql += ");";
            StringBuilder sb = new StringBuilder(sql);
            sb  = sb.deleteCharAt(sql.length()-4);
            sql = sb.toString();
            sqlDB.execSQL(sql);
            return true;
        }

        public boolean conductSQL(String sql){
            try {sqlDB.execSQL(sql);}
            catch(Exception exception) {return false;}
            return true;
        }

        public ArrayList<String> getAllValueReturn(String sql, int cols){
            ArrayList<String> results = new ArrayList<>();
            Cursor cursor = sqlDB.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                for (int i=0; i<cols; i++)
                    results.add(cursor.getString(i));
            }
            return results;
        }
    }
}
