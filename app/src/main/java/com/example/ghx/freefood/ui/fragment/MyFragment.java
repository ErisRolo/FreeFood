package com.example.ghx.freefood.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseFragment;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.ui.activity.GetFoodActivity;
import com.example.ghx.freefood.ui.activity.MyUserActivity;
import com.example.ghx.freefood.ui.activity.PublishFoodActivity;
import com.example.ghx.freefood.ui.activity.ShareFoodActivity;
import com.example.ghx.freefood.ui.activity.WasteFoodActivity;
import com.example.ghx.freefood.utils.Config;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/8.
 * 个人中心
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_profile;//编辑个人资料
    private LinearLayout ll_positive;//正积分
    private LinearLayout ll_negative;//负积分
    private LinearLayout ll_publish_food;//发布的食物
    private LinearLayout ll_share_food;//分享的食物
    private LinearLayout ll_waste_food;//浪费的食物
    private LinearLayout ll_get_food;//获得的食物

    private CircleImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_desc;

    private TextView tv_positive;//正积分
    private TextView tv_negative;//负积分

    private TextView tv_publish_number;//发布食物数
    private TextView tv_share_number;//分享食物数
    private TextView tv_waste_number;//浪费食物数
    private TextView tv_get_number;//获得食物数

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        findView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshView();
    }

    private void findView(View view) {

        ll_profile = view.findViewById(R.id.ll_profile);
        ll_positive = view.findViewById(R.id.ll_positive);
        ll_negative = view.findViewById(R.id.ll_negative);
        ll_publish_food = view.findViewById(R.id.ll_publish_food);
        ll_share_food = view.findViewById(R.id.ll_share_food);
        ll_waste_food = view.findViewById(R.id.ll_waste_food);
        ll_get_food = view.findViewById(R.id.ll_get_food);

        ll_profile.setOnClickListener(this);
        ll_publish_food.setOnClickListener(this);
        ll_share_food.setOnClickListener(this);
        ll_waste_food.setOnClickListener(this);
        ll_get_food.setOnClickListener(this);

        iv_avatar = view.findViewById(R.id.iv_avatar);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_desc = view.findViewById(R.id.tv_desc);

        tv_positive = view.findViewById(R.id.tv_positive);
        tv_negative = view.findViewById(R.id.tv_negative);
        tv_positive.setText(String.valueOf(mCurrentUser.getScore1()));
        tv_negative.setText(String.valueOf(mCurrentUser.getScore2()));

        tv_publish_number = view.findViewById(R.id.tv_publish_number);
        tv_share_number = view.findViewById(R.id.tv_share_number);
        tv_waste_number = view.findViewById(R.id.tv_waste_number);
        tv_get_number = view.findViewById(R.id.tv_get_number);

        refreshView();

    }

    private void refreshView() {

        if (mCurrentUser != null) {

            String url = mCurrentUser.getAvatar();
            if (url != null) {
                Glide.with(this).load(url).into(iv_avatar);
            }
            tv_nickname.setText(mCurrentUser.getNickname());
            tv_desc.setText(mCurrentUser.getDesc());
            tv_positive.setText(String.valueOf(mCurrentUser.getScore1()));
            tv_negative.setText(String.valueOf(mCurrentUser.getScore2()));

        }

        LCQuery<Food> queryPublish = LCObject.getQuery(Food.class);
        queryPublish.whereEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
        queryPublish.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foods) {
                if (foods.size() != 0)
                    tv_publish_number.setText(String.valueOf(foods.size()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        LCQuery<Food> queryShare = LCObject.getQuery(Food.class);
        queryShare.whereEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
        queryShare.whereEqualTo(Config.STATE, 2);
        queryShare.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foods) {
                if (foods.size() != 0)
                    tv_share_number.setText(String.valueOf(foods.size()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        LCQuery<Food> queryWaste = LCObject.getQuery(Food.class);
        queryWaste.whereEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
        queryWaste.whereEqualTo(Config.STATE, 3);
        queryWaste.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foods) {
                if (foods.size() != 0)
                    tv_waste_number.setText(String.valueOf(foods.size()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        LCQuery<Food> queryGet = LCObject.getQuery(Food.class);
        queryGet.whereEqualTo(Config.RECEIVER, mCurrentUser.getObjectId());
        queryGet.whereEqualTo(Config.STATE, 2);
        queryGet.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foods) {
                if (foods.size() != 0)
                    tv_get_number.setText(String.valueOf(foods.size()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_profile:
                startActivity(new Intent(getActivity(), MyUserActivity.class));
                break;
            case R.id.ll_publish_food:
                startActivity(new Intent(getActivity(), PublishFoodActivity.class));
                break;
            case R.id.ll_share_food:
                startActivity(new Intent(getActivity(), ShareFoodActivity.class));
                break;
            case R.id.ll_waste_food:
                startActivity(new Intent(getActivity(), WasteFoodActivity.class));
                break;
            case R.id.ll_get_food:
                startActivity(new Intent(getActivity(), GetFoodActivity.class));
                break;
        }

    }
}
