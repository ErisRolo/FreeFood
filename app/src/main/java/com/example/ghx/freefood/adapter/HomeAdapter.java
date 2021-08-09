package com.example.ghx.freefood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.ui.activity.GetActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ghx on 2021/6/19.
 * 首页Food的Adapter
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private static final String FOOD_ID = "food_id";

    private Context mContext;
    private List<Food> mFoodList;


    static class ViewHolder extends RecyclerView.ViewHolder {

        View homeView;
        LinearLayout ll_item;
        ImageView iv_food_photo;
        TextView tv_food_name;
        TextView tv_food_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            homeView = itemView;
            ll_item = itemView.findViewById(R.id.ll_item);
            iv_food_photo = itemView.findViewById(R.id.iv_food_photo);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_desc = itemView.findViewById(R.id.tv_food_desc);

        }
    }

    public HomeAdapter(Context context, List<Food> foodList) {
        mContext = context;
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.homeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Food food = mFoodList.get(position);
                String food_id = food.getObjectId();
                Intent intent = new Intent(mContext, GetActivity.class);
                intent.putExtra(FOOD_ID, food_id);
                mContext.startActivity(intent);
            }
        });
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
