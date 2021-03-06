package com.example.ghx.freefood.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.adapter.SearchResultAdapter;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.widget.SegmentedGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by ghx on 2021/6/29.
 * ???????????????
 */

public class LocationActivity extends BaseActivity implements View.OnClickListener,
        PoiSearch.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener, LocationSource,
        AMapLocationListener {


    private static final int REQUEST_LOCATION = 730;

    private Toolbar mToolbar;
    private ListView listView;
    private SegmentedGroup mSegmentedGroup;
    private AutoCompleteTextView searchText;
    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private String[] items = {"?????????", "??????", "??????", "??????"};
    private Marker locationMarker;
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private int currentPage = 0;// ??????????????????0????????????
    private PoiSearch.Query query;// Poi???????????????
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi??????
    private String searchType = items[0];
    private String searchKey = "";
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData = new ArrayList<>();
    private SearchResultAdapter searchResultAdapter;
    private boolean isItemClickAction;
    private List<Tip> autoTips;
    private boolean isfirstinput = true;
    private PoiItem firstItem;
    private ImageView finishBtn;
    private boolean isInputKeySearch;
    private String inputSearchKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initMap();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
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


        listView = findViewById(R.id.listview);
        searchResultAdapter = new SearchResultAdapter(LocationActivity.this);
        listView.setAdapter(searchResultAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        finishBtn = findViewById(R.id.iv_finish);
        finishBtn.setOnClickListener(this);

        mSegmentedGroup = findViewById(R.id.segmented_group);
        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                searchType = items[0];
                switch (checkedId) {
                    case R.id.radio0:
                        searchType = items[0];
                        break;
                    case R.id.radio1:
                        searchType = items[1];
                        break;
                    case R.id.radio2:
                        searchType = items[2];
                        break;
                    case R.id.radio3:
                        searchType = items[3];
                        break;
                }
                geoAddress();
            }
        });

        searchText = findViewById(R.id.keyWord);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    InputtipsQuery inputquery = new InputtipsQuery(newText, "");
                    Inputtips inputTips = new Inputtips(LocationActivity.this, inputquery);
                    inputquery.setCityLimit(true);
                    inputTips.setInputtipsListener(inputtipsListener);
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MY", "setOnItemClickListener");
                if (autoTips != null && autoTips.size() > position) {
                    Tip tip = autoTips.get(position);
                    searchPoi(tip);
                }
            }
        });

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);

        hideSoftKey(searchText);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isItemClickAction && !isInputKeySearch) {
                    geoAddress();
                    startJumpAnimation();
                }
                searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                isInputKeySearch = false;
                isItemClickAction = false;
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter(null);
            }
        });
    }

    /**
     * ????????????amap?????????
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// ??????????????????
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// ????????????????????????????????????
        aMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * ????????????marker ??????
     */
    public void startJumpAnimation() {

        if (locationMarker != null) {
            //????????????????????????????????????????????????
            final LatLng latLng = locationMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //??????TranslateAnimation,????????????????????????????????????
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // ?????????????????????interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //??????????????????????????????
            animation.setDuration(600);
            //????????????
            locationMarker.setAnimation(animation);
            //????????????
            locationMarker.startAnimation();

        } else {
            Log.e("ama", "screenMarker is null");
        }
    }

    //dip???px??????
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * ?????????????????????
     */
    public void geoAddress() {
//        Log.i("MY", "geoAddress"+ searchLatlonPoint.toString());
        showDialog();
        searchText.setText("");
        if (searchLatlonPoint != null) {
            RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 200, GeocodeSearch.AMAP);// ???????????????????????????Latlng????????????????????????????????????????????????????????????????????????????????????GPS???????????????
            geocoderSearch.getFromLocationAsyn(query);
        }
    }

    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("????????????...");
        progDialog.show();
    }

    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //??????Marker????????????,?????????????????????
        locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        locationMarker.setZIndex(1);

    }

    private void searchPoi(Tip result) {
        isInputKeySearch = true;
        inputSearchKey = result.getName();//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        searchLatlonPoint = result.getPoint();
        firstItem = new PoiItem("tip", searchLatlonPoint, inputSearchKey, result.getAddress());
        firstItem.setCityName(result.getDistrict());
        firstItem.setAdName("");
        resultData.clear();

        searchResultAdapter.setSelectedPosition(0);

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchLatlonPoint.getLatitude(), searchLatlonPoint.getLongitude()), 16f));

        hideSoftKey(searchText);
        doSearchQuery();
    }

    private void hideSoftKey(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * ????????????poi??????
     */
    protected void doSearchQuery() {
//        Log.i("MY", "doSearchQuery");
        currentPage = 0;
        query = new PoiSearch.Query(searchKey, searchType, "");// ????????????????????????????????????????????????????????????poi????????????????????????????????????poi??????????????????????????????????????????
        query.setCityLimit(true);
        query.setPageSize(20);
        query.setPageNum(currentPage);

        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));//
            poiSearch.searchPOIAsyn();
        }
    }

    /**
     * ??????????????????item
     *
     * @param poiItems
     */
    private void updateListview(List<PoiItem> poiItems) {
        resultData.clear();
        searchResultAdapter.setSelectedPosition(0);
        resultData.add(firstItem);
        resultData.addAll(poiItems);

        searchResultAdapter.setData(resultData);
        searchResultAdapter.notifyDataSetChanged();
    }

    /**
     * ???????????????????????????
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
                LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                searchLatlonPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));
                isInputKeySearch = false;
                searchText.setText("");
            } else {
                String errText = "????????????," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * ????????????
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //??????????????????
            mlocationClient.setLocationListener(this);
            //??????????????????????????????
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //??????????????????
            mlocationClient.setLocationOption(mLocationOption);
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????2000ms?????????????????????????????????stopLocation()???????????????????????????
            // ???????????????????????????????????????????????????onDestroy()??????
            // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
            mlocationClient.startLocation();
        }
    }

    /**
     * ????????????
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String address = result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
                firstItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                doSearchQuery();
            }
        } else {
            Toast.makeText(LocationActivity.this, "error code is " + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        updateListview(poiItems);
                    } else {
                        Toast.makeText(LocationActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(LocationActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                Intent intent = new Intent();
                intent.putExtra("locationName", searchResultAdapter.getSelectedPoiItem().getTitle());
                Log.i("tag", searchResultAdapter.getSelectedPoiItem().getTitle() + searchResultAdapter.getSelectedPoiItem().getLatLonPoint());
                Bundle bundle = new Bundle();
                bundle.putParcelable("LatLonPoint", searchResultAdapter.getSelectedPoiItem().getLatLonPoint());
                intent.putExtras(bundle);
                setResult(REQUEST_LOCATION, intent);
                finish();
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != searchResultAdapter.getSelectedPosition()) {
                PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
                LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());

                isItemClickAction = true;

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                searchResultAdapter.setSelectedPosition(position);
                searchResultAdapter.notifyDataSetChanged();
            }
        }
    };

    Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> list, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// ????????????
                autoTips = list;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    listString.add(list.get(i).getName());
                }
                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.route_inputs, listString);
                searchText.setAdapter(aAdapter);
                aAdapter.notifyDataSetChanged();
                if (isfirstinput) {
                    isfirstinput = false;
                    searchText.showDropDown();
                }
            } else {
                Toast.makeText(LocationActivity.this, "erroCode " + rCode, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
