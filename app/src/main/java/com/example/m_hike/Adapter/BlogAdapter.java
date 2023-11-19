package com.example.m_hike.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.Activity.Blog.AddBlogActivity;
import com.example.m_hike.Activity.Blog.BlogViewActivity;
import com.example.m_hike.Activity.FeedbackViewActivity;
import com.example.m_hike.Activity.Hike.DetailHikeActivity;
import com.example.m_hike.Activity.ShowFeedback;
import com.example.m_hike.Authentication.ProfileActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Blog;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.Models.LikeBlog;
import com.example.m_hike.Models.User;
import com.example.m_hike.R;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogHolderView>{


    Activity context;
    List<Blog> list;
    boolean flag = false;

    public BlogAdapter(Activity context, List<Blog> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public BlogHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  BlogHolderView(LayoutInflater.from(context).inflate(R.layout.item_blog, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlogHolderView holder, @SuppressLint("RecyclerView") int position) {



        HikeDAO hikeDAO = new HikeDAO(context);
        User user = hikeDAO.getUser(list.get(position).getUser_id());
        Hike hike = hikeDAO.getHikeBlog(list.get(position).getHike_id());
        String user_name = user.getUser_name();
        holder.txt_nameUser.setText(user_name + " shared a Hike");

        holder.txt_content.setText(list.get(position).getBlog_caption());
        holder.txt_title_Hike.setText(hike.getHike_name());
        holder.txt_dateBlog.setText(list.get(position).getBlog_date());

        String quantity = String.valueOf(checkQuantityLike(list.get(position).getBlog_id()));
        holder.txt_quantityLike.setText(quantity);




        if(hike != null){
            byte[] img = hike.getHike_img();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            holder.showImgBlog.setBackground(null);
            holder.showImgBlog.setImageBitmap(bitmap);
        }

        String myBlog = BlogViewActivity.myBlog;

        if(!myBlog.equals("")){
            holder.buttonMyBlog.setVisibility(View.VISIBLE);
        } else {
            holder.buttonMyBlog.setVisibility(View.GONE);
        }



        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hike hike = hikeDAO.getHikeBlog(list.get(position).getHike_id());

                Intent intent = new Intent(context, AddBlogActivity.class);

                intent.putExtra("nameHike", hike.getHike_name());
                String hike_id = String.valueOf(list.get(position).getHike_id());

                intent.putExtra("hike_id", hike_id);
                String blog_id = String.valueOf(list.get(position).getBlog_id());

                intent.putExtra("blog_id",blog_id );
                intent.putExtra("caption", list.get(position).getBlog_caption());

                AddBlogActivity.id = true;
                context.startActivity(intent);
            }
        });

        holder.btn_feedbackBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedbackViewActivity.class);
                FeedbackViewActivity.hike_id = list.get(position).getHike_id();
                context.startActivity(intent);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                int Blog_id = list.get(position).getBlog_id();
                                hikeDAO.deleteBlog(Blog_id);

                                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, BlogViewActivity.class));
                                context.overridePendingTransition(0,0);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        holder.btn_itemBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailHikeActivity.class);

                String blog_id = String.valueOf(list.get(position).getBlog_id());
                String hike_id = String.valueOf(list.get(position).getHike_id());

                intent.putExtra("blog_id",blog_id);
                intent.putExtra("hike_id",hike_id);

                int hike_ = list.get(position).getHike_id();

                ShowFeedback.hike_id = hike_;
                context.startActivity(intent);
            }
        });


        if(checkLike(position)){
            holder.btn_like.setBackgroundResource(R.drawable.fav_r);
        }

        checkQuantityLike(list.get(position).getBlog_id());

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LikeBlog likeBlog = new LikeBlog(list.get(position).getBlog_id() ,ProfileActivity.user_id);



                if(checkLike(position)){
                    hikeDAO.deleteLikeBlog(ProfileActivity.user_id);
                }else {
                    hikeDAO.insertLikeBlog(likeBlog);

                }
                checkQuantityLike(list.get(position).getBlog_id());

                notifyItemChanged(position);
            }
        });
    }


    private int checkQuantityLike(int position) {

        HikeDAO hikeDAO = new HikeDAO(context);
        List<LikeBlog> likeBlogs = hikeDAO.getQuantityLikeBlog(position);

        return likeBlogs.size();
    }

    private boolean checkLike(int position) {

        HikeDAO hikeDAO = new HikeDAO(context);
        List<LikeBlog> likeBlogs = hikeDAO.getListLikeBlog();

        for (LikeBlog l : likeBlogs){
            if(l.getBlog_id() == list.get(position).getBlog_id()){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
