package com.example.e_commerce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    private static String databaseName = "EcommerceDatabase";
    SQLiteDatabase EcommerceDB;

    public DBHelper(Context context)
    {
        super(context, databaseName, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table Customers (CustID integer primary key autoincrement, "+
                " CutName text not null , Email text not null,Password text not null, " +
                " Gender text not null, Birthdate text not null, Job text not null);");

        db.execSQL("create table Categories (CatID integer primary key autoincrement, CatName text not null );");

        db.execSQL("create table Orders (OrdID integer primary key autoincrement, "+
                " OrdDate text not null ,CustID integer,Address text not null, " +
                "FOREIGN KEY(CustID) REFERENCES Customers (CustID));");

        db.execSQL("create table Products (ProID integer primary key autoincrement, "+
                " ProName text not null ,Price integer not null, Quantity integer not null, image blob, " +
                "CatID integer, FOREIGN KEY(CatID) REFERENCES Categories (CatID));");

        db.execSQL("create table OrderDetails (ODID integer primary key, " +
                "OrdID integer, ProID integer, Quantity integer not null, " +
                "FOREIGN KEY(OrdID) REFERENCES Orders (OrdID)," +
                "FOREIGN KEY(ProID) REFERENCES Products (ProID));");


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists OrderDetails");
        db.execSQL("drop table if exists Products");
        db.execSQL("drop table if exists Orders");
        db.execSQL("drop table if exists Categories");
        db.execSQL("drop table if exists Customers");
        onCreate(db);
    }
}