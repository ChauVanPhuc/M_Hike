package com.example.m_hike.Activity.Blog;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Adapter.ItemHikeAdapter;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Blog;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddBlogActivity extends AppCompatActivity {

    Spinner sp_nameHike;
    ItemHikeAdapter adapter;
    EditText ed_content;
    Toolbar toolbar;
    TextView title;
    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout;
    Button menu;
    Button btn_submit, btn_back, btn_update, btn_cancel;
    int hike_id;
    String hike_name;
    RelativeLayout form_add, form_edit;
    public static boolean id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        sp_nameHike = findViewById(R.id.sp_nameHike);
        btn_back = findViewById(R.id.btn_backAddBlog);
        btn_submit = findViewById(R.id.btn_submitAddBlog);
        ed_content = findViewById(R.id.ed_content);
        btn_update = findViewById(R.id.btn_updateBlog);
        btn_cancel = findViewById(R.id.btn_cancelUpdateBlog);
        form_add = findViewById(R.id.form_add);
        form_edit = findViewById(R.id.form_edit);
        getData();

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);

        setSupportActionBar(toolbar);

        adapter = new ItemHikeAdapter(this, R.layout.item_select, getListHike());
        sp_nameHike.setAdapter(adapter);
        menu = findViewById(R.id.btn_menuBar);
        // Button user to show Menu

        title = findViewById(R.id.txt_titleToolBar);

        if(!id){
            form_edit.setVisibility(View.GONE);
            title.setText("Add Blog");
        }else {
            form_add.setVisibility(View.GONE);
            title.setText("Edit Blog");
        }


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

                Calendar calendar = Calendar.getInstance();
                int H = calendar.get(Calendar.HOUR_OF_DAY);
                int M = calendar.get(Calendar.MINUTE);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                int Month = calendar.get(Calendar.MONTH);
                int Year = calendar.get(Calendar.YEAR);

                String date = ""+Day+"/" + ""+Month+"/" + ""+Year+"  " + ""+H+":" + ""+M+"";

                int blog_id = Integer.parseInt(getIntent().getStringExtra("blog_id"));
                Blog blog = new Blog(blog_id, hike_id, ProfileActivity.user_id,ed_content.getText().toString(), date);
                HikeDAO hikeDAO = new HikeDAO(AddBlogActivity.this);
                hikeDAO.updateBlog(blog);
                Toast.makeText(AddBlogActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddBlogActivity.this, BlogViewActivity.class));
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getListHike().size() == 0){
                    Toast.makeText(AddBlogActivity.this, "Please, Create a Hike", Toast.LENGTH_SHORT).show();
                }else {
                    if(ed_content.getText().toString().equals("")){
                        ed_content.setError("Enter content");
                    }else {
                        Intent intent = new Intent(AddBlogActivity.this, DetailBlogActivity.class);
                        String hikeid = String.valueOf(hike_id);
                        intent.putExtra("hike_id", hikeid);
                        intent.putExtra("hike_name", hike_name);
                        intent.putExtra("content", ed_content.getText().toString());

                        startActivity(intent);
                    }
                }


            }
        });



        sp_nameHike.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hike_id = adapter.getItem(position).getHike_id();
                hike_name = adapter.getItem(position).getHike_name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        drawerLayout = findViewById(R.id.layoutDrawerAddBlog);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        logout = findViewById(R.id.txt_logOut);
        myFav = findViewById(R.id.txt_favorites);

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AddBlogActivity.this, FavoritesViewActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AddBlogActivity.this, LoginActivity.class);
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
                redirectActivity(AddBlogActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(AddBlogActivity.this, BlogViewActivity.class);
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
                    redirectActivity(AddBlogActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(AddBlogActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(AddBlogActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });

    }

    private void getData(){
        if(getIntent().hasExtra("nameHike") && getIntent().hasExtra("caption") &&
                getIntent().hasExtra("hike_id") && getIntent().hasExtra("blog_id")) {
            
            String caption = getIntent().getStringExtra("caption");

            int hike_id = Integer.parseInt(getIntent().getStringExtra("hike_id"));

            sp_nameHike.setSelection(hike_id-1);
            ed_content.setText(caption);
        }
    }


    private List<Hike> getListHike() {
        List<Hike> hikeList = new ArrayList<>();

        HikeDAO hikeDAO = new HikeDAO(this);
        hikeList = hikeDAO.getListHikeUser(ProfileActivity.user_id);


        return hikeList;
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