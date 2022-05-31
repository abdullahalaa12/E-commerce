package com.example.e_commerce.order;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_commerce.DBHelper;
import com.example.e_commerce.product.Product;
import com.example.e_commerce.product.ProductDAO;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderDAO {
    private DBHelper dbhelper;
    private Context context;

    public OrderDAO(Context context)
    {
        this.context = context;
        dbhelper = new DBHelper(context);
    }

    /*db.execSQL("create table Orders (OrdID integer primary key autoincrement, "+
                " OrdDate text not null ,CustID integer,Address text not null, " +
                "FOREIGN KEY(CustID) REFERENCES Customers (CustID));");*/

    /*db.execSQL("create table OrderDetails (ODID integer primary key, " +
                "OrdID integer, ProID integer, Quantity integer not null, " +
                "FOREIGN KEY(OrdID) REFERENCES Orders (OrdID)," +
                "FOREIGN KEY(ProID) REFERENCES Products (ProID));");*/

    public boolean makeOrder(int custID, String address,
         HashMap<Integer, Integer> selectedcounts, ArrayList<Product>selectedproducts)
    {
        int ordID = insertOrder(custID, address);
        ProductDAO dao=new ProductDAO(context);
        SQLiteDatabase writer = dbhelper.getWritableDatabase();
        for (Product product : selectedproducts)
        {
            ContentValues row = new ContentValues();
            row.put("OrdID", ordID);
            row.put("ProID", product.getId());
            row.put("Quantity", selectedcounts.get(product.getId()));
            writer.insert("OrderDetails", null, row);
            dao.updateQnt(product, selectedcounts.get(product.getId()));
        }
        writer.close();
        return true;
    }

    private int insertOrder(int custID, String address)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        SQLiteDatabase writer = dbhelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("OrdDate", formatter.format(date));
        row.put("CustID", custID);
        row.put("Address", address);
        int id = (int) writer.insert("Orders", null, row);
        writer.close();
        return id;
    }

}
