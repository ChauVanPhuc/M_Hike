package com.example.m_hike.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HikeDB extends SQLiteOpenHelper {

    public HikeDB(Context context) {
        super(context, "Hike.sqlite", null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create Hike data table
        String hike = "CREATE TABLE IF NOT EXISTS hike " +
                "(hike_id INTEGER PRIMARY KEY AUTOINCREMENT, hike_name VARCHAR(50), hike_location VARCHAR(50)," +
                "hike_date VARCHAR(50), hike_parking VARCHAR(50),hike_length VARCHAR(50),hike_level VARCHAR(50), " +
                "hike_des VARCHAR(50), hike_img BLOB, user_id INTEGER, FOREIGN KEY('user_id') REFERENCES user('user_id'))";

        // Create Obs data table
        String obs = "CREATE TABLE IF NOT EXISTS obs " +
                "(obs_id INTEGER PRIMARY KEY AUTOINCREMENT, hike_id INTEGER, obs_name VARCHAR(50), obs_time VARCHAR(50), obs_level VARCHAR(50)," +
                "obs_distance VARCHAR(50), obs_img BLOB,obs_des VARCHAR(50),  FOREIGN KEY('hike_id') REFERENCES hike('hike_id'))";

        // Create favorites data table
        String favorites = "CREATE TABLE IF NOT EXISTS favorites" +
                "(favorites_id INTEGER PRIMARY KEY AUTOINCREMENT,hike_id INTEGER, user_id INTEGER, FOREIGN KEY('hike_id') REFERENCES hike('hike_id'), FOREIGN KEY('user_id') REFERENCES user('user_id')) ";

        // Create User data table
        String User = "CREATE TABLE IF NOT EXISTS user " +
                "(user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name VARCHAR(50), user_birth VARCHAR(50)," +
                " user_gender VARCHAR(50), user_mail VARCHAR(50), user_phone VARCHAR(50), user_address VARCHAR(50), user_job VARCHAR(50), user_password VARCHAR(50))";

        // Create blog data table
        String blog = "CREATE TABLE IF NOT EXISTS blog" +
                "(blog_id INTEGER PRIMARY KEY AUTOINCREMENT,hike_id INTEGER, user_id INTEGER, blog_caption VARCHAR(50), blog_date VARCHAR(50), FOREIGN KEY('hike_id') REFERENCES hike('hike_id'), FOREIGN KEY('user_id') REFERENCES user('user_id')) ";

        // Create feedback data table
        String feedback = "CREATE TABLE IF NOT EXISTS feedback" +
                "(feedback_id INTEGER PRIMARY KEY AUTOINCREMENT,hike_id INTEGER, user_id INTEGER, fb_des VARCHAR(50), fb_poin DOUBLE, FOREIGN KEY('hike_id') REFERENCES hike('hike_id'), FOREIGN KEY('user_id') REFERENCES user('user_id')) ";


        String likeBlog = "CREATE TABLE IF NOT EXISTS likeBlog" +
                "(blog_id INTEGER ,user_id INTEGER,  FOREIGN KEY('blog_id') REFERENCES blog('blog_id'), FOREIGN KEY('user_id') REFERENCES user('user_id')) ";

        // Execute the table creation statement
        sqLiteDatabase.execSQL(User);
        sqLiteDatabase.execSQL(hike);
        sqLiteDatabase.execSQL(obs);
        sqLiteDatabase.execSQL(favorites);
        sqLiteDatabase.execSQL(blog);
        sqLiteDatabase.execSQL(feedback);
        sqLiteDatabase.execSQL(likeBlog);
    }

    // update Version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i!=i1){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS hike");
            onCreate(sqLiteDatabase);
        }
    }
}
