package com.supertridents.learn.digital.marketing.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {
    ArrayList<RecyclerModel> list;
    Context context;
    int level;
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


        SharedPreferences preferences = context.getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE);
        level = preferences.getInt(MainActivity.CURRENT,1);
        if(model.getLevel()>level){
            holder.lock.setVisibility(View.VISIBLE);
            holder.text.setVisibility(View.INVISIBLE);
        }
        else{
            holder.lock.setVisibility(View.INVISIBLE);
            holder.text.setVisibility(View.VISIBLE);
        }
        holder.s1.setVisibility(View.INVISIBLE);
        //.s2.setVisibility(View.INVISIBLE);
        holder.s3.setVisibility(View.INVISIBLE);
        //Toast.makeText(context,"level"+level, Toast.LENGTH_SHORT).show();
        holder.itemView.setOnClickListener(v -> {
            if(model.getLevel() <= level) {
                //Toast.makeText(context,"level"+level, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("level", model.getLevel());
                context.startActivity(intent);
                if(level<=model.getLevel()){level++;}
                SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.LEVEL,MODE_PRIVATE).edit();
                editor.putInt(MainActivity.CURRENT,level);
                editor.apply();
                editor.commit();
            }
            else {
                Toast.makeText(context, "Level Locked", Toast.LENGTH_SHORT).show();
            }
        });
        if(model.getLevel()<level){
            holder.lock.setVisibility(View.INVISIBLE);
            holder.text.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder{
        ImageView s1,s2,s3,lock;
        TextView text;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.leveltext);
            s1 =itemView.findViewById(R.id.star1);
            s2 =itemView.findViewById(R.id.star2);
            s3 =itemView.findViewById(R.id.star3);
            lock =itemView.findViewById(R.id.lockimage);
        }
    }

}
