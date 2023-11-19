package com.example.m_hike.Activity.Hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.FeedbackViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Activity.ShowFeedback;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Favorites;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DetailHikeActivity extends AppCompatActivity {

    TextView hike_des, hike_level, hike_length, hike_parking, hike_location, hike_name, hike_date, title;

    ImageView showImg;

    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout, btn_showFeedback;
    Toolbar toolbar;
    Button bth_save, btn_cancel, btn_edit, btn_delete, btn_feedback, btn_backUser, btn_favorites;
    HikeDAO hikeDAO;
    Button menu;
    RelativeLayout areUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hike);

        hikeDAO = new HikeDAO(DetailHikeActivity.this);
        getViewsById();

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);
        menu = findViewById(R.id.btn_menuBar);
        title.setText("Detail Hike");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.layoutDrawerDetailHike);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);
        btn_showFeedback = findViewById(R.id.btn_showFeedback);

        btn_showFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHikeActivity.this, ShowFeedback.class);

                startActivity(intent);

            }
        });

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DetailHikeActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DetailHikeActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(DetailHikeActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(DetailHikeActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(DetailHikeActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navDetailHike);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(DetailHikeActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(DetailHikeActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(DetailHikeActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });

        if(!getIntent().hasExtra("blog_id")){
            getData();
            if (getIntent().hasExtra("idHike")) {
                btn_cancel.setVisibility(View.GONE);
                bth_save.setVisibility(View.GONE);
            } else {
                btn_edit.setVisibility(View.GONE);
                btn_delete.setVisibility(View.GONE);
                btn_showFeedback.setVisibility(View.GONE);
            }
            areUser.setVisibility(View.GONE);
        }else {
            if(BlogViewActivity.myBlog == ""){
                btn_cancel.setVisibility(View.GONE);
                bth_save.setVisibility(View.GONE);
                btn_edit.setVisibility(View.GONE);
                btn_delete.setVisibility(View.GONE);
            }else {
                btn_cancel.setVisibility(View.GONE);
                bth_save.setVisibility(View.GONE);
                btn_edit.setVisibility(View.GONE);
                btn_delete.setVisibility(View.GONE);
                btn_feedback.setVisibility(View.GONE);
                btn_favorites.setVisibility(View.GONE);
            }
            getDataHike();

        }
        btn_backUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Button to show Activity Fav
            btn_favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    AddFavorites();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailHikeActivity.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });


        //Button delete when press detail hike
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    int idHike = Integer.parseInt(getIntent().getStringExtra("idHike"));
                                    delete(idHike);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailHikeActivity.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });

            // Button use to edit when press it

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailHikeActivity.this, AddHikeActivity.class);
                    AddHikeActivity.idHike = true;
                    intent.putExtra("idHike", getIntent().getStringExtra("idHike"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("location", getIntent().getStringExtra("location"));
                    intent.putExtra("date", getIntent().getStringExtra("date"));
                    intent.putExtra("parking", getIntent().getStringExtra("parking"));
                    intent.putExtra("length", getIntent().getStringExtra("length"));
                    intent.putExtra("level", getIntent().getStringExtra("level"));
                    intent.putExtra("des", getIntent().getStringExtra("des"));
                    intent.putExtra("img", getIntent().getByteArrayExtra("img"));

                    startActivity(intent);
                }
            });


            // Button use to cancel when add Hike
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(DetailHikeActivity.this, AddHikeActivity.class);
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("location", getIntent().getStringExtra("location"));
                    intent.putExtra("date", getIntent().getStringExtra("date"));
                    intent.putExtra("parking", getIntent().getStringExtra("parking"));
                    intent.putExtra("length", getIntent().getStringExtra("length"));
                    intent.putExtra("level", getIntent().getStringExtra("level"));
                    intent.putExtra("des", getIntent().getStringExtra("des"));
                    intent.putExtra("img", getIntent().getByteArrayExtra("img"));
                    startActivity(intent);
                }
            });

            // Button Feedback in Detail Hike
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHikeActivity.this, FeedbackViewActivity.class);
                int _hikeId = Integer.parseInt(getIntent().getStringExtra("hike_id"));
                FeedbackViewActivity.hike_id = _hikeId;
                startActivity(intent);
            }
        });

            // Button user to Save Hike when press it
            bth_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    byte[] img = getIntent().getByteArrayExtra("img");
                    int length = Integer.parseInt(hike_length.getText().toString());
                    int user_id = ProfileActivity.user_id;

                    Hike hike = new Hike(0, hike_name.getText().toString(), hike_location.getText().toString(),
                            hike_date.getText().toString(), hike_parking.getText().toString(),
                            length, hike_level.getText().toString(),
                            hike_des.getText().toString(), img, user_id);

                    hikeDAO.insertHike(hike);

                    Toast.makeText(DetailHikeActivity.this, "Add Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DetailHikeActivity.this, HikeViewActivity.class);
                    startActivity(intent);
                }
            });

        }

    private void getDataHike() {

        int hike_id = Integer.parseInt(getIntent().getStringExtra("hike_id"));

        Hike hike = hikeDAO.getHikeBlog(hike_id);

        if(hike != null){
            hike_des.setText(hike.getHike_description());
            hike_level.setText(hike.getHike_level());
            String length = String.valueOf(hike.getHike_length());
            hike_length.setText(length);
            hike_parking.setText(hike.getHike_parking());
            hike_location.setText(hike.getHike_location());
            hike_name.setText(hike.getHike_name());
            hike_date.setText(hike.getHike_date());

            byte[] img = hike.getHike_img();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            showImg.setBackground(null);
            showImg.setImageBitmap(bitmap);
        }

    }


    //Button user to Add Favorites

    boolean flag =false;
    public void AddFavorites() {

        int hike_id = Integer.parseInt(getIntent().getStringExtra("hike_id"));
        int user_id = ProfileActivity.user_id;
        Favorites favorites = new Favorites(0,hike_id, user_id);

        List<Favorites> f =  hikeDAO.getListFav();

        if(f.size() == 0){
            flag = true;
        }else {
            for(Favorites fav : f){
                if(fav.getHike_id() == hike_id && fav.getUser_id() == user_id){
                    flag = false;
                }else {
                    flag = true;
                }
            }
        }
        if(flag){
            hikeDAO.insertFavorites(favorites);

            Toast.makeText(DetailHikeActivity.this, "Added to favorites list Success", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(DetailHikeActivity.this, "Favorites already exist", Toast.LENGTH_SHORT).show();
        }

    }


    // function delete Hike
    private void delete(int id) {
        hikeDAO.deleteHike(id);
        Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DetailHikeActivity.this, HikeViewActivity.class));

    }


    // Take Date from Activity other
    void getData(){
        if(getIntent().hasExtra("name") && getIntent().hasExtra("location") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("parking") &&
                getIntent().hasExtra("length") && getIntent().hasExtra("level") &&
                getIntent().hasExtra("des") && getIntent().hasExtra("img")){

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

            hike_name.setText(name);
            hike_location.setText(location);
            hike_date.setText(date);
            hike_parking.setText(parking);
            hike_length.setText(length);
            hike_level.setText(level);
            hike_des.setText(des);

        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void getViewsById() {
        btn_cancel = findViewById(R.id.btn_cancelHike);
        bth_save = findViewById(R.id.btn_saveHike);
        btn_delete = findViewById(R.id.btn_deleteHike);
        btn_edit = findViewById(R.id.btn_EditHike);

        hike_name = findViewById(R.id.txt_nameHikeDetail);
        hike_des = findViewById(R.id.txt_desHike);
        hike_location = findViewById(R.id.txt_locationHike);
        hike_level = findViewById(R.id.txt_levelHike);
        hike_length = findViewById(R.id.txt_lengthHike);
        hike_parking = findViewById(R.id.txt_parkingHike);
        hike_date = findViewById(R.id.txt_dateHike);

        areUser= findViewById(R.id.areUser);

        btn_feedback = findViewById(R.id.btn_feedbackHike);
        btn_backUser = findViewById(R.id.btn_backHikeUser);
        btn_favorites = findViewById(R.id.btn_favoritesHike);

        showImg = findViewById(R.id.imgHikeDetail);
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