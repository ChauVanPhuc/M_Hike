package com.example.m_hike.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.R;

public class BlogHolderView extends RecyclerView.ViewHolder{
    TextView txt_nameUser, txt_content, txt_dateBlog, txt_title_Hike, txt_quantityLike;
    ImageView showImgBlog, btn_like;

    Button btn_delete, btn_edit, btn_feedbackBlog;
    LinearLayout btn_itemBlog, buttonMyBlog;
    public BlogHolderView(@NonNull View itemView) {
        super(itemView);

        txt_content = itemView.findViewById(R.id.txt_contentBlog);
        txt_nameUser = itemView.findViewById(R.id.txt_nameUserShare);

        showImgBlog = itemView.findViewById(R.id.img_showImgBlog);
        btn_itemBlog = itemView.findViewById(R.id.btn_itemBlog);
        buttonMyBlog = itemView.findViewById(R.id.buttonMyBlog);
        btn_delete = itemView.findViewById(R.id.btn_deleteBlog);
        btn_edit = itemView.findViewById(R.id.btn_editBlog);
        txt_dateBlog = itemView.findViewById(R.id.txt_dateBlog);
        txt_title_Hike = itemView.findViewById(R.id.txt_title_Hike);
        btn_feedbackBlog = itemView.findViewById(R.id.btn_feedbackBlog);
        btn_like = itemView.findViewById(R.id.btn_likeBlog);
        txt_quantityLike = itemView.findViewById(R.id.txt_quantityLike);
    }
}
