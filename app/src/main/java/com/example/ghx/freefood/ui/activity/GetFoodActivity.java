package com.example.ghx.freefood.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.GetAdapter;
import com.example.ghx.freefood.adapter.ShareAdapter;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.utils.Config;

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
 * 获得的食物列表
 */

public class GetFoodActivity extends BaseActivity {

    private Toolbar mToolbar;

    private RecyclerView rv_get_food;
    private LinearLayoutManager mLayoutManager;
    private GetAdapter mAdapter;
    private List<Food> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_food);
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

        rv_get_food = findViewById(R.id.rv_get_food);
        mLayoutManager = new LinearLayoutManager(this);
        rv_get_food.setLayoutManager(mLayoutManager);
        setGetFoodAdapter();
    }

    private void setGetFoodAdapter() {
        LCQuery<Food> query = LCObject.getQuery(Food.class);
        query.whereEqualTo(Config.RECEIVER, mCurrentUser.getObjectId());
        query.whereEqualTo(Config.STATE, 2);
        query.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foodList) {
                if (foodList.size() != 0) {
                    mList.clear();
                    mList.addAll(foodList);
                    mAdapter = new GetAdapter(GetFoodActivity.this, mList);
                    rv_get_food.setAdapter(mAdapter);
                } else {
                    rv_get_food.removeAllViews();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
