package com.example.e_commerce.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.e_commerce.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainAdapter extends ArrayAdapter {

    private ArrayList<Product> products;
    private ArrayList<Product> selectedproducts;
    private HashMap<Integer, Integer> selectedcount;


    public MainAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.products = objects;
        selectedcount =new HashMap<>();
        selectedproducts=new ArrayList<Product>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);

        Product product = products.get(position);
        TextView name = convertView.findViewById(R.id.productname);
        TextView price = convertView.findViewById(R.id.productprice);
        TextView quantity = convertView.findViewById(R.id.productquantity);
        ImageView image = convertView.findViewById(R.id.productimage);
        ImageView cartadd = convertView.findViewById(R.id.addtocart);


        name.setText(product.getName());
        price.setText(Integer.toString(product.getPrice()) + " $");
        quantity.setText(Integer.toString(product.getQuantity()) + " Left");

        if(product.getImage()!=null) {
            byte[] bytes = product.getImage();
            Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            image.setImageBitmap(bit);
        }

        cartadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=product.getId();
                if(!selectedcount.containsKey(id)) {
                    selectedproducts.add(product);
                    selectedcount.put(id, 1);
                }
                Toast.makeText(getContext(), product.getName()+" Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public HashMap<Integer, Integer> getSelectedcount() {
        return selectedcount;
    }
    public ArrayList<Product> getSelectedproducts() {
        return selectedproducts;
    }
}
