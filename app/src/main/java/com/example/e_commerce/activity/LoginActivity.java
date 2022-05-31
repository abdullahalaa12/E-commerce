package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.DBHelper;
import com.example.e_commerce.R;
import com.example.e_commerce.category.CategoryDAO;
import com.example.e_commerce.customer.Customer;
import com.example.e_commerce.customer.CustomerDAO;

public class LoginActivity extends AppCompatActivity {
    CustomerDAO dao;
    Customer customer;
    EditText email;
    EditText password;
    CheckBox rememberme;
    Button forgot;
    Button signup;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.login);

        //new CategoryDAO(getApplicationContext()).insertCategories(new String[]{"Mobiles","Electronics","Books"});

        SharedPreferences preferences = getSharedPreferences("rememberme",MODE_PRIVATE);
        dao=new CustomerDAO(getApplicationContext());

        if(preferences.getBoolean("remember",false))
        {
            try {
                customer = dao.Login(preferences.getInt("ID", -1));
                moveToMain(customer);

            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }

        }

        initialize();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    customer=dao.Login(email.getText().toString(),password.getText().toString());
                    if(customer==null)
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Email/Password",Toast.LENGTH_LONG).show();
                        password.setText("");
                    }
                    else
                    {
                        if(rememberme.isChecked())
                        {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("remember",true);
                            editor.putInt("ID",customer.getID());
                            editor.apply();
                        }
                        moveToMain(customer);
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

    }

    private void moveToMain(Customer customer)
    {
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        i.putExtra("Customer", customer);
        //Toast.makeText(getApplicationContext(), Integer.toString(customer.getID()), Toast.LENGTH_SHORT).show();
        startActivity(i);
    }

    private void initialize()
    {
        email=(EditText)findViewById(R.id.EmailLogin);
        password=(EditText)findViewById(R.id.PasswordLogin);
        rememberme=(CheckBox)findViewById(R.id.RememberMe);
        forgot=(Button) findViewById(R.id.ForgotPassword);
        signup = (Button)findViewById(R.id.SignUpLogin);
        login=(Button)findViewById(R.id.LoginButton);

        forgot.setBackgroundDrawable(null);
        signup.setBackgroundDrawable(null);
    }

}