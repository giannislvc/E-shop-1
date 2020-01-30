package com.nativeboys.eshop.customViews;

import android.content.res.Resources;

public class ScreenManager {

    public static int getWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
