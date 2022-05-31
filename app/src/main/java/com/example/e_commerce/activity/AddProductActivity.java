package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_commerce.category.Category;

import com.example.e_commerce.R;
import com.example.e_commerce.category.CategoryDAO;
import com.example.e_commerce.product.ProductDAO;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    ArrayList<Category> categories;
    ImageView image;
    Spinner categoryspinner;
    EditText name;
    EditText price;
    EditText qnt;
    Button addbutton;
    int catindex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct);

        initialize();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 101);
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDAO dao=new ProductDAO(getApplicationContext());
                boolean ret = dao.addProduct(name.getText().toString(),Integer.parseInt(price.getText().toString()),
                        Integer.parseInt(qnt.getText().toString()), image, categories.get(catindex).getID());
                if(ret) {
                    Toast.makeText(getApplicationContext(),"Product Added Successfully",Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        });

        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catindex=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initialize() {
        categories=(ArrayList<Category>)getIntent().getSerializableExtra("Cats");
        image=(ImageView) findViewById(R.id.imageView3);
        categoryspinner=(Spinner) findViewById(R.id.categoriesSpinner2);
        LoadCategories();
        name=(EditText) findViewById(R.id.ProductName);
        price=(EditText) findViewById(R.id.ProductPrice);
        qnt=(EditText) findViewById(R.id.ProductQuantity);
        addbutton=(Button) findViewById(R.id.AddProductButton);
    }

    private void LoadCategories()
    {
        ArrayList<String> cats=new ArrayList<String>();
        for(int i=0;i<categories.size();i++)
        {
            cats.add(categories.get(i).getName());
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, cats);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(adp);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {

            try {
                InputStream stream = getContentResolver().openInputStream(data.getData());
                Bitmap bit = BitmapFactory.decodeStream(stream);
                image.setImageBitmap(bit);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}