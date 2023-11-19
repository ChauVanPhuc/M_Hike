package com.example.m_hike.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.R;
import com.example.m_hike.myFunction;

public class LoginActivity extends AppCompatActivity {


    EditText ed_email, ed_password;
    TextView txt_sign;
    Button btn_login;

    public static boolean Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViewsById();

        // button sign in
        txt_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        // button login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myFunction myFunction = new myFunction();

                if (myFunction.checkEmpty(ed_email, "Enter your Mail") ||  // check Empty mail
                    myFunction.checkEmpty(ed_password, "Enter your Password")){ // check Empty password

                    // check format mail
                    if (!Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString().trim()).matches()){
                        ed_email.setError("Illegal Email");
                    }else {
                        HikeDAO hikeDAO = new HikeDAO(LoginActivity.this);
                        // check Exist
                        if (hikeDAO.checkLogin(ed_email.getText().toString(), ed_password.getText().toString())) {
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HikeViewActivity.class));

                        } else {
                            Toast.makeText(LoginActivity.this, "User Name Or Password not exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    private void getViewsById() {
        ed_email = findViewById(R.id.ed_emailLogin);
        ed_password = findViewById(R.id.ed_passwordLogin);

        txt_sign = findViewById(R.id.signUpLogin);
        btn_login = findViewById(R.id.btn_login);
    }
}