package com.youthdraft.youthdraftcoach.utility;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.youthdraft.youthdraftcoach.R;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;

/**
 * Created by jjupin on 12/29/16.
 */

public class ScreenUtils {

    private static int sizeLarge = SCREENLAYOUT_SIZE_LARGE; // For 7" tablet
    private static int sizeXLarge = SCREENLAYOUT_SIZE_XLARGE; // For 10" tablet

    public static boolean is7InchTablet(Context context) {
        return context.getResources().getConfiguration()
                .isLayoutSizeAtLeast(sizeLarge);
    }

    public static boolean is10InchTablet(Context context) {
        return context.getResources().getConfiguration()
                .isLayoutSizeAtLeast(sizeXLarge);
    }

    public static boolean isTablet(Context context) {
        return is7InchTablet(context) || is10InchTablet(context);
    }

    //
    // update the status bar color when you need to...
    //

    public static void setTaskBarColored(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window w = context.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int statusBarHeight = getStatusBarHeight(context);

            View view = new View(context);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getLayoutParams().height = statusBarHeight;
            ((ViewGroup) w.getDecorView()).addView(view);
            view.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }
    }

    public static int getActionBarHeight(Activity context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getStatusBarHeight(Activity context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
