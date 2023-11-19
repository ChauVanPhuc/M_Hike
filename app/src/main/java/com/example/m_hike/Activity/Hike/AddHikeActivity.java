package com.example.m_hike.Activity.Hike;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;
import com.example.m_hike.myFunction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddHikeActivity extends AppCompatActivity {


    EditText hike_des, hike_length, hike_location, hike_name;
    TextView hike_date, title, errorDate;;

    ImageView showImg;
    Toolbar toolbar;
    Button menu;
    RadioButton rd_yes, rd_no;
    String hike_parking, hike_level;
    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout;
    Spinner sp_levelHike;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    Button Camera, Folder;

    Button btn_submit, btn_cancel, btn_saveUpdate, btn_cancelUpdate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static boolean idHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        getViewsById();
        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        setSupportActionBar(toolbar);
        getlevel();
        getData();

        drawerLayout = findViewById(R.id.layoutDrawerAddHike);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);
        menu = findViewById(R.id.btn_menuBar);

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AddHikeActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AddHikeActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(AddHikeActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(AddHikeActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(AddHikeActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navAddHike);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(AddHikeActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(AddHikeActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(AddHikeActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });


        if (idHike){
            btn_cancel.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            title.setText("Update Hike");
            hike_date.setBackground(null);
        }else {
            title.setText("Add Hike");
            btn_saveUpdate.setVisibility(View.GONE);
            btn_cancelUpdate.setVisibility(View.GONE);
        }


        // Button Cancel for Update
        btn_cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Button Save for when update

        btn_saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParking();

                int checkLength = Integer.parseInt(hike_length.getText().toString());
                if (checkLength > 10000) {
                    hike_length.setError("Enter length < 10000");
                } else {
                    Bitmap bitmap;
                    if(showImg.getDrawable() == null){
                        Drawable drawable =  ResourcesCompat.getDrawable(getResources(), R.drawable.avatar, null);

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        assert bitmapDrawable != null;
                        bitmap = bitmapDrawable.getBitmap();
                    }else {

                        // chuyển data img -> byte[]
                        BitmapDrawable drawable = (BitmapDrawable) showImg.getDrawable();
                        bitmap = drawable.getBitmap();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.WEBP,50, byteArrayOutputStream);
                    byte[] img = byteArrayOutputStream.toByteArray();

                    int length = Integer.parseInt(hike_length.getText().toString());
                    int idHike = Integer.parseInt(getIntent().getStringExtra("idHike"));
                    int user_id = ProfileActivity.user_id;

                    Hike hike = new Hike(idHike, hike_name.getText().toString(), hike_location.getText().toString(),
                            hike_date.getText().toString(), hike_parking,
                            length, hike_level,
                            hike_des.getText().toString(), img, user_id);

                    HikeDAO hikeDAO = new HikeDAO(AddHikeActivity.this);
                    hikeDAO.updateHike(hike);

                    Toast.makeText(AddHikeActivity.this, "Update Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddHikeActivity.this, HikeViewActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Button cancel when add Hike

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // Button submit for add Hike
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkParking();
                Bitmap bitmap;

                    Intent intent = new Intent(AddHikeActivity.this, DetailHikeActivity.class);
                    intent.putExtra("name", hike_name.getText().toString());
                    intent.putExtra("location", hike_location.getText().toString());
                    intent.putExtra("date", hike_date.getText().toString());
                    intent.putExtra("parking", hike_parking);
                    intent.putExtra("length", hike_length.getText().toString());
                    intent.putExtra("level", hike_level);
                    intent.putExtra("des", hike_des.getText().toString());

                    myFunction function = new myFunction();

                    if (function.checkEmpty(hike_name, "Enter name Hike") && function.checkEmpty(hike_location, "Enter Location")
                            && checkDate() && function.checkEmpty(hike_length, "Enter Length Hike") &&
                            function.checkEmpty(hike_des, "Enter Des Hike")) {
                        int length = Integer.parseInt(hike_length.getText().toString());
                        if (length > 10000) {
                            hike_length.setError("Enter length < 10000");
                        } else {
                        if (showImg.getDrawable() == null) {
                            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.avatar, null);

                            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                            assert bitmapDrawable != null;
                            bitmap = bitmapDrawable.getBitmap();
                        } else {

                            // chuyển data img -> byte[]
                            BitmapDrawable drawable = (BitmapDrawable) showImg.getDrawable();
                            bitmap = drawable.getBitmap();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, byteArrayOutputStream);
                        byte[] img = byteArrayOutputStream.toByteArray();

                        intent.putExtra("img", img);

                        startActivity(intent);
                    }
                }
            }
        });


        // Button show date when add hike
        hike_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddHikeActivity.this,
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
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = month + "/" + dayOfMonth + "/" + year;
                hike_date.setBackground(null);
                hike_date.setText(date);

            }

        };


        //Button show Camera
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        //Button show Folder
        Folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });

    }

    //Take data Level
    private void getlevel() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Easy");
        arrayList.add("Medium");
        arrayList.add("Difficult");

        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,arrayList);
        sp_levelHike.setAdapter(adapter);


        sp_levelHike.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hike_level = arrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    boolean checkDate(){
        if(hike_date.getText().toString().equals("")){
            errorDate.setVisibility(View.VISIBLE);
            errorDate.setError("Please, Choose Time");
            return false;
        }else{
            errorDate.setVisibility(View.GONE);
            return true;
        }
    }

    //Button Camera and Folder
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            showImg.setImageBitmap(bitmap);
            showImg.setBackground(null);
        }

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                showImg.setImageBitmap(bitmap);
                showImg.setBackground(null);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getViewsById() {
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);
        btn_saveUpdate = findViewById(R.id.btn_updateHike);
        btn_cancelUpdate = findViewById(R.id.btn_cancelUpdateHike);

        hike_name = findViewById(R.id.ed_nameHike);
        hike_des = findViewById(R.id.ed_desHike);
        hike_location = findViewById(R.id.ed_locationHike);
        hike_length = findViewById(R.id.ed_lengthHike);
        sp_levelHike = findViewById(R.id.sp_levelHike);
        rd_yes = findViewById(R.id.rd_yesParking);
        rd_no = findViewById(R.id.rd_noParking);
        hike_date = findViewById(R.id.ed_dateHike);

        errorDate = findViewById(R.id.errorDate);

        showImg = findViewById(R.id.imageView);

        Camera = findViewById(R.id.btn_Camera);
        Folder = findViewById(R.id.btn_Folder);
    }

    void checkParking(){
        if(rd_yes.isChecked()){
            hike_parking = rd_yes.getText().toString();
        } else if (rd_no.isChecked()) {
            hike_parking = rd_no.getText().toString();
        }
    }


    // Take data from Activity other
    private void getData() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("location") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("parking") &&
                getIntent().hasExtra("length") && getIntent().hasExtra("level") &&
                getIntent().hasExtra("des") && getIntent().hasExtra("img")) {

            String name = getIntent().getStringExtra("name");
            String location = getIntent().getStringExtra("location");
            String date = getIntent().getStringExtra("date");
            String parking = getIntent().getStringExtra("parking");
            String length = getIntent().getStringExtra("length");
            String level = getIntent().getStringExtra("level");
            String des = getIntent().getStringExtra("des");

            Bitmap b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("img"), 0, getIntent().getByteArrayExtra("img").length);
            showImg.setBackground(null);
            showImg.setImageBitmap(b);

            if (parking!=null){
                if(parking.equals("Yes")){
                    rd_yes.setChecked(true);
                } else if (parking.equals("No")) {
                    rd_no.setChecked(true);
                }
            }


            if(level.equals("Easy")){
                sp_levelHike.setSelection(0);
            } else if (level.equals("Medium")) {
                sp_levelHike.setSelection(1);
            }else {
                sp_levelHike.setSelection(2);
            }

            hike_name.setText(name);
            hike_location.setText(location);
            hike_date.setText(date);
            hike_length.setText(length);
            hike_des.setText(des);
        }
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