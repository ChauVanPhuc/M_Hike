package com.example.m_hike.Activity.Obs;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.AddHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Obs;
import com.example.m_hike.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailObsActivity extends AppCompatActivity {

    TextView txt_name, txt_time, txt_level, txt_distance, txt_des, title;
    ImageView showImg;
    Toolbar toolbar;
    Button menu;
    DrawerLayout drawerLayout;
    TextView myFav, myblog, newHike, newStatus, logout;
    Button btn_save, btn_cancel, btn_edit, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_obs);

        getViewsById();
        getData();

        toolbar = findViewById(R.id.myToolBar);
        title = findViewById(R.id.txt_titleToolBar);
        menu = findViewById(R.id.btn_menuBar);
        title.setText("Detail Observation");
        title.setTextSize(26);
        setSupportActionBar(toolbar);


        if (getIntent().hasExtra("Obs_id")){
            btn_cancel.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
        }else {
            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }

        drawerLayout = findViewById(R.id.layoutDrawerDetailObs);
        menu = findViewById(R.id.btn_menuBar);
        myblog = findViewById(R.id.txt_myBlog);
        newHike = findViewById(R.id.txt_newHike);
        newStatus = findViewById(R.id.txt_newStatus);
        myFav = findViewById(R.id.txt_favorites);
        logout = findViewById(R.id.txt_logOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DetailObsActivity.this, LoginActivity.class);
            }
        });
        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DetailObsActivity.this, FavoritesViewActivity.class);
            }
        });

        newStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(DetailObsActivity.this, AddBlogActivity.class);
            }
        });
        newHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHikeActivity.idHike = false;
                redirectActivity(DetailObsActivity.this, AddHikeActivity.class);
            }
        });
        myblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogViewActivity.myBlog = "myBlog";
                redirectActivity(DetailObsActivity.this, BlogViewActivity.class);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlogViewActivity.myBlog = "";
                openDrawer(drawerLayout);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.bottom_navDetailObs);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.blog){
                    BlogViewActivity.myBlog = "";
                    redirectActivity(DetailObsActivity.this, BlogViewActivity.class);
                } else if (id == R.id.profile) {
                    redirectActivity(DetailObsActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.hike) {
                    redirectActivity(DetailObsActivity.this, HikeViewActivity.class);
                }
                return true;
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailObsActivity.this, AddObsActivity.class);

                startActivity(intent);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailObsActivity.this, AddObsActivity.class);
                intent.putExtra("name", txt_name.getText().toString());
                intent.putExtra("time", txt_time.getText().toString());
                intent.putExtra("level", txt_level.getText().toString());
                intent.putExtra("des",txt_des.getText().toString());
                intent.putExtra("dis", txt_distance.getText().toString());
                byte[] img = getIntent().getByteArrayExtra("img");
                intent.putExtra("img", img);
                String obs_id = getIntent().getStringExtra("Obs_id");
                intent.putExtra("Obs_id", obs_id);
                startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                int Obs_id = Integer.parseInt(getIntent().getStringExtra("Obs_id"));
                                deleteObs(Obs_id);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailObsActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }

        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] img = getIntent().getByteArrayExtra("img");

                int hike_id = ObsViewActivity.hike_id;
                Obs obs = new Obs(0, hike_id, txt_name.getText().toString(), txt_time.getText().toString(),
                        txt_level.getText().toString(), txt_distance.getText().toString(), img, txt_des.getText().toString());

                HikeDAO hikeDAO = new HikeDAO(DetailObsActivity.this);

                hikeDAO.insertObs(obs);

                Toast.makeText(DetailObsActivity.this, "Add Observation Success", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DetailObsActivity.this, ObsViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deleteObs(int Obs_id) {
        HikeDAO hikeDAO = new HikeDAO(this);
        hikeDAO.deleteObs(Obs_id);
        Toast.makeText(this, "Delete Success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DetailObsActivity.this, ObsViewActivity.class));
    }

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
            showImg.setImageBitmap(b);

            txt_name.setText(name);
            txt_time.setText(time);
            txt_level.setText(level);
            txt_des.setText(des);
            txt_distance.setText(dis);
        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void getViewsById() {
        txt_name = findViewById(R.id.ed_detailNameObs);
        txt_time = findViewById(R.id.ed_detailTimeObs);
        txt_level = findViewById(R.id.ed_detailLevelObs);
        txt_distance = findViewById(R.id.ed_detailDistanceObs);
        txt_des = findViewById(R.id.ed_detailDescriptionObs);

        showImg = findViewById(R.id.img_detailShowImgObs);

        btn_cancel = findViewById(R.id.btn_detailCancelObs);
        btn_save = findViewById(R.id.btn_detailSaveObs);
        btn_edit = findViewById(R.id.btn_detailEditObs);
        btn_delete = findViewById(R.id.btn_detailDeleteObs);
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