package com.example.ghx.freefood;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ghx.freefood.base.BaseActivity;
import com.example.ghx.freefood.ui.fragment.HomeFragment;
import com.example.ghx.freefood.ui.fragment.MapFragment;
import com.example.ghx.freefood.ui.fragment.MsgFragment;
import com.example.ghx.freefood.ui.fragment.MyFragment;
import com.example.ghx.freefood.utils.FragmentUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.im.v2.LCIMClient;
import cn.leancloud.im.v2.LCIMException;
import cn.leancloud.im.v2.callback.LCIMClientCallback;
import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by ghx on 2021/6/7.
 * 主界面
 */

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottom_layout;
    private FragmentManager fm;

    private HomeFragment mHomeFragment;
    private MapFragment mMapFragment;
    private MsgFragment mMsgFragment;
    private MyFragment mMyFragment;

    public static MainActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        //动态权限申请
        PermissionGen.with(this)
                .addRequestCode(10000)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CHANGE_CONFIGURATION)
                .request();

        //聊天用户登录
        LCChatKit.getInstance().open(mCurrentUser.getObjectId(), new LCIMClientCallback() {
            @Override
            public void done(LCIMClient client, LCIMException e) {
                if (null == e) {
                    Log.i("MainActivity","聊天用户已登入。");
                }
            }
        });


        fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mHomeFragment = new HomeFragment();
            FragmentUtils.addFragment(fm, R.id.fragment_container, mHomeFragment);
        }
        initView();

    }

    private void initView() {
        bottom_layout = findViewById(R.id.bottom_layout);
        //BottomNavigationViewHelper.disableShiftMode(bottom_layout);
        bottom_layout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        mHomeFragment = new HomeFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mHomeFragment);
                        break;
                    case R.id.map:
                        mMapFragment = new MapFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mMapFragment);
                        break;
                    case R.id.msg:
                        mMsgFragment = new MsgFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mMsgFragment);
                        break;
                    case R.id.my:
                        mMyFragment = new MyFragment();
                        FragmentUtils.replaceFragment(fm, R.id.fragment_container, mMyFragment);
                        break;
                }
                return true;
            }
        });


    }
}
