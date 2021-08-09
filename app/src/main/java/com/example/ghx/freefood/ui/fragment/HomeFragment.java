package com.example.ghx.freefood.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.HomeAdapter;
import com.example.ghx.freefood.base.BaseFragment;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.ui.activity.PublishActivity;
import com.example.ghx.freefood.ui.activity.SearchActivity;
import com.example.ghx.freefood.utils.Config;
import com.example.ghx.freefood.utils.ShowToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.types.LCGeoPoint;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/8.
 * 首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private List<Integer> imageUrl;


    private LinearLayout ll_search;
    private NestedScrollView mScrollView;
    private Banner mBanner;
    private FloatingActionButton fab_add;
    private RecyclerView rv_food;
    private LinearLayoutManager mLayoutManager;
    private HomeAdapter mAdapter;
    private List<Food> mList = new ArrayList<>();

    private AMapLocationClient locationClient = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        findView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //setFoodAdapter();

        initLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    private void findView(View view) {
        ll_search = view.findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);

        mScrollView = view.findViewById(R.id.mScrollView);
        mBanner = view.findViewById(R.id.mBanner);
        initBanner();

        fab_add = view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        rv_food = view.findViewById(R.id.rv_food);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv_food.setLayoutManager(mLayoutManager);
        //setFoodAdapter();

        initLocation();
    }

    private void initBanner() {
        imageUrl = new ArrayList<>();
        imageUrl.add(R.drawable.sf1);
        imageUrl.add(R.drawable.sf2);
        imageUrl.add(R.drawable.sf3);
        mBanner.setAdapter(new BannerImageAdapter<Integer>(imageUrl) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        });
    }

//    private void setFoodAdapter() {
//        LCQuery<Food> query = LCObject.getQuery(Food.class);
//        query.whereNotEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
//        query.whereEqualTo(Config.STATE, 0);
//        query.findInBackground().subscribe(new Observer<List<Food>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(List<Food> foods) {
//                if (foods.size() != 0) {
//                    mList.clear();
//                    mList.addAll(foods);
//                    mAdapter = new HomeAdapter(getActivity(), mList);
//                    rv_food.setAdapter(mAdapter);
//                } else {
//                    rv_food.removeAllViews();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                ShowToast.showShortToast(getActivity(), e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//
//    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

        locationClient.startLocation();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.fab_add:
                startActivity(new Intent(getActivity(), PublishActivity.class));
                break;
        }
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //获取距离当前位置10km范围内的食物
                LCQuery<Food> queryFood = LCObject.getQuery(Food.class);
                LCGeoPoint point = new LCGeoPoint(location.getLatitude(), location.getLongitude());
                queryFood.whereNotEqualTo(Config.PUBLISHER, mCurrentUser.getObjectId());
                queryFood.whereEqualTo(Config.STATE, 0);
                queryFood.whereWithinKilometers(Config.WHERECREATED, point, 10);
                queryFood.findInBackground().subscribe(new Observer<List<Food>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Food> foods) {
                        if (foods.size() != 0) {
                            mList.clear();
                            mList.addAll(foods);
                            mAdapter = new HomeAdapter(getActivity(), mList);
                            rv_food.setAdapter(mAdapter);
                        } else {
                            rv_food.removeAllViews();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ShowToast.showShortToast(getActivity(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            } else {
                ShowToast.showShortToast(getActivity(), "定位失败");
            }
        }

    };


}
