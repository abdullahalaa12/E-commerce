package com.example.e_commerce.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_commerce.DBHelper;
import com.example.e_commerce.category.Category;
import com.example.e_commerce.customer.Customer;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductDAO {
    private DBHelper dbhelper;
    private Context context;

    public ProductDAO(Context context)
    {
        this.context = context;
        dbhelper = new DBHelper(context);
    }


    public boolean addProduct(String name, int price, int quantity, ImageView image, int categoryid)
    {
        ContentValues row = new ContentValues();
        row.put("ProName", name);
        row.put("Price", price);
        row.put("Quantity", quantity);
        row.put("image", imageByte(image));
        row.put("CatID", categoryid);

        SQLiteDatabase writer = dbhelper.getWritableDatabase();
        try {
            writer.insert("Products", null, row);
            writer.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private byte[] imageByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public ArrayList<Product> getProducts()
    {
        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] columns = {"ProID", "ProName", "Price", "Quantity", "image", "CatID"};
        Cursor cursor = reader.query("Products", columns, null, null, null, null, null);
        if(cursor!=null)
            cursor.moveToFirst();
        reader.close();

        ArrayList<Product> ret=new ArrayList<Product>();
        while(!cursor.isAfterLast())
        {
            if(cursor.getInt(3)>0) {
                ret.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                        cursor.getInt(3), cursor.getBlob(4), cursor.getInt(5)));
            }
            cursor.moveToNext();
        }
        return ret;
    }

    public ArrayList<Product> getProducts(int catID)
    {
        SQLiteDatabase reader = dbhelper.getReadableDatabase();
        String[] arg={Integer.toString(catID)};
        Cursor cursor=reader.rawQuery
                ("Select ProID, ProName, Price, Quantity, image from Products " +
                        "where CatID = ?",arg);
        if(cursor!=null)cursor.moveToFirst();
        reader.close();

        ArrayList<Product> ret=new ArrayList<Product>();
        while(!cursor.isAfterLast())
        {
            if(cursor.getInt(3)>0) {
                ret.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                        cursor.getInt(3), cursor.getBlob(4), catID));
            }
            cursor.moveToNext();
        }
        return ret;
    }

    /*db.execSQL("create table Products (ProID integer primary key autoincrement, "+
                " ProName text not null ,Price integer not null, Quantity integer not null, image blob, " +
                "CatID integer, FOREIGN KEY(CatID) REFERENCES Categories (CatID));");*/

    public void updateQnt(Product product, int qntbought) {
        SQLiteDatabase writer = dbhelper.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put("Quantity", product.getQuantity()-qntbought);
        writer.update("Products",row,"ProID = ?",new String[]{Integer.toString(product.getId())});
        writer.close();
    }

}
