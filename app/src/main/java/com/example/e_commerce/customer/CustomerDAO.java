package com.example.e_commerce.customer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.e_commerce.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerDAO
{
    private DBHelper dbhelper;
    private Context context;

    public CustomerDAO(Context context)
    {
        this.context = context;
        dbhelper = new DBHelper(context);
    }


    public boolean Signup(String name, String email, String password, String gender, String BOD, String job)
    {
        ContentValues row = new ContentValues();
        row.put("CutName", name);
        row.put("Email", email);
        row.put("Password", password);
        row.put("Gender", gender);
        row.put("Birthdate", BOD);
        row.put("Job", job);

        SQLiteDatabase writer = dbhelper.getWritableDatabase();
        try {
            writer.insert("Customers", null, row);
            writer.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean CheckEmail(String Email)
    {
        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] arg={Email};
        Cursor cursor= reader.rawQuery
                ("Select CustID from Customers where Email like ?",arg);
        if(cursor!=null)cursor.moveToFirst();

        reader.close();
        if(cursor.isAfterLast())
            return true;
        else
            return false;
    }


    /*db.execSQL("create table Customers (CustID integer primary key autoincrement, "+
               " CutName text not null , Email text not null,Password text not null, " +
               " Gender text not null, Birthdate text not null, Job text not null);");*/

    public Customer Login(String Email, String Password) throws Exception
    {

        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] arg={Email, Password};
        Cursor cursor=reader.rawQuery
                ("Select CustID, CutName, Gender, Birthdate, Job from Customers " +
                        "where Email = ? and Password = ?",arg);
        if(cursor!=null)cursor.moveToFirst();
        reader.close();

        if(cursor.isAfterLast())
            return null;

        String sdate = cursor.getString(3);
        Date bod = new SimpleDateFormat("MMM dd yyyy").parse(sdate);
        Customer c = new Customer(cursor.getInt(0),cursor.getString(1),Email, Password,
                cursor.getString(2), bod, cursor.getString(4));
        return c;
    }

    public Customer Login(int id) throws Exception
    {

        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] arg={Integer.toString(id)};
        Cursor cursor=reader.rawQuery
                ("Select CutName, Email, Password, Gender, Birthdate, Job from Customers " +
                        "where CustID = ?",arg);
        if(cursor!=null)cursor.moveToFirst();
        reader.close();

        if(cursor.isAfterLast())
            return null;

        String sdate = cursor.getString(4);
        Date bod = new SimpleDateFormat("MMM dd yyyy").parse(sdate);
        Customer c = new Customer(id,cursor.getString(0),cursor.getString(1),
                cursor.getString(2), cursor.getString(3), bod, cursor.getString(5));
        return c;
    }

    public String getPassword(String email)
    {
        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] arg={email};
        Cursor cursor=reader.rawQuery
                ("Select Password from Customers " +
                        "where Email = ?",arg);
        if(cursor!=null)cursor.moveToFirst();
        reader.close();

        if(cursor.isAfterLast())
            return null;

        String password=cursor.getString(0);
        return password;

    }

    public String getEmail(int custID)
    {
        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] arg={Integer.toString(custID)};
        Cursor cursor=reader.rawQuery
                ("Select Email from Customers " +
                        "where CustID = ?",arg);
        if(cursor!=null)cursor.moveToFirst();
        reader.close();

        if(cursor.isAfterLast())
            return null;

        String email=cursor.getString(0);
        return email;

    }

}
