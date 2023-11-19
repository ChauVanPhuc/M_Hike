package com.example.m_hike.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.R;

public class ObsHolderView extends RecyclerView.ViewHolder {
    TextView name, level, edit;
    LinearLayout btn_itemStreet;
    ImageView showImg;
    ImageView checkbox;
    LinearLayout card_obs;
    public ObsHolderView(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.txt_nameObs);
        edit = itemView.findViewById(R.id.btn_editObs);
        level = itemView.findViewById(R.id.txt_levelObs);

        showImg = itemView.findViewById(R.id.img_showImgObs);
        btn_itemStreet = itemView.findViewById(R.id.btn_itemStreet);
        checkbox=itemView.findViewById(R.id.check_boxObs);
        card_obs = itemView.findViewById(R.id.card_obs);
    }
}
