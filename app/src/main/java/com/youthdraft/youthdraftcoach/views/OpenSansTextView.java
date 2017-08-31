package com.youthdraft.youthdraftcoach.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.youthdraft.youthdraftcoach.R;

/**
 * Created by jjupin on 11/15/16.
 */

public class OpenSansTextView extends TextView {
    public enum Font {
        OpenSansRegular,
        OpenSansSemiBold,
        OpenSansBold;
    }

    private static Typeface openSansRegular;
    private static Typeface openSansSemibold;
    private static Typeface openSansBold;

    public OpenSansTextView(Context context) {
        super(context);
        init(context);
    }

    public OpenSansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        readAttributes(context, attrs);
    }

    public OpenSansTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        readAttributes(context, attrs);
    }

    private void init(Context context) {
        if (openSansRegular == null) {
            openSansRegular = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        }

        setTypeface(openSansRegular);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OpenSansTextView);

            try {
                int fontValue = a.getInt(R.styleable.OpenSansTextView_typeface, 0);
                switch (fontValue) {
                    case 0:
                        setFont(Font.OpenSansRegular);
                        break;
                    case 1:
                        setFont(Font.OpenSansSemiBold);
                        break;
                    case 2:
                        setFont(Font.OpenSansBold);
                        break;
                }
            } finally {
                a.recycle();
            }
        }
    }

    public void setFont(Font font) {
        switch (font) {
            case OpenSansRegular:
                if (openSansRegular == null) {
                    openSansRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
                }
                setTypeface(openSansRegular);
                break;
            case OpenSansSemiBold:
                if (openSansSemibold == null) {
                    openSansSemibold = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Semibold.ttf");
                }
                setTypeface(openSansSemibold);
                break;
            case OpenSansBold:
                if (openSansBold == null) {
                    openSansBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Bold.ttf");
                }
                setTypeface(openSansBold);
                break;
        }
    }
}
