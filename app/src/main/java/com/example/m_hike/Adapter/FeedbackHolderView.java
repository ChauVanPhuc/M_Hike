package com.example.m_hike.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.R;

public class FeedbackHolderView extends RecyclerView.ViewHolder {

    TextView txt_ratting, txt_caption;
    public FeedbackHolderView(@NonNull View itemView) {
        super(itemView);

        txt_caption = itemView.findViewById(R.id.txt_desShowBlog);
        txt_ratting = itemView.findViewById(R.id.txt_rattingBlog);
    }
}
