package com.example.m_hike.Activity.Blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Blog;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class DetailBlogActivity extends AppCompatActivity {

    TextView txt_nameHike, txt_content;
    Button btn_save, btn_cancel;

    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout, title;
    Button menu;
    int hike_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_blog);


        getViewsById();

        getData();
        title = findViewById(R.id.txt_titleToolBar);

        title.setText("View Blog");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailBlogActivity.this, BlogViewActivity.class));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int H = calendar.get(Calendar.HOUR_OF_DAY);
                int M = calendar.get(Calendar.MINUTE);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                int Month = calendar.get(Calendar.MONTH);
                int Year = calendar.get(Calendar.YEAR);

                String date = ""+Day+"/" + ""+Month+"/" + ""+Year+"  " + ""+H+":" + ""+M+"";

                Blog blog = new Blog(0, hike_id, ProfileActivity.user_id, txt_content.getText().toString(), date);

                HikeDAO hikeDAO = new HikeDAO(DetailBlogActivity.this);
                hikeDAO.insertBlog(blog);

                Toast.makeText(DetailBlogActivity.this, "Add Success", Toast.LENGTH_SHORT).show();

                BlogViewActivity.myBlog = "myBlog";
                Intent intent = new Intent(DetailBlogActivity.this, BlogViewActivity.class);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.layoutDrawerDetailBlog);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);
        menu = findViewById(R.id.btn_menuBar);

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DetailBlogActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DetailBlogActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                openDrawer(drawerLayout);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(DetailBlogActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(DetailBlogActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navDetaiBlog);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(DetailBlogActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(DetailBlogActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(DetailBlogActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });

    }

    private void getData() {
        if(getIntent().hasExtra("hike_id") && getIntent().hasExtra("content")&& getIntent().hasExtra("hike_name")) {

            hike_id = Integer.parseInt(getIntent().getStringExtra("hike_id"));
            String hike_name = getIntent().getStringExtra("hike_name");
            String content = getIntent().getStringExtra("content");

            txt_content.setText(content);
            txt_nameHike.setText(hike_name);
        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void getViewsById() {
        txt_content = findViewById(R.id.txt_contentDetail);
        txt_nameHike = findViewById(R.id.nameHikeDetail);

        btn_cancel = findViewById(R.id.btn_cancelAddBlog);
        btn_save = findViewById(R.id.btn_saveAddBlog);

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