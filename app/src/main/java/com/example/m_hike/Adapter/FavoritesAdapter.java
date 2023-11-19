package com.example.m_hike.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.Activity.Favorites.FavoritesViewActivity;
import com.example.m_hike.Activity.Hike.DetailHikeActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesHolderView>{

    Activity context;
    List<Hike> hikeList;

    public FavoritesAdapter(Activity context, List<Hike> hikeList) {
        this.context = context;
        this.hikeList = hikeList;
    }
    @NonNull
    @Override
    public FavoritesHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  FavoritesHolderView(LayoutInflater.from(context).inflate(R.layout.item_favorites, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolderView holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_name.setText(hikeList.get(position).getHike_name());
        holder.txt_date.setText(hikeList.get(position).getHike_date());
        String length = String.valueOf(hikeList.get(position).getHike_length());
        holder.txt_km.setText(length);
        holder.txt_level.setText(hikeList.get(position).getHike_level());

        holder.btn_deleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HikeDAO hikeDAO = new HikeDAO(context);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:


                                hikeDAO.deleteFav(hikeList.get(position).getHike_id());
                                redirectActivity(context, FavoritesViewActivity.class);
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
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
