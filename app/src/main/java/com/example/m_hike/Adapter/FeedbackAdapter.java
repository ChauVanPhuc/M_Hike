package com.example.m_hike.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike.Models.Feedback;
import com.example.m_hike.R;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackHolderView> {

    Context context;
    List<Feedback> feedbackList;

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  FeedbackHolderView(LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackHolderView holder, int position) {
        if (feedbackList.get(position).getFb_des() !=null ){
            holder.txt_caption.setText(feedbackList.get(position).getFb_des());
        }

        String ratting = String.valueOf(feedbackList.get(position).getFb_poin());
        holder.txt_ratting.setText(ratting);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
}
