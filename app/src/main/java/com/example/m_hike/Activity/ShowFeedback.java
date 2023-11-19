package com.example.m_hike.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Adapter.FeedbackAdapter;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Feedback;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ShowFeedback extends AppCompatActivity {


    List<Feedback> list = new ArrayList<>();
    HikeDAO hikeDAO;
    Toolbar toolbar;
    RecyclerView recyclerView;
    FeedbackAdapter adapter;
    public static int hike_id;

    Button menu;

    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout, title, btn_showFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);

        recyclerView = findViewById(R.id.reViewShowBlog);
        hikeDAO = new HikeDAO(ShowFeedback.this);

        hikeDAO = new HikeDAO(this);

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        title.setText("List Feedback");
        setSupportActionBar(toolbar);

        menu = findViewById(R.id.btn_menuBar);

        recyclerView = findViewById(R.id.reViewShowFeedback);
        list.clear();

        list = hikeDAO.getFeedbackHike(hike_id);

        adapter = new FeedbackAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowFeedback.this));
        recyclerView.setAdapter(adapter);


        drawerLayout = findViewById(R.id.layoutDrawerShowFeedback);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        btn_showFeedback = findViewById(R.id.btn_showFeedback);
        myFav = findViewById(R.id.txt_favorites);


        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer(drawerLayout);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ShowFeedback.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(ShowFeedback.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(ShowFeedback.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(ShowFeedback.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });


        BottomNavigationView navigationView = findViewById(R.id.bottom_navFeedback);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    redirectActivity(ShowFeedback.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(ShowFeedback.this, ProfileActivity.class);
                }else if (id == R.id.hike) {
                    redirectActivity(ShowFeedback.this, HikeViewActivity.class);
                }
                return true;
            }
        });

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