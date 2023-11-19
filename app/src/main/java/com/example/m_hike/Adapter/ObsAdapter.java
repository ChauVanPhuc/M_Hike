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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.Activity.Hike.DetailHikeActivity;
import com.example.m_hike.Activity.Obs.DetailObsActivity;
import com.example.m_hike.Activity.Obs.ObsViewActivity;
import com.example.m_hike.Database.HikeDAO;
import com.example.m_hike.Models.Obs;
import com.example.m_hike.R;

import java.util.ArrayList;
import java.util.List;

public class ObsAdapter extends RecyclerView.Adapter<ObsHolderView> {

    ObsViewActivity context;
    List<Obs> list;
    boolean isEnable=false;
    boolean isSelectAll=false;

    ArrayList<Obs> selectList=new ArrayList<>();

    public ObsAdapter(ObsViewActivity context, List<Obs> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ObsHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  ObsHolderView(LayoutInflater.from(context).inflate(R.layout.item_obs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ObsHolderView holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).getObs_name());

        holder.level.setText(list.get(position).getObs_level());

        byte[] img = list.get(position).getObs_img();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        holder.showImg.setBackground(null);
        holder.showImg.setImageBitmap(bitmap);


        if(list.get(position).getObs_level().equals("Safe")){
            holder.level.setTextColor(Color.rgb(173,255,47));
        } else if (list.get(position).getObs_level().equals("Moderate")) {
            holder.level.setTextColor(Color.rgb(255,255,47));
        }else if (list.get(position).getObs_level().equals("Dangerous")) {
            holder.level.setTextColor(Color.rgb(255,100,47));
        }else {
            holder.level.setTextColor(Color.rgb(255,60,20));
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailObsActivity.class);
                intent.putExtra("name", list.get(position).getObs_name());
                intent.putExtra("time", list.get(position).getObs_time());
                intent.putExtra("level", list.get(position).getObs_level());
                intent.putExtra("des",list.get(position).getObs_des());
                intent.putExtra("dis", list.get(position).getObs_distance());
                intent.putExtra("img", list.get(position).getObs_img());
                String ob_id = String.valueOf(list.get(position).getObs_id());
                intent.putExtra("Obs_id", ob_id);
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
                            ClickItem(holder, position);
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
                                                    for (Obs s : selectList) {
                                                        // remove selected item list
                                                        HikeDAO hikeDAO = new HikeDAO(context);
                                                        hikeDAO.deleteObs(s.getObs_id());
                                                        notifyDataSetChanged();

                                                        context.startActivity(new Intent(context, ObsViewActivity.class));
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
//                                if (list.size() == 0) {
//                                    // when array list is empty
//                                    // visible text view
////                                    tvEmpty.setVisibility(View.VISIBLE);
//                                }
//                                // finish action mode
//                                mode.finish();

                            } else if (id == R.id.menu_select_all) {

                                // when click on select all
                                // check condition
                                if (selectList.size() == list.size()) {
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
                                    selectList.addAll(list);
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
                            context.startActivity(new Intent(context, ObsViewActivity.class));
                        }
                    };
                    // start action mode
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                } else {
                    // when action mode is already enable
                    // call method

                    ClickItem(holder, position);

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
                    ClickItem(holder, position);
                } else {

                }
            }
        });

        if (isSelectAll) {
            // when value selected
            // visible all check boc image
            holder.checkbox.setVisibility(View.VISIBLE);
            //set background color
            holder.card_obs.setBackgroundColor(Color.rgb(255 ,218, 185));
        } else {
            /// when all value unselected
            // hide all check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            holder.card_obs.setBackgroundColor(Color.rgb(	186, 186, 186));
        }
    }

    private void ClickItem(ObsHolderView holder, int position) {

        // get selected item value
        Obs s = list.get(holder.getAdapterPosition());
        // check condition
        if(holder.checkbox.getVisibility()==View.GONE)
        {
            // when item not selected
            // visible check box image
            holder.checkbox.setVisibility(View.VISIBLE);
            // set background color
            holder.card_obs.setBackground(null);
            holder.card_obs.setBackgroundColor(Color.rgb(255 ,218, 185));
            // add value in select array list
            selectList.add(s);
        }
        else
        {
            // when item selected
            // hide check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            holder.card_obs.setBackgroundColor(Color.rgb(	186, 186, 186));
            // remove value from select arrayList
            selectList.remove(s);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
