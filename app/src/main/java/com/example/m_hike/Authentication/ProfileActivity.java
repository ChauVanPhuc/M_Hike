package com.example.m_hike.Authentication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.User;
import com.example.m_hike.R;
import com.example.m_hike.myFunction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    Button btn_update, btn_back, menu;
    EditText name, phone, address, job, passwordOld, confirmPassword, passwordNew;
    TextView date, txt_resetPassword, mail, myFav, myblog, newHike, newStatus, logout, title;
    RadioButton male, female;
    LinearLayout form_password;
    String gender, password;
    DrawerLayout drawerLayout;

    public static int user_id ;
    boolean checkForm = false;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getViewsById();
        getData();
        title.setText("Profile");
        form_password.setVisibility(View.GONE);

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(ProfileActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(ProfileActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(ProfileActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });



        BottomNavigationView navigationView = findViewById(R.id.bottom_navProfile);
        navigationView.setSelectedItemId(R.id.profile);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    redirectActivity(ProfileActivity.this, BlogViewActivity.class);
                    overridePendingTransition(0,0);
                } else if (id == R.id.profile) {
                    redirectActivity(ProfileActivity.this, ProfileActivity.class);
                    overridePendingTransition(0,0);
                }else if (id == R.id.hike) {
                    redirectActivity(ProfileActivity.this, HikeViewActivity.class);
                    overridePendingTransition(0,0);
                }
                return true;
            }
        });

        txt_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkForm){
                    checkForm = true;
                    form_password.setVisibility(View.VISIBLE);
                }else {
                    checkForm = false;
                    form_password.setVisibility(View.GONE);
                }

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                updateUser();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Profile Will Update New Information. Are You Sure ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        // back buton
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, HikeViewActivity.class);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfileActivity.this,
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
    }

    private void updateUser(){
        checkRadio();

        HikeDAO hikeDAO = new HikeDAO(ProfileActivity.this);
        User chekUser = hikeDAO.getUser(user_id);

        myFunction myFunction = new myFunction();
        if(checkForm){
            if(myFunction.checkEmpty(passwordNew, "Enter Password")) {
                if (!chekUser.getUser_password().equals(passwordOld.getText().toString())) {
                    passwordOld.setError("Password incorrect");
                } else if (chekUser.getUser_password().equals(passwordNew.getText().toString())) {
                    passwordNew.setError("Passwords Can Not Be Repeated");
                } else {
                    if (passwordNew.getText().toString().equals(confirmPassword.getText().toString())) {
                        User user = new User(user_id, name.getText().toString(), date.getText().toString(),
                                gender, mail.getText().toString(), phone.getText().toString(), address.getText().toString(),
                                job.getText().toString(), passwordNew.getText().toString());

                        hikeDAO = new HikeDAO(this);
                        hikeDAO.updateUser(user);

                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                        Toast.makeText(this, "Updated Information Profile Success", Toast.LENGTH_SHORT).show();
                    } else {
                        confirmPassword.setError("Password Mismatched");
                    }

                }
            }
        }else {
            User user = new User(user_id, name.getText().toString(), date.getText().toString(),
                    gender, mail.getText().toString(), phone.getText().toString(), address.getText().toString(),
                    job.getText().toString(), password);

            hikeDAO = new HikeDAO(this);
            hikeDAO.updateUser(user);

            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            Toast.makeText(this, "Updated Information Profile Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkRadio(){
        if (male.isChecked()){
            gender = male.getText().toString();
        } else if (female.isChecked()) {
            gender = female.getText().toString();
        }
    }

    private void getData() {
        HikeDAO hikeDAO = new HikeDAO(this);
        List<User> list =  hikeDAO.getUser();

        for(User user : list){
            name.setText(user.getUser_name());
            mail.setText(user.getUser_mail());
            phone.setText(user.getUser_phone());
            address.setText(user.getUser_address());
            job.setText(user.getUser_job());
            date.setText(user.getUser_birth());


            if(!user.getUser_birth().equals("")){
                date.setBackground(null);
            }

            password = user.getUser_password();
            if(!user.getUser_birth().equals("")){
                date.setBackground(null);
            }

            if(user.getUser_gender() != null){
                if(user.getUser_gender().equals("Male")){
                    male.setChecked(true);
                } else if (user.getUser_gender().equals("Female")) {
                    female.setChecked(true);
                }
            }

        }
    }

    private void getViewsById() {
        name = findViewById(R.id.ed_nameProfile);
        mail =findViewById(R.id.ed_mailProfile);
        phone = findViewById(R.id.ed_phoneProfile);
        address = findViewById(R.id.ed_addressProfile);
        job = findViewById(R.id.ed_jobProfile);
        passwordNew = findViewById(R.id.ed_passwordNewProfile);
        passwordOld = findViewById(R.id.ed_passwordOldProfile);
        form_password = findViewById(R.id.form_password);
        date = findViewById(R.id.ed_dateProfile);
        male = findViewById(R.id.rd_maleProfile);
        female = findViewById(R.id.rd_femaleProfile);
        txt_resetPassword = findViewById(R.id.txt_resetPassword);
        confirmPassword = findViewById(R.id.ed_confirmPasswordProfile);
        btn_back = findViewById(R.id.btn_backProfile);
        btn_update = findViewById(R.id.btn_editProfile);
        title = findViewById(R.id.txt_titleToolBar);
        drawerLayout = findViewById(R.id.layoutDrawerProfile);
        menu = findViewById(R.id.btn_menuBar);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);
    }

    public static void openDrawer(DrawerLayout drawerLayout){

        drawerLayout.openDrawer(GravityCompat.START);

    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        activity.startActivity(intent);
        activity.finish();
    }
}