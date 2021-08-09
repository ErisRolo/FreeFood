package com.example.ghx.freefood.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.SearchNameAdapter;
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
 * Created by ghx on 2021/6/17.
 * 搜索结果
 */

public class SearchNameActivity extends BaseActivity {

    private static final String FOOD_NAME = "food_name";
    private String foodName;

    private Toolbar mToolbar;

    private RecyclerView rv_search;
    private LinearLayoutManager mLayoutManager;
    private SearchNameAdapter mAdapter;
    private List<Food> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodName = getIntent().getStringExtra(FOOD_NAME);

        setContentView(R.layout.acitivity_search_name);
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

        rv_search = findViewById(R.id.rv_search);
        mLayoutManager = new LinearLayoutManager(this);
        rv_search.setLayoutManager(mLayoutManager);
        setSearchNameAdapter();

    }

    private void setSearchNameAdapter() {
        LCQuery<Food> query = LCObject.getQuery(Food.class);
        Log.i("SearchNameActivity", foodName);
        query.whereEqualTo(Config.FOODNAME, foodName);
        query.whereEqualTo(Config.STATE, 0);
        query.whereNotEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
        query.findInBackground().subscribe(new Observer<List<Food>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Food> foodList) {
                if (foodList.size() != 0) {
                    mList.clear();
                    mList.addAll(foodList);
                    mAdapter = new SearchNameAdapter(SearchNameActivity.this, mList);
                    rv_search.setAdapter(mAdapter);
                } else {
                    rv_search.removeAllViews();
                }
            }

            @Override
            public void onError(Throwable e) {
                ShowToast.showShortToast(SearchNameActivity.this, e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
