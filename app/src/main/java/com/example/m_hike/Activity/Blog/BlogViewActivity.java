package com.example.m_hike.Activity.Blog;

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

import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Adapter.BlogAdapter;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Blog;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BlogViewActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button menu, btn_addBlog;;

    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout, title, btn_showFeedback;
    HikeDAO hikeDAO;
    RecyclerView recyclerView;

    Button like, feedback;
    BlogAdapter adapter;
    public static String myBlog = "";
    List<Blog> blogList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_view);

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);


        setSupportActionBar(toolbar);

        menu = findViewById(R.id.btn_menuBar);
        btn_addBlog = findViewById(R.id.btn_addBlog);

        btn_showFeedback = findViewById(R.id.btn_showFeedback);

        hikeDAO = new HikeDAO(this);
        recyclerView = findViewById(R.id.reViewShowBlog);
        blogList.clear();

        if(myBlog.equals("")){
            title.setText("View Blog");
            blogList = hikeDAO.getListBlog();
        }else {
            title.setText("View My Blog");
            blogList = hikeDAO.getListMyBlog();
        }


        drawerLayout = findViewById(R.id.layoutDrawerBlog);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        btn_showFeedback = findViewById(R.id.btn_showFeedback);
        myFav = findViewById(R.id.txt_favorites);


        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(BlogViewActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(BlogViewActivity.this, LoginActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(BlogViewActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(BlogViewActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(BlogViewActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });


        BottomNavigationView navigationView = findViewById(R.id.bottom_navBlog);
        navigationView.setSelectedItemId(R.id.blog);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    myBlog = "";
                    redirectActivity(BlogViewActivity.this, BlogViewActivity.class);
                    overridePendingTransition(0,0);
                } else if (id == R.id.profile) {
                    redirectActivity(BlogViewActivity.this, ProfileActivity.class);
                    overridePendingTransition(0,0);
                }else if (id == R.id.hike) {
                    redirectActivity(BlogViewActivity.this, HikeViewActivity.class);
                    overridePendingTransition(0,0);
                }
                return true;
            }
        });


        adapter = new BlogAdapter(this, blogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(BlogViewActivity.this));
        recyclerView.setAdapter(adapter);



        // Button user to show Activity Add Blog
        btn_addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBlogActivity.id = false;
                startActivity(new Intent(BlogViewActivity.this, AddBlogActivity.class));
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
                List<Blog> list = new ArrayList<>();

                for (Blog blog : blogList){
                    if (blog.getBlog_caption().toLowerCase().contains(newText.toLowerCase())){
                        list.add(blog);
                    }
                }

                adapter = new BlogAdapter(BlogViewActivity.this, list);
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