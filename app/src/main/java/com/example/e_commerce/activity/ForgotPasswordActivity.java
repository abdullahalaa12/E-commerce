package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.customer.CustomerDAO;
import com.example.e_commerce.emailapi.JavaMailAPI;

public class ForgotPasswordActivity extends AppCompatActivity {
    CustomerDAO dao;
    EditText email;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        dao=new CustomerDAO(getApplicationContext());
        email=(EditText) findViewById(R.id.EmailForgot);
        button=(Button) findViewById(R.id.ForgotButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dao.CheckEmail(email.getText().toString()))
                {
                    String password = dao.getPassword(email.getText().toString());

                    String subject="E-Commerce password recovery";
                    String message="Your Password Is : "+password;

                    JavaMailAPI javaMailAPI=new JavaMailAPI(getApplicationContext(),
                            email.getText().toString(), subject, message);
                    javaMailAPI.execute();
                    Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "This Email Is Not Registered !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}