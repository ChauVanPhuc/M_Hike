package com.example.m_hike.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Feedback;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedbackViewActivity extends AppCompatActivity {

    TextView nameHike, title;
    EditText contentFB;
    RatingBar poin;
    Toolbar toolbar;

    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout;
    HikeDAO hikeDAO;
    Button btn_back, btn_submit, menu;

    public static int hike_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_view);

        nameHike = findViewById(R.id.txt_nameHikeFb);
        contentFB = findViewById(R.id.ed_contentfb);
        poin = findViewById(R.id.poinHikeFb);

        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.txt_titleToolBar);
        title.setText("Feedback");

        menu = findViewById(R.id.btn_menuBar);


        btn_submit = findViewById(R.id.btn_submitAddFB);
        btn_back = findViewById(R.id.btn_backAddFB);


        hikeDAO = new HikeDAO(FeedbackViewActivity.this);
        Hike hike = hikeDAO.getHikeBlog(hike_id);
        nameHike.setText(hike.getHike_name());


        drawerLayout = findViewById(R.id.layoutDrawer);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(FeedbackViewActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(FeedbackViewActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(FeedbackViewActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(FeedbackViewActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(FeedbackViewActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(FeedbackViewActivity.this, BlogViewActivity.class);
                    overridePendingTransition(0,0);
                } else if (id == R.id.profile) {
                    redirectActivity(FeedbackViewActivity.this, ProfileActivity.class);
                    overridePendingTransition(0,0);
                }
                else if (id == R.id.hike) {
                    redirectActivity(FeedbackViewActivity.this, HikeViewActivity.class);
                    overridePendingTransition(0,0);
                }
                return true;
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Feedback feedback = new Feedback(0, ProfileActivity.user_id,hike_id,poin.getRating(),contentFB.getText().toString());

                HikeDAO hikeDAO = new HikeDAO(FeedbackViewActivity.this);
                hikeDAO.insertFeedback(feedback);

                Toast.makeText(FeedbackViewActivity.this, "Feedback Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FeedbackViewActivity.this, BlogViewActivity.class);
                startActivity(intent);
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