package com.example.ghx.freefood.bean;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ghx on 2021/6/28.
 * 实现即时通讯的用户接口
 */

public class CustomUserProvider implements LCChatProfileProvider {

    private static final String TAG = "CustomUserProvider";

    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    private CustomUserProvider() {
    }

    private List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();


    @Override
    public void fetchProfiles(List<String> userIdList, LCChatProfilesCallBack callBack) {
        LCQuery<User> query = LCObject.getQuery(User.class);
        query.findInBackground().subscribe(new Observer<List<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<User> list) {
                List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
                if (list != null && !list.isEmpty()) {
                    for (LCUser lcUser:list){
                        User user = LCUser.cast(lcUser, User.class);
                        for (String userId : userIdList) {
                            if (userId.equals(user.getObjectId())) {
                                Log.i(TAG, "userId:" + user.getObjectId() +
                                        "nickname:" + user.getNickname() +
                                        "avatar:" + user.getAvatar());
                                LCChatKitUser kitUser = new LCChatKitUser(
                                        user.getObjectId(),
                                        user.getNickname(),
                                        user.getAvatar());
                                userList.add(kitUser);
                                partUsers.add(kitUser);
                            }
                        }
                    }
                }
                callBack.done(userList, null);
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
    public List<LCChatKitUser> getAllUsers() {
        return partUsers;
    }
}
