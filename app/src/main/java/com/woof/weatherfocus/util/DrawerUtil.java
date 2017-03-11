package com.woof.weatherfocus.util;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by Woof on 3/11/2017.
 */

public class DrawerUtil {

    private static final float OFFSET_THRESHOLD = 0.03f;

    public static void close(final DrawerLayout drawerLayout) {
        drawerLayout.closeDrawer(GravityCompat.START);
        DrawerLayout.DrawerListener listener = new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < OFFSET_THRESHOLD) {

                }
            }
        };
        drawerLayout.addDrawerListener(listener);
    }

}
