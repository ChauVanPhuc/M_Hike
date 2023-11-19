package com.example.m_hike.Activity.Hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Adapter.HikeAdapter;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HikeViewActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button menu, btn_addHike;;
    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout, title;

    HikeDAO hikeDAO;
    RecyclerView recyclerView;
    HikeAdapter adapter;

    List<Hike> hikeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_view);

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        title.setText("View Hike");
        setSupportActionBar(toolbar);

        menu = findViewById(R.id.btn_menuBar);
        btn_addHike = findViewById(R.id.btn_addHike);

        hikeDAO = new HikeDAO(this);
        recyclerView = findViewById(R.id.reViewShowHike);
        hikeList.clear();

        int user_id = ProfileActivity.user_id;


        if(LoginActivity.Admin){
            hikeList = hikeDAO.getListHike();
        }else {
            hikeList = hikeDAO.getListHikeUser(user_id);
        }


        adapter = new HikeAdapter(this, hikeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(HikeViewActivity.this));
        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.layoutDrawer);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(HikeViewActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(HikeViewActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(HikeViewActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(HikeViewActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(HikeViewActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setSelectedItemId(R.id.hike);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(HikeViewActivity.this, BlogViewActivity.class);
                    overridePendingTransition(0,0);
                } else if (id == R.id.profile) {
                    redirectActivity(HikeViewActivity.this, ProfileActivity.class);
                    overridePendingTransition(0,0);
                }
                else if (id == R.id.hike) {
                    redirectActivity(HikeViewActivity.this, HikeViewActivity.class);
                    overridePendingTransition(0,0);
                }
                return true;
            }
        });


        // Button user to show Activity Add Hike
        btn_addHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHikeActivity.idHike = false;
                startActivity(new Intent(HikeViewActivity.this, AddHikeActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem menu_delete = menu.findItem(R.id.menu_delete);
        MenuItem menu_select_all = menu.findItem(R.id.menu_select_all);
        menu_delete.setVisible(false);
        menu_select_all.setVisible(false);

        SearchView searchView = (SearchView) menu.findItem(R.id.btn_searchBar).getActionView();
        searchView.setQueryHint("Search Data Here ......");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Hike> list = new ArrayList<>();

                for (Hike hike : hikeList){
                    if (hike.getHike_name().toLowerCase().contains(newText.toLowerCase())){
                        list.add(hike);
                    }
                }

                adapter = new HikeAdapter(HikeViewActivity.this, list);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

    public void hideToolbar(){
            toolbar.setVisibility(View.GONE);

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