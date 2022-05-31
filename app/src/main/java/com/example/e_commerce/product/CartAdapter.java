package com.example.e_commerce.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.e_commerce.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends ArrayAdapter {

    private ArrayList<Product> products;
    private HashMap<Integer, Integer> counts;
    private TextView totalview;

    public CartAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects, HashMap<Integer, Integer> counts, TextView totalview) {
        super(context, resource, objects);
        this.products = objects;
        this.counts=counts;
        this.totalview=totalview;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartproduct, parent, false);

        Product product = products.get(position);
        TextView name = convertView.findViewById(R.id.cartpname);
        TextView price = convertView.findViewById(R.id.cartpprice);
        TextView quantity = convertView.findViewById(R.id.cartquantity);
        ImageView image = convertView.findViewById(R.id.cartpimage);
        Button cartadd = convertView.findViewById(R.id.cartqntadd);
        Button cartsub = convertView.findViewById(R.id.cartqntsub);
        ImageView remove = convertView.findViewById(R.id.cartpremove);

        totalview.setText(Integer.toString(getTotal()));
        name.setText(product.getName());
        price.setText(Integer.toString(product.getPrice() * counts.get(product.getId())) + " $");
        quantity.setText(Integer.toString(counts.get(product.getId())));

        if(product.getImage()!=null) {
            byte[] bytes = product.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            image.setImageBitmap(bmp);
        }

        cartadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increment qnt
                //edit price
                if(counts.get(product.getId())<product.getQuantity())
                {
                    counts.put(product.getId(),counts.get(product.getId()) + 1);
                    price.setText(Integer.toString(counts.get(product.getId())*product.getPrice()));
                    quantity.setText(Integer.toString(counts.get(product.getId())));
                    totalview.setText(Integer.toString(getTotal()));
                }
                else
                    Toast.makeText(getContext(), "No More " + product.getName() + " In Stock !", Toast.LENGTH_SHORT).show();
            }
        });

        cartsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //decrement qnt
                //edit price
                if(counts.get(product.getId())>1)
                {
                    counts.put(product.getId(),counts.get(product.getId()) - 1);
                    price.setText(Integer.toString(counts.get(product.getId())*product.getPrice()));
                    quantity.setText(Integer.toString(counts.get(product.getId())));
                    totalview.setText(Integer.toString(getTotal()));

                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.remove(product);
                notifyDataSetChanged();
                totalview.setText(Integer.toString(getTotal()));
            }
        });

        return convertView;
    }

    public HashMap<Integer, Integer> getCounts() {
        return counts;
    }

    public int getTotal() {
        int total=0;
        Product product;
        for(int i=0;i<products.size();i++)
        {
            product= products.get(i);
            total+=product.getPrice()*counts.get(product.getId());
        }
        return total;
    }
}
