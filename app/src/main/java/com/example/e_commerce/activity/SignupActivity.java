package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.customer.CustomerDAO;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    Button birthdate;
    int byear=2021,bmonth=12,bday=30;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        /*(int ID, String name, String email, String password, String gender, Date BOD, String job)*/
        EditText name=(EditText)findViewById(R.id.SignupName);
        EditText email=(EditText)findViewById(R.id.SignupEmail);
        EditText password=(EditText)findViewById(R.id.SignupPassword);
        EditText repassword=(EditText)findViewById(R.id.SignupRePassword);
        Spinner gender=findViewById(R.id.spinner);
        birthdate = findViewById(R.id.datePickerButton);;
        EditText job=findViewById(R.id.SignupJob);
        Button signupbutton = (Button) findViewById(R.id.Signupbutton);

        initDatePicker();
        birthdate.setText(getTodaysDate());

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().equals(repassword.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Passwords not the same!",Toast.LENGTH_LONG).show();
                else
                {
                    CustomerDAO dao=new CustomerDAO(getApplicationContext());
                    if(!dao.CheckEmail(email.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Email is already registered!",Toast.LENGTH_LONG).show();
                    else if(dao.Signup(name.getText().toString(),email.getText().toString(),password.getText().toString(),
                            gender.getSelectedItem().toString(), birthdate.getText().toString(),job.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(),"Successfully signed up",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                byear=year;
                bmonth=month;
                bday=day;
                String date = makeDateString(day, month, year);
                birthdate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}
