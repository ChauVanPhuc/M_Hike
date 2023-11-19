package com.example.m_hike.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.R;

public class FavoritesHolderView extends RecyclerView.ViewHolder{
    TextView txt_name, txt_date, txt_km, txt_level, txt_poin, btn_deleteFav;
    CardView card_hike;
    public FavoritesHolderView(@NonNull View itemView) {
        super(itemView);

        txt_name = itemView.findViewById(R.id.txt_nameHikeF);
        txt_date = itemView.findViewById(R.id.txt_dateHikeF);
        txt_km = itemView.findViewById(R.id.txt_kmHikeF);
        txt_level = itemView.findViewById(R.id.txt_levelHikeF);
        txt_poin = itemView.findViewById(R.id.txt_poinHikeF);
        btn_deleteFav = itemView.findViewById(R.id.btn_deleteFav);

        card_hike = itemView.findViewById(R.id.card_hikeF);
    }
}
