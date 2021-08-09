package com.example.ghx.freefood.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ghx on 2021/6/30.
 * FlexboxLayout的工具类
 */

public class FlexUtils {

    public static int pixelToDp(Context context, int pixel) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return pixel < 0 ? pixel : Math.round(pixel / displayMetrics.density);
    }

    public static int dpToPixel(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return dp < 0 ? dp : Math.round(dp * displayMetrics.density);
    }

}
