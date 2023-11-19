package com.example.m_hike.Authentication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.User;
import com.example.m_hike.R;
import com.example.m_hike.myFunction;

import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    Button btn_submit, btn_back;
    EditText name, mail, phone, address, job, password, confirmPassword;
    TextView date;
    RadioButton male, female;
    String gender;
    HikeDAO hikeDAO;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    boolean checkUser = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getViewsById();
        hikeDAO = new HikeDAO(RegisterActivity.this);

        //back button
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // show date button
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String Date = month + "/" + dayOfMonth + "/" + year;
                date.setBackground(null);
                date.setText(Date);

            }
        };

        // Submit button
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGender();

                myFunction myFunction = new myFunction();

                if (myFunction.checkEmpty(name, "Enter your name") &&
                    myFunction.checkEmpty(mail, "Enter you Mail") &&
                    myFunction.checkEmpty(password, "Enter your Password") &&
                    myFunction.checkEmpty(confirmPassword, "Enter confirm Password")) {

                    if (myFunction.checkLength(name, "Name must not exceed 50 characters") && myFunction.checkLength(mail, "Mail must not exceed 50 characters") &&
                            myFunction.checkLength(password, "Password must not exceed 50 characters") && myFunction.checkLength(confirmPassword, "Password must not exceed 50 characters") &&
                            myFunction.checkPhone(phone, "Phone must not exceed 10 number") && myFunction.checkLength(address, "Address must not exceed 50 characters")) {


                        // check confirm password
                        if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                            confirmPassword.setError("Password Incorrect");

                            // check mail
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString().trim()).matches()) {
                            mail.setError("Illegal Email");
                        } else {
                            CheckUser(); // Check the existence of the user

                            // Create a new user account
                            if (!CheckUser()) {
                                User user = new User(0, name.getText().toString(),
                                        date.getText().toString(), gender, mail.getText().toString(),
                                        phone.getText().toString(), address.getText().toString(),
                                        job.getText().toString(), password.getText().toString());

                                hikeDAO.insertUser(user);

                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                            } else {
                                Toast.makeText(RegisterActivity.this, "Mail already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

    // Check the existence of the email before signing a new one
    private boolean CheckUser() {
        HikeDAO hikeDAO = new HikeDAO(RegisterActivity.this);
        List<User> list = hikeDAO.getUser();

        for (User listUser : list){
            if(listUser.getUser_mail().equals(mail.getText().toString())){
                checkUser = true;
                break;
            }else {
                checkUser = false;
            }
        }
        return checkUser;
    }

    void checkGender(){
        if(male.isChecked()){
            gender = male.getText().toString();
        } else if (female.isChecked()) {
            gender = female.getText().toString();
        }
    }

    private void getViewsById() {
        btn_back = findViewById(R.id.btn_backRegister);
        btn_submit = findViewById(R.id.btn_submitRegister);

        name = findViewById(R.id.ed_nameRegister);
        date = findViewById(R.id.ed_dateHike);
        mail = findViewById(R.id.ed_mailRegister);
        phone = findViewById(R.id.ed_phoneRegister);
        address = findViewById(R.id.ed_addressRegister);
        job = findViewById(R.id.ed_jobRegister);
        password = findViewById(R.id.ed_passwordRegister);
        confirmPassword = findViewById(R.id.ed_confirmPasswordRegister);

        male = findViewById(R.id.rd_maleSignUp);
        female = findViewById(R.id.rd_femaleSignUp);

    }

}