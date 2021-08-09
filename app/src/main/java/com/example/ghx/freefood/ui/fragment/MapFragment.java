package com.example.ghx.freefood.ui.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseFragment;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.ui.activity.GetActivity;
import com.example.ghx.freefood.utils.Config;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.types.LCGeoPoint;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/8.
 * 附近
 */

public class MapFragment extends BaseFragment implements AMap.OnMyLocationChangeListener,
        AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter, AMap.OnMapClickListener {

    private static final String FOOD_ID = "food_id";

    private LatLng latLng = new LatLng(30.262989572401978, 120.13264656882572); //地图中心点经纬度
    //private RelativeLayout rl_map;
    private TextureMapView mMapView;
    private AMap mAMap;

    private MyLocationStyle mMyLocationStyle;
    private Marker mMarker;
    private boolean infoWindowShown = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        //rl_map = view.findViewById(R.id.rl_map);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        AMapOptions options = new AMapOptions(); //设置地图参数
//        options.camera(new CameraPosition(latLng, 10f, 0, 0));
//        mMapView = new TextureMapView(getActivity(), options);
//        mMapView.onCreate(savedInstanceState); //必须重写！！！
//        rl_map.addView(mMapView);
//        if (mAMap == null) {
//            mAMap = mMapView.getMap();
//        }
//        mAMap.getUiSettings().setZoomControlsEnabled(true); //缩放按钮

        mMapView = getView().findViewById(R.id.mMapView);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(18.0F));
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(180.0F));
        mAMap.getUiSettings().setZoomControlsEnabled(true); //缩放按钮

        //初始化定位蓝点样式类
        mMyLocationStyle = new MyLocationStyle();
        mMyLocationStyle.interval(3000); //设置连续定位模式下的定位间隔
        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER); //连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动
        mAMap.setMyLocationStyle(mMyLocationStyle);
        mAMap.setMyLocationEnabled(true); //是否启动定位蓝点，默认为false

        mAMap.setOnMyLocationChangeListener(this);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setInfoWindowAdapter(this);
        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setOnMapClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMyLocationChange(Location location) {
//        String ll = String.valueOf(location.getLatitude());
//        String rr = String.valueOf(location.getLongitude());
//        Log.i("MapFragment", ll + rr);
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
            public void onNext(List<Food> foodList) {
                if (foodList != null) {
                    if (foodList.size() != 0) {
                        for (int i = 0; i < foodList.size(); i++) {
                            Food food = foodList.get(i);
                            //添加Marker
                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(food.getWhereCreated().getLatitude(), food.getWhereCreated().getLongitude());
                            markerOptions.position(latLng);
                            markerOptions.title(food.getFoodname());
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker)));
                            markerOptions.snippet(food.getObjectId()); //食物id
                            markerOptions.draggable(false);
                            markerOptions.setFlat(false);
                            mMarker = mAMap.addMarker(markerOptions);
                            //添加动画效果
                            Animation animation = new ScaleAnimation(0f, 1.0f, 0f, 1.0f);
                            animation.setDuration(5000);
                            animation.setInterpolator(new BounceInterpolator());
                            mMarker.setAnimation(animation);
                            mMarker.startAnimation();
                        }
                    }
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

    @Override
    public void onMapClick(LatLng latLng) {
        if (infoWindowShown) {
            mMarker.hideInfoWindow();
            infoWindowShown = false;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMarker = marker;
        mMarker.showInfoWindow();
        infoWindowShown = true;

        return true;//返回true说明marker被点击后不会成为中心点
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(getActivity()).inflate(R.layout.infowindow_item, null);
        TextView tv_food_name = infoWindow.findViewById(R.id.tv_food_name);
        tv_food_name.setText(marker.getTitle());
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String food_id = marker.getSnippet();
        Intent intent = new Intent(getContext(), GetActivity.class);
        intent.putExtra(FOOD_ID, food_id);
        startActivity(intent);
    }

}
