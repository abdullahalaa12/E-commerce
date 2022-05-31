package com.example.e_commerce.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.customer.CustomerDAO;
import com.example.e_commerce.emailapi.JavaMailAPI;
import com.example.e_commerce.order.OrderDAO;
import com.example.e_commerce.product.CartAdapter;
import com.example.e_commerce.product.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    HashMap<Integer, Integer> selectedcounts;
    ArrayList<Product> selectedproducts;
    int customerID;
    String inputlocation;
    CartAdapter cartadapt;
    GridView gridView;
    TextView totalprice;
    EditText location;
    Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        initialize();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedproducts.size()==0)
                {
                    Toast.makeText(getApplicationContext(), "Cart Is Empty !", Toast.LENGTH_SHORT).show();
                }
                else if(inputlocation.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Set Location !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    OrderDAO orderDAO = new OrderDAO(getApplicationContext());
                    if(orderDAO.makeOrder(customerID, inputlocation, selectedcounts, selectedproducts))
                    {
                        CustomerDAO customerDAO = new CustomerDAO(getApplicationContext());
                        String email = customerDAO.getEmail(customerID);
                        String subject = "E-Commerce";
                        String message = "Your order with total price of "
                                +totalprice.getText()+"\nhas been successfully submitted";
                        JavaMailAPI javaMailAPI = new JavaMailAPI(getApplicationContext(),
                                email,subject,message);
                        javaMailAPI.execute();
                        Toast.makeText(getApplicationContext(),"Submitted Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        location.setInputType(InputType.TYPE_NULL);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getLocation();
                }
            }
        });
    }

    private void getLocation() {
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, 102);
    }

    private void initialize()
    {
        customerID=getIntent().getIntExtra("CustomerID",-1);
        if(customerID==-1)
        {
            Toast.makeText(getApplicationContext(), "ERROR IN CUSTOMER ID", Toast.LENGTH_SHORT).show();
            finish();
        }
        selectedcounts =(HashMap<Integer, Integer>) getIntent().getSerializableExtra("SelectedCounts");
        selectedproducts =(ArrayList<Product>) getIntent().getSerializableExtra("SelectedProducts");
        inputlocation = "";

        gridView=(GridView) findViewById(R.id.cartgridview);
        totalprice=(TextView)findViewById(R.id.totalprice);
        location=(EditText)findViewById(R.id.locationinput);
        buy=(Button)findViewById(R.id.submitbutton);

        cartadapt = new CartAdapter(getApplicationContext(), R.layout.cartproduct, selectedproducts, selectedcounts, totalprice);
        gridView.setAdapter(cartadapt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 102) {
            inputlocation = data.getStringExtra("PickedPoint");
            location.setText(inputlocation);
        }
    }
}