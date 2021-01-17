package com.supertridents.learn.digital.marketing.quiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {
    ArrayList<RecyclerModel> list;
    Context context;
    public RecyclerAdapter(ArrayList<RecyclerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.levels, parent, false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        //we sets the data here
        RecyclerModel model = list.get(position);

        holder.text.setText(String.valueOf(model.getLevel()));
        holder.s1.setImageResource(model.getS1());
        holder.s2.setImageResource(model.getS2());
        holder.s3.setImageResource(model.getS3());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,GameActivity.class);
            intent.putExtra("level",model.getLevel());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder{
        ImageView s1,s2,s3;
        TextView text;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.leveltext);
            s1 =itemView.findViewById(R.id.star1);
            s2 =itemView.findViewById(R.id.star2);
            s3 =itemView.findViewById(R.id.star3);
        }
    }
}
