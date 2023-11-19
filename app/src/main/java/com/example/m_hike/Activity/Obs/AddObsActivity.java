package com.example.m_hike.Activity.Obs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.Models.Obs;
import com.example.m_hike.R;
import com.example.m_hike.myFunction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddObsActivity extends AppCompatActivity {


    EditText ed_name, ed_distance, ed_des;
    ImageView showImg;
    Spinner sp_levelObs;
    String ed_level;

    Toolbar toolbar;
    Button menu;
    DrawerLayout drawerLayout;
    TextView  myFav, myblog, newHike, newStatus, logout, errorDateObs;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    Button Camera, Folder;
    TextView ed_time, title;


    Button btn_submit, btn_cancel, btn_saveUpdate, btn_cancelUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_obs);

        getViewsById();
        getLevel();

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("Obs_id")){
            btn_cancel.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            title.setText("Update Observation");
            title.setTextSize(26);
            getData();
        }else {
            btn_saveUpdate.setVisibility(View.GONE);
            btn_cancelUpdate.setVisibility(View.GONE);
            title.setText("Add Observation");
            title.setTextSize(26);
        }

        drawerLayout = findViewById(R.id.layoutDrawerAddObs);
        menu = findViewById(R.id.btn_menuBar);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        myFav = findViewById(R.id.txt_favorites);
        logout = findViewById(R.id.txt_logOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AddObsActivity.this, LoginActivity.class);
            }
        });
        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AddObsActivity.this, FavoritesViewActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(AddObsActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(AddObsActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(AddObsActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navAddObs);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(AddObsActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(AddObsActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(AddObsActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });
        //Button used to show Camera
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        // Button used to show Folder

        Folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });


        //Button used to choose Time

        ed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });

        // Button used to cancel when Update
        btn_cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddObsActivity.this, DetailObsActivity.class);
                intent.putExtra("name", ed_name.getText().toString());
                intent.putExtra("time", ed_time.getText().toString());
                intent.putExtra("level", ed_level);
                intent.putExtra("des", ed_des.getText().toString());
                intent.putExtra("dis", ed_distance.getText().toString());

                byte[] img = getIntent().getByteArrayExtra("img");
                intent.putExtra("img", img);

                startActivity(intent);
            }
        });


        // Button used to cancel when add Hike
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddObsActivity.this, ObsViewActivity.class);
                startActivity(intent);
            }
        });

        // Button used to submit when Add Hike
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap;

                HikeDAO hikeDAO = new HikeDAO(AddObsActivity.this);
                Hike h = hikeDAO.getListHike(ObsViewActivity.hike_id);
                int lenght = h.getHike_length();

                Intent intent = new Intent(AddObsActivity.this, DetailObsActivity.class);
                intent.putExtra("name", ed_name.getText().toString());
                intent.putExtra("time", ed_time.getText().toString());
                intent.putExtra("level", ed_level);
                intent.putExtra("des", ed_des.getText().toString());
                intent.putExtra("dis", ed_distance.getText().toString());
                myFunction function = new myFunction();


                if(function.checkEmpty(ed_name, "Enter name Observation") && checkDate() && function.checkEmpty(ed_distance, "Enter Distance")) {
                    int distance = Integer.parseInt(ed_distance.getText().toString());
                    if (lenght < distance) {
                        ed_distance.setError("Distance < "+lenght+"");
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


        // Button used to Save Hike when update Hike
        btn_saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HikeDAO hikeDAO = new HikeDAO(AddObsActivity.this);
                Hike h = hikeDAO.getListHike(ObsViewActivity.hike_id);
                int lenght = h.getHike_length();

                int dis = Integer.parseInt(ed_distance.getText().toString());
                if (lenght < dis) {
                    ed_distance.setError("Distance < " + lenght + "");
                } else {

                    Bitmap bitmap;
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

                    int Obs_id = Integer.parseInt(getIntent().getStringExtra("Obs_id"));
                    int hike_id = ObsViewActivity.hike_id;

                    Obs obs = new Obs(Obs_id, hike_id, ed_name.getText().toString(),
                            ed_time.getText().toString(), ed_level,
                            ed_distance.getText().toString(),
                            img, ed_des.getText().toString());


                    hikeDAO.updateObs(obs);

                    Toast.makeText(AddObsActivity.this, "Update Observations Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddObsActivity.this, ObsViewActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getLevel() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Safe");
        arrayList.add("Moderate");
        arrayList.add("Dangerous");
        arrayList.add("Lethal");

        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,arrayList);
        sp_levelObs.setAdapter(adapter);

        sp_levelObs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ed_level = arrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    boolean checkDate(){
        if(ed_time.getText().toString().equals("")){
            errorDateObs.setVisibility(View.VISIBLE);
            return false;
        }else{
            errorDateObs.setVisibility(View.GONE);
            return true;
        }
    }

    private void getTime() {

        Calendar calendar = Calendar.getInstance();
        int H = calendar.get(Calendar.HOUR_OF_DAY);
        int M = calendar.get(Calendar.MINUTE);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(Year,Month,Day,hourOfDay,minute);
                ed_time.setBackground(null);
                ed_time.setText(simpleDateFormat.format(calendar.getTime()));

                checkDate();
            }
        }, 0, 0, true);
        timePickerDialog.show();
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

        ed_name = findViewById(R.id.ed_addNameObs);
        ed_time = findViewById(R.id.ed_addTimeObs);
        sp_levelObs = findViewById(R.id.ed_addLevelObs);
        ed_distance = findViewById(R.id.ed_addDistanceObs);
        ed_des = findViewById(R.id.ed_addDescriptionObs);

        showImg = findViewById(R.id.img_addShowImgObs);

        Camera = findViewById(R.id.btn_addCameraObs);
        Folder = findViewById(R.id.btn_addFolderObs);


        btn_cancel = findViewById(R.id.btn_cancelAddObs);
        btn_submit = findViewById(R.id.btn_submitAddObs);
        btn_saveUpdate = findViewById(R.id.btn_saveUpdateObs);
        btn_cancelUpdate = findViewById(R.id.btn_cancelUpdateObs);
        errorDateObs = findViewById(R.id.errorDateObs);
    }

    // Take Data from Activity other
    private void getData() {
        if(getIntent().hasExtra("name") && getIntent().hasExtra("time") &&
                getIntent().hasExtra("level")&& getIntent().hasExtra("img") &&
                getIntent().hasExtra("des") && getIntent().hasExtra("dis")){

            String name = getIntent().getStringExtra("name");
            String time = getIntent().getStringExtra("time");
            String level = getIntent().getStringExtra("level");
            String des = getIntent().getStringExtra("des");
            String dis = getIntent().getStringExtra("dis");

            Bitmap b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("img"), 0, getIntent().getByteArrayExtra("img").length);
            showImg.setBackground(null);
            ed_time.setBackground(null);
            showImg.setImageBitmap(b);


            if(level.equals("Safe")){
                sp_levelObs.setSelection(0);
            } else if (level.equals("Moderate")) {
                sp_levelObs.setSelection(1);
            } else if (level.equals("Dangerous")) {
                sp_levelObs.setSelection(2);
            }else {
                sp_levelObs.setSelection(3);
            }

            ed_name.setText(name);
            ed_time.setText(time);
//            ed_level.setText(level);
            ed_des.setText(des);
            ed_distance.setText(dis);
        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }

    }
    public void openDrawer(DrawerLayout drawerLayout){

        drawerLayout.openDrawer(GravityCompat.START);

    }

    public void closeDrawer(DrawerLayout drawerLayout){
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