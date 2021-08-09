package com.example.ghx.freefood.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.utils.FileUtils;
import com.example.ghx.freefood.utils.ShowToast;
import com.google.android.material.snackbar.Snackbar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.types.LCGeoPoint;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/19.
 * 发布食物
 */

public class PublishActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_LOCATION = 730;

    private Toolbar mToolbar;

    private ImageView iv_finish;
    private ImageView iv_add;
    private EditText et_foodname;
    private EditText et_fooddesc;

    private EditText et_weight;
    private EditText et_footprint1;
    private EditText et_distance;
    private EditText et_footprint2;
    private EditText et_footprint;
    private EditText et_place;
    private Spinner mSpinner;
    private ArrayAdapter<String> mAdapter;
    private AlertDialog dialog_footprint;

    private String locationName;
    private LatLonPoint locationCoordinate;

    Food food;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publish);
        initView();
        initDialog();
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

        iv_finish = findViewById(R.id.iv_finish);
        iv_add = findViewById(R.id.iv_add);
        et_foodname = findViewById(R.id.et_foodname);
        et_fooddesc = findViewById(R.id.et_fooddesc);

        et_footprint = findViewById(R.id.et_footprint);
        et_place = findViewById(R.id.et_place);

        iv_finish.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        et_footprint.setOnClickListener(this);
        et_place.setOnClickListener(this);

        food = new Food();

    }

    private void initDialog() {
        View dialogFootprint = LayoutInflater.from(this).inflate(R.layout.dialog_footprint, null);
        et_weight = dialogFootprint.findViewById(R.id.et_weight);
        et_footprint1 = dialogFootprint.findViewById(R.id.et_footprint1);
        et_distance = dialogFootprint.findViewById(R.id.et_distance);
        et_footprint2 = dialogFootprint.findViewById(R.id.et_footprint2);
        mSpinner = dialogFootprint.findViewById(R.id.mSpinner);
        et_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et_distance.getText().toString())&&!TextUtils.isEmpty(et_weight.getText().toString())) {
                    double distance = Double.parseDouble(et_distance.getText().toString().trim());
                    double foodweight = Double.parseDouble(et_weight.getText().toString().trim());
                    //按载重3000kg货车的碳排放271.6g/km计算
                    double footprint2 = distance * 271.6 * (foodweight / 3000000);
                    DecimalFormat df = new DecimalFormat("0.00");
                    et_footprint2.setText(String.valueOf(df.format(footprint2)));
                }
            }
        });
        String[] mList = getResources().getStringArray(R.array.food_type);
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_display_style, R.id.tv_spinner, mList);
        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(this);
        dialog_footprint = new AlertDialog.Builder(this)
                .setView(dialogFootprint)
                .setTitle(" ")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double footprint1 = Double.parseDouble(et_footprint1.getText().toString().trim());
                        double footprint2 = Double.parseDouble(et_footprint2.getText().toString().trim());
                        double footprint = footprint1 + footprint2;
                        et_footprint.setText(String.valueOf(footprint));
                    }
                }).setNegativeButton("取消", null)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                String foodname = et_foodname.getText().toString().trim();
                String fooddesc = et_fooddesc.getText().toString().trim();
                String foodtype = mSpinner.getSelectedItem().toString();
                double foodweight = 0, footprint1 = 0, distance = 0, footprint2 = 0, footprint = 0;
                if (!et_weight.getText().toString().trim().equals("") && !et_footprint1.getText().toString().trim().equals("")
                        && !et_distance.getText().toString().trim().equals("") && !et_footprint2.getText().toString().trim().equals("")
                        && !et_footprint.getText().toString().trim().equals("")) {
                    foodweight = Double.parseDouble(et_weight.getText().toString().trim());
                    footprint1 = Double.parseDouble(et_footprint1.getText().toString().trim());
                    distance = Double.parseDouble(et_distance.getText().toString().trim());
                    footprint2 = Double.parseDouble(et_footprint2.getText().toString().trim());
                    footprint = Double.parseDouble(et_footprint.getText().toString().trim());
                }
                if (!TextUtils.isEmpty(foodname) && !TextUtils.isEmpty(fooddesc)
                        && !TextUtils.isEmpty(et_weight.getText().toString()) && !TextUtils.isEmpty(et_footprint1.getText().toString())
                        && !TextUtils.isEmpty(et_footprint2.getText().toString()) && !TextUtils.isEmpty(et_footprint.getText().toString())) {
                    food.setPublisher(mCurrentUser.getObjectId());
                    food.setFoodname(foodname);
                    food.setFooddesc(fooddesc);
                    food.setFoodweight(foodweight);
                    food.setFoodtype(foodtype);
                    food.setFootprint1(footprint1);
                    food.setDistance(distance);
                    food.setFootprint2(footprint2);
                    food.setFootprint(footprint);
                    food.setState(0); //新建默认为未交易状态
                    food.setPlace(locationName);
                    LCGeoPoint point = new LCGeoPoint(locationCoordinate.getLatitude(), locationCoordinate.getLongitude());
                    food.setWhereCreated(point);
                    food.saveInBackground().subscribe(new Observer<LCObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LCObject lcObject) {
                            ShowToast.showShortToast(PublishActivity.this, "食物发布成功！");
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ShowToast.showShortToast(PublishActivity.this, e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                } else {
                    ShowToast.showShortToast(this, "输入框不能为空！");
                }
                break;
            case R.id.iv_add:
                Matisse.from(this)
                        .choose(MimeType.ofAll())
                        .theme(R.style.Matisse_Zhihu)
                        .imageEngine(new GlideEngine())
                        .countable(false)
                        .maxSelectable(1)
                        .forResult(0);
                break;
            case R.id.et_footprint:
                dialog_footprint.show();
                dialog_footprint.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(20);
                dialog_footprint.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(20);
                break;
            case R.id.et_place:
                Intent intent = new Intent(this, LocationActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = Matisse.obtainResult(data).get(0);
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .into(iv_add);
                String path = FileUtils.getFilePahtFromUri(this, uri);
                if (path != null) {
                    try {
                        LCFile file = LCFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                        file.saveInBackground().subscribe(new Observer<LCFile>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LCFile lcFile) {
                                food.setFoodphoto(lcFile.getUrl());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Snackbar.make(iv_add, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        if (resultCode == REQUEST_LOCATION) {
            locationName = data.getStringExtra("locationName");
            locationCoordinate = data.getParcelableExtra("LatLonPoint");
            et_place.setText(locationName);
            et_place.setTextColor(Color.BLACK);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] mList = getResources().getStringArray(R.array.food_type);
        String type = mList[position];
        double foodweight = 0, footprint = 0, footprint1 = 0;
        if (!et_weight.getText().toString().trim().equals("")) {
            foodweight = Double.parseDouble(et_weight.getText().toString().trim());
        }
        switch (type) {
            case "Onion":
                footprint = 1.05;
                break;
            default:
                break;
        }
        Log.i("PublishActivity", String.valueOf(foodweight));
        footprint1 = foodweight * footprint;
        DecimalFormat df = new DecimalFormat("0.00");
        et_footprint1.setText(String.valueOf(df.format(footprint1)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
