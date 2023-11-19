package com.example.m_hike.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.R;

public class HikeHolderView extends RecyclerView.ViewHolder{

    TextView txt_name, btn_edit, txt_date, txt_km, txt_level, txt_poin;
    LinearLayout card_hike;
    ImageView checkbox;

    public HikeHolderView(@NonNull View itemView) {
        super(itemView);

        txt_name = itemView.findViewById(R.id.txt_nameHike);
        txt_date = itemView.findViewById(R.id.txt_dateHike);
        txt_km = itemView.findViewById(R.id.txt_kmHike);
        txt_level = itemView.findViewById(R.id.txt_levelHike);
        txt_poin = itemView.findViewById(R.id.txt_poinHike);

        btn_edit = itemView.findViewById(R.id.btn_editHike);

        card_hike = itemView.findViewById(R.id.card_hike);
        checkbox=itemView.findViewById(R.id.check_box);


    }
}
