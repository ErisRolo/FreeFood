package com.example.ghx.freefood.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.PublishAdapter;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.utils.Config;
import com.example.ghx.freefood.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/21.
 * 发布的食物列表
 */

public class PublishFoodActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView rv_publish_food;
    private LinearLayoutManager mLayoutManager;
    private PublishAdapter mAdapter;
    private List<Food> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_publish_food);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_publish_food = findViewById(R.id.rv_publish_food);
        mLayoutManager = new LinearLayoutManager(this);
        rv_publish_food.setLayoutManager(mLayoutManager);
        setPublishFoodAdapter();

    }

    private void setPublishFoodAdapter() {
        LCQuery<Food> query = LCObject.getQuery(Food.class);
        query.whereEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
        query.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foods) {
                if (foods.size() != 0) {
                    mList.clear();
                    mList.addAll(foods);
                    mAdapter = new PublishAdapter(PublishFoodActivity.this, mList);
                    rv_publish_food.setAdapter(mAdapter);
                } else {
                    rv_publish_food.removeAllViews();
                }
            }

            @Override
            public void onError(Throwable e) {
                ShowToast.showShortToast(PublishFoodActivity.this, e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
