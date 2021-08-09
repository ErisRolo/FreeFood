package com.example.ghx.freefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.Food;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ghx on 2021/6/25.
 * 浪费列表Food的Adapter
 */

public class WasteAdapter extends RecyclerView.Adapter<WasteAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> mFoodList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View wasteView;
        ImageView iv_food_photo;
        TextView tv_food_name;
        TextView tv_food_desc;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wasteView = itemView;
            iv_food_photo = itemView.findViewById(R.id.iv_food_photo);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_desc = itemView.findViewById(R.id.tv_food_desc);
        }
    }

    public WasteAdapter(Context context, List<Food> foodList) {
        mContext = context;
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.waste_item, parent, false);
        WasteAdapter.ViewHolder holder = new WasteAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = mFoodList.get(position);
        String url = food.getFoodphoto();
        if (url != null) {
            Glide.with(mContext).load(url).into(holder.iv_food_photo);
        }
        holder.tv_food_name.setText(food.getFoodname());
        holder.tv_food_desc.setText(food.getFooddesc());
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
