package com.example.m_hike.Activity.Favorites;

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
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Adapter.FavoritesAdapter;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewActivity extends AppCompatActivity {


    Toolbar toolbar;

    Button menu;;

    HikeDAO hikeDAO;
    RecyclerView recyclerView;
    FavoritesAdapter adapter;
    TextView Title;
    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout, title;
    List<Hike> hikeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_view);

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        title.setText("List Favorites");
        setSupportActionBar(toolbar);

        menu = findViewById(R.id.btn_menuBar);

        hikeDAO = new HikeDAO(this);
        recyclerView = findViewById(R.id.reViewShowFavorites);
        hikeList.clear();

        hikeList = hikeDAO.getListFavUser();

        adapter = new FavoritesAdapter(this, hikeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoritesViewActivity.this));
        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawer_layoutFav);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
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
                redirectActivity(FavoritesViewActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(FavoritesViewActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(FavoritesViewActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(FavoritesViewActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
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
                    redirectActivity(FavoritesViewActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(FavoritesViewActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(FavoritesViewActivity.this, HikeViewActivity.class);
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