package com.example.e_commerce.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_commerce.DBHelper;

import java.util.ArrayList;

public class CategoryDAO {
    private DBHelper dbhelper;
    private Context context;

    public CategoryDAO(Context context)
    {
        this.context = context;
        dbhelper = new DBHelper(context);
    }

    public ArrayList<Category> getCategories()
    {
        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] columns = {"CatID", "CatName"};
        Cursor cursor = reader.query("Categories", columns, null, null, null, null, null);
        if(cursor!=null)
            cursor.moveToFirst();
        reader.close();

        ArrayList<Category> ret=new ArrayList<Category>();
        while(!cursor.isAfterLast())
        {
            ret.add(new Category(cursor.getInt(0),cursor.getString(1)));
            cursor.moveToNext();
        }
        return ret;
    }

    public void insertCategories(String[] data) {
        SQLiteDatabase writer = dbhelper.getWritableDatabase();
        for (int i = 0; i < data.length; i++) {
            ContentValues row = new ContentValues();
            row.put("CatName", data[i]);
            writer.insert("Categories", null, row);
        }
        writer.close();

    }
}
