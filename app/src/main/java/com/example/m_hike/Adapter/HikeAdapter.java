package com.example.m_hike.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.Activity.Hike.DetailHikeActivity;
import com.example.m_hike.Activity.Hike.HikeViewActivity;
import com.example.m_hike.Activity.Obs.ObsViewActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Feedback;
import com.example.m_hike.Models.Hike;
import com.example.m_hike.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HikeAdapter extends RecyclerView.Adapter<HikeHolderView>{

    HikeViewActivity context;
    List<Hike> hikeList;
    boolean isEnable=false;
    boolean isSelectAll=false;
    boolean flag = false;
    ArrayList<Hike> selectList=new ArrayList<>();
    public HikeAdapter(HikeViewActivity context, List<Hike> hikeList) {
        this.context = context;
        this.hikeList = hikeList;
    }

    @NonNull
    @Override
    public HikeHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  HikeHolderView(LayoutInflater.from(context).inflate(R.layout.item_hike, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HikeHolderView holder, @SuppressLint("RecyclerView") int position) {

        holder.txt_name.setText(hikeList.get(position).getHike_name());
        holder.txt_date.setText(hikeList.get(position).getHike_date());
        String length = String.valueOf(hikeList.get(position).getHike_length());
        holder.txt_km.setText(length);
        holder.txt_level.setText(hikeList.get(position).getHike_level());


        double poin = 0;
        HikeDAO hikeDAO = new HikeDAO(context);
        int hike_id = hikeList.get(position).getHike_id();
        List<Feedback> listFB = hikeDAO.getListFeedback(hike_id);
        for (Feedback f : listFB) {
            poin += f.getFb_poin();
        }
        String total_poin;
        if (listFB.size() == 0) {
            total_poin = String.valueOf(0);
        } else {
            total_poin = String.valueOf(poin / listFB.size());
        }
        holder.txt_poin.setText(total_poin);


        if(hikeList.get(position).getHike_level().equals("Easy")){
            holder.txt_level.setTextColor(Color.parseColor("#33FF33"));
        } else if (hikeList.get(position).getHike_level().equals("Medium")) {
            holder.txt_level.setTextColor(Color.parseColor("#FFFF33"));
        }else {
            holder.txt_level.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailHikeActivity.class);
                intent.putExtra("name", hikeList.get(position).getHike_name());
                intent.putExtra("location", hikeList.get(position).getHike_location());
                intent.putExtra("date", hikeList.get(position).getHike_date());
                intent.putExtra("parking", hikeList.get(position).getHike_parking());
                String length = String.valueOf(hikeList.get(position).getHike_length());
                intent.putExtra("length", length);
                intent.putExtra("level", hikeList.get(position).getHike_level());
                intent.putExtra("des", hikeList.get(position).getHike_description());
                byte[] img = hikeList.get(position).getHike_img();
                intent.putExtra("img", img);
                String idHike = String.valueOf(hikeList.get(position).getHike_id());
                intent.putExtra("idHike", idHike);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // check condition
                if (!isEnable) {

                    // initialize action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            // initialize menu inflater
                            MenuInflater menuInflater = mode.getMenuInflater();
                            // inflate menu
                            menuInflater.inflate(R.menu.menu, menu);
                            // return true
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // when action mode is prepare
                            // set isEnable true
                            isEnable = true;
                            // create method
                            ClickItem(holder);
                            context.hideToolbar();
                            MenuItem item = menu.findItem(R.id.btn_searchBar);
                            item.setVisible(false);
                            // return true
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            // when click on action mode item
                            // get item  id
                            int id = item.getItemId();
                            // use switch condition
                            if (id == R.id.menu_delete) {
                                // when click on delete
                                // use for loop


                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:

                                                for (Hike s : selectList) {
                                                    // remove selected item list
                                                    HikeDAO hikeDAO = new HikeDAO(context);
                                                    hikeDAO.deleteHike(s.getHike_id());
                                                    notifyDataSetChanged();

                                                    context.startActivity(new Intent(context, HikeViewActivity.class));

                                                }

                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked

                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("The selected ones will be deleted \nAre you sure?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

//                                // check condition
//                                if (hikeList.size() == 0) {
//                                    // when array list is empty
//                                    // visible text view
////                                    tvEmpty.setVisibility(View.VISIBLE);
//                                }
//                                // finish action mode
//                                mode.finish();

                            } else if (id == R.id.menu_select_all) {

                                // when click on select all
                                // check condition
                                if (selectList.size() == hikeList.size()) {
                                    // when all item selected
                                    // set isselectall false
                                    isSelectAll = false;
                                    // create select array list
                                    selectList.clear();
                                } else {
                                    // when  all item unselected
                                    // set isSelectALL true
                                    isSelectAll = true;
                                    // clear select array list
                                    selectList.clear();
                                    // add value in select array list
                                    selectList.addAll(hikeList);
                                }
                                // set text on view model
//                                mainViewModel.setText(String.valueOf(selectList.size()));
                                // notify adapter
                                notifyDataSetChanged();
                            }
                            // return true
                            return true;
                        }


                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            // when action mode is destroy
                            // set isEnable false
                            isEnable = false;
                            // set isSelectAll false
                            isSelectAll = false;
                            // clear select array list
                            selectList.clear();
                            // notify adapter
                            notifyDataSetChanged();
                            context.startActivity(new Intent(context, HikeViewActivity.class));
                        }
                    };
                    // start action mode
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                } else {
                    // when action mode is already enable
                    // call method

                    ClickItem(holder);

                }
                // return true
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition
                if (isEnable) {
                    // when action mode is enable
                    // call method
                    ClickItem(holder);
                } else {

                    ObsViewActivity.hike_id = hikeList.get(position).getHike_id();
                    Intent intent = new Intent(context, ObsViewActivity.class);
                    ObsViewActivity.hike_name = hikeList.get(position).getHike_name();
                    context.startActivity(intent);
                }
            }
        });

        if (isSelectAll) {
            // when value selected
            // visible all check boc image
            holder.checkbox.setVisibility(View.VISIBLE);
            //set background color
            holder.card_hike.setBackground(null);
            holder.card_hike.setBackgroundColor(Color.rgb(255, 218, 185));
        } else {
            // when all value unselected
            // hide all check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            holder.card_hike.setBackgroundColor(Color.rgb(	186, 186, 186));
        }
    }

        @SuppressLint("ResourceAsColor")
        private void ClickItem(HikeHolderView holder) {

            // get selected item value
            Hike s = hikeList.get(holder.getAdapterPosition());
            // check condition
            if(holder.checkbox.getVisibility()==View.GONE)
            {
                // when item not selected
                // visible check box image
                holder.checkbox.setVisibility(View.VISIBLE);
                holder.card_hike.setBackground(null);
                // set background color
                holder.card_hike.setBackgroundColor(Color.rgb(255, 218, 185));
                // add value in select array list
                selectList.add(s);
            }
            else
            {
                // when item selected
                // hide check box image
                holder.checkbox.setVisibility(View.GONE);
                // set background color
                holder.card_hike.setBackgroundColor(Color.rgb(	186, 186, 186));
                // remove value from select arrayList
                selectList.remove(s);
            }
        }



    @Override
    public int getItemCount() {
        return hikeList.size();
    }
}
