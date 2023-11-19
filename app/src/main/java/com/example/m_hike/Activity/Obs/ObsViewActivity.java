package com.example.m_hike.Activity.Obs;

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
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Adapter.ObsAdapter;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Obs;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ObsViewActivity extends AppCompatActivity {

    Button btn_addObs, menu;
    HikeDAO hikeDAO;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ObsAdapter adapter;

    DrawerLayout drawerLayout;
    TextView  myFav, myblog, newHike, newStatus, logout, title;
    TextView txt_tileHike;
    List<Obs> listObs = new ArrayList<>();

    public static int hike_id;
    public static String hike_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_view);

        btn_addObs = findViewById(R.id.btn_addObs);

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        title.setText("List Observations");
        title.setTextSize(26);
        setSupportActionBar(toolbar);

        menu = findViewById(R.id.btn_menuBar);
        txt_tileHike = findViewById(R.id.txt_tileHike);

        hikeDAO = new HikeDAO(this);
        recyclerView = findViewById(R.id.reViewShowObs);
        listObs.clear();

        listObs = hikeDAO.getListObs(hike_id);



        adapter = new ObsAdapter(this, listObs);
        recyclerView.setLayoutManager(new LinearLayoutManager(ObsViewActivity.this));
        recyclerView.setAdapter(adapter);


        txt_tileHike.setText(hike_name);


        drawerLayout = findViewById(R.id.drawer_layoutObs);

        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        myFav = findViewById(R.id.txt_favorites);
        logout = findViewById(R.id.txt_logOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ObsViewActivity.this, LoginActivity.class);
            }
        });
        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ObsViewActivity.this, FavoritesViewActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(ObsViewActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(ObsViewActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(ObsViewActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navObs);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(ObsViewActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(ObsViewActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(ObsViewActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });


        btn_addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ObsViewActivity.this, AddObsActivity.class);

                intent.putExtra("hike_id", hike_id);

                startActivity(intent);
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
                List<Obs> list = new ArrayList<>();

                for (Obs obs : listObs){
                    if (obs.getObs_name().toLowerCase().contains(newText.toLowerCase())){
                        list.add(obs);
                    }
                }

                adapter = new ObsAdapter(ObsViewActivity.this, list);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

    public void hideToolbar(){
        toolbar.setVisibility(View.GONE);

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