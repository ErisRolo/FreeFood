package com.example.ghx.freefood;

import android.app.Application;
import android.content.Context;

import com.example.ghx.freefood.bean.CustomUserProvider;
import com.example.ghx.freefood.bean.Food;
import com.example.ghx.freefood.bean.User;
import com.example.ghx.freefood.utils.API;

import androidx.multidex.MultiDex;
import cn.leancloud.LCObject;
import cn.leancloud.LCUser;
import cn.leancloud.LeanCloud;
import cn.leancloud.chatkit.LCChatKit;


/**
 * Created by ghx on 2021/6/7.
 * Application
 * 获取全局Context
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        //文件分包
        MultiDex.install(this);


        //子类化User
        LCUser.registerSubclass(User.class);
        LCUser.alwaysUseSubUserClass(User.class);

        //子类化Food
        LCObject.registerSubclass(Food.class);

        //初始化LeanCloud
        LeanCloud.initialize(this, API.AVOS_APP_ID, API.AVOS_APP_KEY);

        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), API.AVOS_APP_ID, API.AVOS_APP_KEY);

    }

    public static Context getContext() {
        return mContext;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
