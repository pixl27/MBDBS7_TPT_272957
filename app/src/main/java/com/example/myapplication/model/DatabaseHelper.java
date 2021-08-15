package com.example.myapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
private static final String TAG = "DatabaseHelper";
private static final String TABLE_NAME = "token_table";
private static final String col1 = "token";

public DatabaseHelper (Context context){
    super(context,TABLE_NAME,null,1);
}
    @Override
    public void onCreate(SQLiteDatabase db) {
    String creataTable = "CREATE TABLE " + TABLE_NAME + "(" + col1 + " TEXT)";


        db.execSQL(creataTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

onCreate(db);
    }
    public boolean insertToken (String token){
    SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1,token);

        long result = db.insert(TABLE_NAME,null,contentValues);
    if (result == -1){
        return false;
    }
    else
    {
        return true;

    }

}   public void deletetablematchs(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Matchs");

    }
    public void deletetableuser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS User");

    }
    public void initializeoffline(){
        SQLiteDatabase db = this.getWritableDatabase();
        String createofflinetable = "CREATE TABLE IF NOT EXISTS Matchs ( nomTeam1  TEXT ,nomTeam2 TEXT,odds1 TEXT,odds2 TEXT,logo1 TEXT, logo2 TEXT)";
        db.execSQL(createofflinetable);
    }
    public void initializeteamoffline(){
        SQLiteDatabase db = this.getWritableDatabase();
        String createofflinetable = "CREATE TABLE IF NOT EXISTS Teams ( name  TEXT ,tag TEXT,logo_url TEXT)";
        db.execSQL(createofflinetable);
    }
    public void initializetoken(){
        SQLiteDatabase db = this.getWritableDatabase();
        String creataTable = "CREATE TABLE " + TABLE_NAME + "(" + col1 + " TEXT)";


        db.execSQL(creataTable);
    }
    public void initializeiduser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String createofflinetable = "CREATE TABLE IF NOT EXISTS User ( idUser  TEXT,solde TEXT)";
        db.execSQL(createofflinetable);
    }
    public void insertIdUser (String iduser,String solde){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idUser",iduser);
        contentValues.put("solde",solde);
        db.insert("User",null,contentValues);


    }
    public void insertofflinematch (List<Match> listmatchs){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i = 0;i<listmatchs.size();i++){
            contentValues.put("nomTeam1",listmatchs.get(i).getNomTeam1());
            contentValues.put("nomTeam2",listmatchs.get(i).getNomTeam2());
            contentValues.put("odds1",listmatchs.get(i).getOdds1().toString());
            contentValues.put("odds2",listmatchs.get(i).getOdds2().toString());
            contentValues.put("logo1", R.drawable.nointernet);
            contentValues.put("logo2",R.drawable.nointernet);
           db.insert("Matchs",null,contentValues);

        }


    }

    public void insertofflineteam (List<Team> listteam){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i = 0;i<listteam.size();i++){
            contentValues.put("name",listteam.get(i).getName());
            contentValues.put("tag",listteam.get(i).getTag());
            contentValues.put("logo_url", R.drawable.nointernet);

            db.insert("Teams",null,contentValues);

        }


    }


public void deleteToken(){
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "DELETE FROM " + TABLE_NAME;
    db.execSQL(query);
}
    public void deletematch(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM Matchs";
        db.execSQL(query);
    }
    public void deleteuser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM User";
        db.execSQL(query);
    }
    public void deleteteams(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM Teams";
        db.execSQL(query);
    }

    public String getToken(){
        SQLiteDatabase dblite = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = dblite.rawQuery(query,null);
        String datastring      = null;

        if (data.moveToFirst()) {
            do {
                datastring =  data.getString(0);
            } while (data.moveToNext());
        }
        data.close();
        return datastring ;
    }
    public String [] getIdUser(){
        SQLiteDatabase dblite = this.getReadableDatabase();
        String query = "SELECT * FROM User";
        Cursor data = dblite.rawQuery(query,null);
        String [] datastring      =new String[100];

        if (data.moveToFirst()) {
            do {
                datastring[0] =  data.getString(0);
                datastring[1] =  data.getString(1);
            } while (data.moveToNext());
        }
        data.close();
        return datastring ;
    }

    public List<Match> getOfflineMatch(){
        SQLiteDatabase dblite = this.getReadableDatabase();
        String query = "SELECT * FROM Matchs";
        Cursor data = dblite.rawQuery(query,null);
        String datastring      = null;
        List<Match> listmatches = new ArrayList<>() ;

        if (data.moveToFirst()) {
            do {
                Match instance = new Match();
                instance.setNomTeam1(data.getString(0));
                instance.setNomTeam2(data.getString(1));
                instance.setOdds1(Double.parseDouble(data.getString(2)));
                instance.setOdds2(Double.parseDouble(data.getString(3)));
                instance.setLogo1(data.getString(4));
                instance.setLogo2(data.getString(5));
                listmatches.add(instance);
            } while (data.moveToNext());
        }
        data.close();
        return listmatches ;
    }
    public List<Team> getOfflineTeam(){
        SQLiteDatabase dblite = this.getReadableDatabase();
        String query = "SELECT * FROM Teams";
        Cursor data = dblite.rawQuery(query,null);
        List<Team> listteam = new ArrayList<>() ;

        if (data.moveToFirst()) {
            do {
                Team instance = new Team();
                instance.setName(data.getString(0));
                instance.setTag(data.getString(1));
                instance.setLogo_url(data.getString(2));
                listteam.add(instance);
            } while (data.moveToNext());
        }
        data.close();
        return listteam ;
    }
}
