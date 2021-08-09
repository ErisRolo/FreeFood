package com.example.ghx.freefood.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by ghx on 2021/6/7.
 * Fragment封装工具类
 */

public class FragmentUtils {

    public static void addFragment(FragmentManager mFragmentManager, int frameId,
                                   Fragment mFragment) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(frameId, mFragment);
        mFragmentTransaction.commit();
    }

    public static void replaceFragment(FragmentManager mFragmentManager, int frameId,
                                       Fragment mFragment) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(frameId, mFragment);
        mFragmentTransaction.commit();
    }
}
