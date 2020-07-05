package com.example.marvelapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DataBaseName="Profile.db";
    public static final String table_name="table_name";

    public DataBaseHelper(Context context) {
        super(context,DataBaseName,null,1);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table " + table_name + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, status TEXT,mobile_number TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert_data_values(String status,String mobile_number)
    {

        String col_status="status",col_mobile_number="mobile_number";
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+table_name);
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_status,status);
        contentValues.put(col_mobile_number,mobile_number);
        long result=sqLiteDatabase.insert(table_name,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor get_All_Data()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from "+table_name,null);
        return res;
    }

    public void delete_all_data()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+table_name);
    }


}
