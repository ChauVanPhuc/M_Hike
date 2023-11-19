package com.example.m_hike.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;

import java.util.List;

public class ItemHikeAdapter extends ArrayAdapter<Hike> {
    public ItemHikeAdapter(@NonNull Context context, int resource, @NonNull List<Hike> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select,parent,false);
        TextView select = convertView.findViewById(R.id.txt_nameSelect);

        Hike hike = this.getItem(position);
        if(hike != null){
            select.setText(hike.getHike_name());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hike,parent,false);
        TextView txt_hike = convertView.findViewById(R.id.txt_nameHike);

        Hike hike = this.getItem(position);
        if(hike != null){
            txt_hike.setText(hike.getHike_name());
        }
        return convertView;
    }
}
