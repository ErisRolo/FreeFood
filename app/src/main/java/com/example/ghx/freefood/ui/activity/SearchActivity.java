package com.example.ghx.freefood.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghx.freefood.R;
import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.bean.SearchFood;
import com.example.ghx.freefood.utils.FlexUtils;
import com.google.android.flexbox.FlexboxLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

/**
 * Created by ghx on 2021/6/17.
 * 搜索
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final String TAG = "SearchActivity";

    private static final String FOOD_NAME = "food_name";

    private TextView tv_back;
    private ImageView iv_search;
    private EditText et_search;
    private FlexboxLayout mFlexboxLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {

        tv_back = findViewById(R.id.tv_back);
        iv_search = findViewById(R.id.iv_search);
        et_search = findViewById(R.id.et_search);
        mFlexboxLayout = findViewById(R.id.mFlexboxLayout);
        String[] tags = {"hamburger","chips","cola"};
        for (int i = 0; i < tags.length; i++) {
            SearchFood model = new SearchFood();
            model.setId(i);
            model.setName(tags[i]);
            mFlexboxLayout.addView(createNewFlexItemTextView(model));
        }

        tv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        et_search.setOnEditorActionListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                if (isSoftShowing()) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                } else {
                    finish();
                }
                break;
            case R.id.iv_search:
                Intent intent = new Intent(this, SearchNameActivity.class);
                intent.putExtra(FOOD_NAME, et_search.getText().toString().trim());
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            ((InputMethodManager) et_search.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(SearchActivity.this
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            Intent intent = new Intent(this, SearchNameActivity.class);
            intent.putExtra(FOOD_NAME, et_search.getText().toString().trim());
            startActivity(intent);
            return true;
        }
        return false;
    }


    //判断键盘是否弹出
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度  
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom  
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度  
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度  
        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    private TextView createNewFlexItemTextView(SearchFood food) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(food.getName());
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colorLogo));
        textView.setBackgroundResource(R.drawable.search_bg);
        textView.setTag(food.getId());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchNameActivity.class);
                intent.putExtra(FOOD_NAME, food.getName());
                startActivity(intent);
            }
        });
        int padding = FlexUtils.dpToPixel(this, 4);
        int paddingLeftAndRight = FlexUtils.dpToPixel(this, 8);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = FlexUtils.dpToPixel(this, 6);
        int marginTop = FlexUtils.dpToPixel(this, 16);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

}
