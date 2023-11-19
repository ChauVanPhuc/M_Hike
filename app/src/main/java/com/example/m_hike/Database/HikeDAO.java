package com.example.m_hike.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Authentication.LoginActivity;
import com.example.m_hike.Models.Blog;
import com.example.m_hike.Models.Favorites;
import com.example.m_hike.Models.Feedback;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.Models.LikeBlog;
import com.example.m_hike.Models.Obs;
import com.example.m_hike.Models.User;

import java.util.ArrayList;
import java.util.List;

public class HikeDAO {

    private HikeDB hikeDB;
    private SQLiteDatabase database;

    public HikeDAO(Context context) {
        hikeDB = new HikeDB(context);
        database = hikeDB.getWritableDatabase();
    }


    // check data login
    public boolean checkLogin(String mail, String password){
        Cursor cursor = database.rawQuery("SELECT * FROM user WHERE user_mail = ? and user_password = ?", new String[]{mail, password});

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                int number = cursor.getInt(0);
                ProfileActivity.user_id = number;
                String admin = cursor.getString(4);
                if(admin.equals("admin")){
                    LoginActivity.Admin = true;
                }else {
                    LoginActivity.Admin = false;
                }
            }

            return true;
        }
        return false;
    }

    // add user into database
    public long insertUser(User user){
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUser_name());
        values.put("user_birth", user.getUser_birth());
        values.put("user_gender", user.getUser_gender());
        values.put("user_mail", user.getUser_mail());
        values.put("user_phone", user.getUser_phone());
        values.put("user_address", user.getUser_address());
        values.put("user_job", user.getUser_job());
        values.put("user_password", user.getUser_password());

        long check = database.insert("user", null, values);
        return check;
    }

    public long insertFavorites(Favorites favorites){
        ContentValues values = new ContentValues();
        values.put("hike_id", favorites.getHike_id());
        values.put("user_id", favorites.getUser_id());

        long check = database.insert("favorites", null, values);
        return check;
    }

    public long insertBlog(Blog blog){
        ContentValues values = new ContentValues();
        values.put("hike_id", blog.getHike_id());
        values.put("user_id", blog.getUser_id());
        values.put("blog_caption", blog.getBlog_caption());
        values.put("blog_date", blog.getBlog_date());

        long check = database.insert("blog", null, values);
        return check;
    }

    public long insertFeedback(Feedback feedback){
        ContentValues values = new ContentValues();
        values.put("hike_id", feedback.getHike_id());
        values.put("user_id", feedback.getUser_id());
        values.put("fb_poin", feedback.getFb_poin());
        values.put("fb_des", feedback.getFb_des());

        long check = database.insert("feedback", null, values);
        return check;
    }
    public long updateUser(User user){
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUser_name());
        values.put("user_birth", user.getUser_birth());
        values.put("user_gender", user.getUser_gender());
        values.put("user_mail", user.getUser_mail());
        values.put("user_phone", user.getUser_phone());
        values.put("user_address", user.getUser_address());
        values.put("user_job", user.getUser_job());
        values.put("user_password", user.getUser_password());

        long check = database.update("user",  values, "user_id=?", new String[]{String.valueOf(user.getUser_id())});
        return check;
    }

    public List<Hike> getListHike(){
        List<Hike> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM hike ORDER BY hike_id DESC", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);

            String name = dataContact.getString(1);
            String location = dataContact.getString(2);
            String date = dataContact.getString(3);
            String parking = dataContact.getString(4);
            int length = dataContact.getInt(5);
            String level = dataContact.getString(6);
            String des = dataContact.getString(7);
            byte[] img = dataContact.getBlob(8);
            int user_id = dataContact.getInt(9);

            list.add(new Hike(id, name, location,date,parking,length,level,des,img, user_id));
        }
        return list;
    }

    public Hike getListHike(int hike_id){
        Hike hike = null;
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM hike WHERE "+hike_id+" = hike_id ORDER BY hike_id DESC", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);

            String name = dataContact.getString(1);
            String location = dataContact.getString(2);
            String date = dataContact.getString(3);
            String parking = dataContact.getString(4);
            int length = dataContact.getInt(5);
            String level = dataContact.getString(6);
            String des = dataContact.getString(7);
            byte[] img = dataContact.getBlob(8);
            int user_id = dataContact.getInt(9);

            hike = new Hike(id, name, location,date,parking,length,level,des,img, user_id);
        }
        return hike;
    }

    public List<Blog> getListBlog(){
        List<Blog> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        int user = ProfileActivity.user_id;
        Cursor dataContact = database.rawQuery("SELECT * FROM blog, user WHERE blog.user_id != "+user+" AND blog.user_id = user.user_id ORDER BY blog_id DESC", null);
        while (dataContact.moveToNext()){
            int blog_id = dataContact.getInt(0);

            int hike_id = dataContact.getInt(1);
            int user_id = dataContact.getInt(2);
            String blog_caption = dataContact.getString(3);
            String blog_date = dataContact.getString(4);

            list.add(new Blog(blog_id, hike_id, user_id,blog_caption, blog_date));
        }
        return list;
    }

    public List<Feedback> getListFeedback(int id){
        List<Feedback> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM feedback, hike ON feedback.hike_id = hike.hike_id WHERE hike.hike_id = "+id+"", null);
        while (dataContact.moveToNext()){
            int feedback_id = dataContact.getInt(0);


            int user_id = dataContact.getInt(1);
            int hike_id = dataContact.getInt(2);
            double feedback_poin = dataContact.getInt(4);
            String feedback_des = dataContact.getString(3);

            list.add(new Feedback(feedback_id, user_id, hike_id,feedback_poin, feedback_des));
        }
        return list;
    }

    public List<Blog> getListMyBlog(){
        List<Blog> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();
        int user = ProfileActivity.user_id;

        Cursor dataContact = database.rawQuery("SELECT * FROM blog, user ON blog.user_id = user.user_id WHERE blog.user_id = "+user+" ORDER BY blog_id DESC", null);
        while (dataContact.moveToNext()){
            int blog_id = dataContact.getInt(0);

            int hike_id = dataContact.getInt(1);
            int user_id = dataContact.getInt(2);
            String blog_caption = dataContact.getString(3);
            String blog_date = dataContact.getString(4);

            list.add(new Blog(blog_id, hike_id, user_id,blog_caption, blog_date));
        }
        return list;
    }


    public List<LikeBlog> getListLikeBlog(){
        List<LikeBlog> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();
        int user = ProfileActivity.user_id;

        Cursor dataContact = database.rawQuery("SELECT * FROM likeBlog WHERE likeBlog.user_id = "+user+"", null);
        while (dataContact.moveToNext()){
            int blog_id = dataContact.getInt(0);
            int user_id = dataContact.getInt(1);


            list.add(new LikeBlog(blog_id,user_id));
        }
        return list;
    }

    public List<LikeBlog> getQuantityLikeBlog(int id){
        List<LikeBlog> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM likeBlog, blog WHERE likeBlog.blog_id = blog.blog_id AND blog.blog_id = "+id+" ", null);
        while (dataContact.moveToNext()){
            int blog_id = dataContact.getInt(0);
            int user_id = dataContact.getInt(1);


            list.add(new LikeBlog(blog_id,user_id));
        }
        return list;
    }

    public List<Hike> getListHikeUser(int _id){
        List<Hike> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM hike INNER JOIN user ON hike.user_id = user.user_id WHERE "+_id+" = hike.user_id  ORDER BY hike_id DESC", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);

            String name = dataContact.getString(1);
            String location = dataContact.getString(2);
            String date = dataContact.getString(3);
            String parking = dataContact.getString(4);
            int length = dataContact.getInt(5);
            String level = dataContact.getString(6);
            String des = dataContact.getString(7);
            byte[] img = dataContact.getBlob(8);
            int user_id = dataContact.getInt(9);

            list.add(new Hike(id, name, location,date,parking,length,level,des,img, user_id));
        }
        return list;
    }

    public Hike getHikeBlog(int _id){
        Hike hike = null;
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM hike INNER JOIN blog ON hike.hike_id = blog.hike_id WHERE "+_id+" = hike.hike_id", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);

            String name = dataContact.getString(1);
            String location = dataContact.getString(2);
            String date = dataContact.getString(3);
            String parking = dataContact.getString(4);
            int length = dataContact.getInt(5);
            String level = dataContact.getString(6);
            String des = dataContact.getString(7);
            byte[] img = dataContact.getBlob(8);
            int user_id = dataContact.getInt(9);

            hike = new Hike(id, name, location,date,parking,length,level,des,img, user_id);
        }
        return hike;
    }

    public List<Feedback> getFeedbackHike(int _id){
        List<Feedback> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM feedback, hike " +
                "WHERE feedback.hike_id = hike.hike_id AND hike.hike_id = "+_id+"", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);

            int hike_id = Integer.parseInt(dataContact.getString(1));
            int user_id = Integer.parseInt(dataContact.getString(2));
            String caption = dataContact.getString(3);
            double ratting = Double.parseDouble(dataContact.getString(4));

            list.add(new Feedback(id, hike_id, user_id,ratting,caption));
        }
        return list;
    }



    public List<Hike> getListFavUser(){
        List<Hike> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();
        int user_id = ProfileActivity.user_id;

        Cursor dataContact = database.rawQuery("SELECT * FROM hike, user, favorites " +
                "WHERE user.user_id = hike.user_id AND favorites.hike_id = hike.hike_id AND favorites.user_id = "+user_id+"", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);

            String name = dataContact.getString(1);
            String location = dataContact.getString(2);
            String date = dataContact.getString(3);
            String parking = dataContact.getString(4);
            int length = dataContact.getInt(5);
            String level = dataContact.getString(6);
            String des = dataContact.getString(7);
            byte[] img = dataContact.getBlob(8);
            int userid = dataContact.getInt(9);

            int fav_id = dataContact.getInt(19);


            list.add(new Hike(id, name, location,date,parking,length,level,des,img, userid));
        }
        return list;
    }

    public List<Favorites> getListFav(){
        List<Favorites> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();
        int user_id = ProfileActivity.user_id;

        Cursor dataContact = database.rawQuery("SELECT * FROM favorites ,hike, user " +
                "WHERE user.user_id = hike.user_id AND favorites.hike_id = hike.hike_id AND favorites.user_id = "+user_id+"", null);
        while (dataContact.moveToNext()){
            int id = dataContact.getInt(0);
            int id_hike = dataContact.getInt(1);
            int id_user = dataContact.getInt(2);



            list.add(new Favorites(id, id_hike, id_user));
        }
        return list;
    }



    public long insertHike(Hike hike){
        ContentValues values = new ContentValues();
        values.put("hike_name", hike.getHike_name());
        values.put("hike_location", hike.getHike_location());
        values.put("hike_date", hike.getHike_date());
        values.put("hike_parking", hike.getHike_parking());
        values.put("hike_length", hike.getHike_length());
        values.put("hike_level", hike.getHike_level());
        values.put("hike_des", hike.getHike_description());
        values.put("hike_img", hike.getHike_img());
        values.put("user_id", hike.getUser_id());

        long check = database.insert("hike", null, values);
        return check;
    }

    public long deleteHike(int id){
        long check = database.delete("hike", "hike_id = ?", new String[]{String.valueOf(id)});
        database.delete("blog", "hike_id = ?", new  String[]{String.valueOf(id)});
        return check;
    }

    public long deleteLikeBlog(int id){
        long check = database.delete("likeBlog", "user_id = ?", new String[]{String.valueOf(id)});
        return check;
    }

    public long deleteBlog(int id){
        long check = database.delete("blog", "blog_id = ?", new String[]{String.valueOf(id)});
        return check;
    }

    public long deleteObs(int id){
        long check = database.delete("obs", "obs_id = ?", new String[]{String.valueOf(id)});
        return check;
    }

    public long deleteFav(int id){
        long check = database.delete("favorites", "hike_id = ?", new String[]{String.valueOf(id)});
        return check;
    }
    public long updateHike(Hike hike){
        ContentValues values = new ContentValues();
        values.put("hike_name", hike.getHike_name());
        values.put("hike_location", hike.getHike_location());
        values.put("hike_date", hike.getHike_date());
        values.put("hike_parking", hike.getHike_parking());
        values.put("hike_length", hike.getHike_length());
        values.put("hike_level", hike.getHike_level());
        values.put("hike_des", hike.getHike_description());
        values.put("hike_img", hike.getHike_img());
        values.put("user_id", hike.getUser_id());

        long check = database.update("hike", values, "hike_id=?",
                new String[]{String.valueOf(hike.getHike_id())});
        return check;
    }

    public long updateBlog(Blog blog){
        ContentValues values = new ContentValues();
        values.put("hike_id", blog.getHike_id());
        values.put("user_id", blog.getUser_id());
        values.put("blog_caption", blog.getBlog_caption());
        values.put("blog_date", blog.getBlog_date());

        long check = database.update("blog", values, "blog_id=?",
                new String[]{String.valueOf(blog.getBlog_id())});
        return check;
    }

    public long insertLikeBlog(LikeBlog likeBlog){
        ContentValues values = new ContentValues();
        values.put("blog_id", likeBlog.getBlog_id());
        values.put("user_id", likeBlog.getUser_id());
        long check = database.insert("likeBlog", null, values);
        return check;
    }

    public long insertObs(Obs observations){
        ContentValues values = new ContentValues();
        values.put("hike_id", observations.getHike_id());
        values.put("obs_name", observations.getObs_name());
        values.put("obs_time", observations.getObs_time());
        values.put("obs_level", observations.getObs_level());
        values.put("obs_distance", observations.getObs_distance());
        values.put("obs_img",observations.getObs_img());
        values.put("obs_des", observations.getObs_des());
        long check = database.insert("obs", null, values);
        return check;
    }
    public long updateObs(Obs observations){
        ContentValues values = new ContentValues();
        values.put("hike_id", observations.getHike_id());
        values.put("obs_name", observations.getObs_name());
        values.put("obs_time", observations.getObs_time());
        values.put("obs_level", observations.getObs_level());
        values.put("obs_distance", observations.getObs_distance());
        values.put("obs_img",observations.getObs_img());
        values.put("obs_des", observations.getObs_des());

        long check = database.update("obs", values, "obs_id=?",
                new String[]{String.valueOf(observations.getObs_id())});
        return check;
    }

    public List<Obs> getListObs(int _id){
        List<Obs> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor dataContact = database.rawQuery("SELECT * FROM obs INNER JOIN hike ON hike.hike_id = obs.hike_id WHERE "+_id+" = hike.hike_id", null);
        while (dataContact.moveToNext()){

            int id = dataContact.getInt(0);
            int hike_id = dataContact.getInt(1);
            String name = dataContact.getString(2);
            String time  = dataContact.getString(3);
            String level = dataContact.getString(4);
            String distance = dataContact.getString(5);
            byte[] img = dataContact.getBlob(6);
            String des = dataContact.getString(7);

            list.add(new Obs(id,hike_id, name, time,level, distance,img, des));
        }
        return list;
    }

    public List<User> getUser(){
        List<User> list = new ArrayList<>();
        database = hikeDB.getReadableDatabase();

        Cursor data= database.rawQuery("SELECT * FROM user", null);

        while (data.moveToNext()){
            int userid = data.getInt(0);
            String name = data.getString(1);
            String birth = data.getString(2);
            String gender = data.getString(3);
            String mail = data.getString(4);
            String phone = data.getString(5);
            String address = data.getString(6);
            String job = data.getString(7);
            String password = data.getString(8);

            list.add(new User(userid, name, birth, gender, mail, phone, address, job, password));
        }

        return list;
    }

    public User getUser(int id){
        User user = null;
        database = hikeDB.getReadableDatabase();

        Cursor data= database.rawQuery("SELECT * FROM user WHERE user_id = "+id+"", null);

        while (data.moveToNext()){
            int userid = data.getInt(0);
            String name = data.getString(1);
            String birth = data.getString(2);
            String gender = data.getString(3);
            String mail = data.getString(4);
            String phone = data.getString(5);
            String address = data.getString(6);
            String job = data.getString(7);
            String password = data.getString(8);

           user = new User(userid, name, birth, gender, mail, phone, address, job, password);
        }
        return user;
    }
}
