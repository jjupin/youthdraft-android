package com.youthdraft.youthdraftcoach.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jjupin on 11/15/16.
 */

public class NonSwipeableViewPager extends ViewPager {
    private boolean isSwipeable;

    public NonSwipeableViewPager(Context context) {
        super(context);
        isSwipeable = true;
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isSwipeable = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSwipeable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            // Never allow swiping to switch between pages
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSwipeable) {
            return super.onTouchEvent(ev);
        } else {
            // Never allow swiping to switch between pages
            return false;
        }
    }

    public void setIsSwipeable(boolean swipeable) {
        isSwipeable = swipeable;
    }
}