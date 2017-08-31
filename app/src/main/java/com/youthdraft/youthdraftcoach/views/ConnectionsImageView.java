package com.youthdraft.youthdraftcoach.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.youthdraft.youthdraftcoach.R;

/**
 * Created by jjupin on 11/15/16.
 */

public class ConnectionsImageView extends RelativeLayout {
    private static final String TAG = "ConnectionsImageView";

    private View rootView;
    private ImageView avatarView;
    private OpenSansTextView initialsView;
    private boolean isInitialsView = false;

    private Bitmap defaultAvatar;
    private RelativeLayout.LayoutParams smallLP;
    private RelativeLayout.LayoutParams maxLP;

    private int transparent;
    private int residentBackground;
    private int sexOffenderBackground;
    private int white;

    public ConnectionsImageView(Context context) {
        super(context);
        init(context);
    }

    public ConnectionsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ConnectionsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.connection_image_view_layout, this);
        avatarView = (ImageView) rootView.findViewById(R.id.avatar);
        initialsView = (OpenSansTextView) rootView.findViewById(R.id.initials);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            defaultAvatar = ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.avatar)).getBitmap();
        }

        Resources r = context.getResources();
        int avatarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, r.getDisplayMetrics());
        int avatarWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 19, r.getDisplayMetrics());

        smallLP = new RelativeLayout.LayoutParams(avatarWidth, avatarHeight);
        smallLP.addRule(RelativeLayout.CENTER_IN_PARENT);

        maxLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        transparent = ContextCompat.getColor(context, android.R.color.transparent);
        residentBackground = ContextCompat.getColor(context, R.color.yellow);
        sexOffenderBackground = ContextCompat.getColor(context, R.color.dark_red);
        white = ContextCompat.getColor(context, R.color.white);
    }

    public void setAvatar() {
        // show profile avatar if user has one from connections or phone
        // show avatar is the contact is unknown
        // show initials

        Context context = getContext();

        avatarView.setLayoutParams(smallLP);
        avatarView.setVisibility(View.VISIBLE);
        initialsView.setText("");
        initialsView.setVisibility(View.VISIBLE);

        rootView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_edges_pale_gray));

        //rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.pale_gray));  // setting it to default color

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // bitmap
            avatarView.setImageBitmap(defaultAvatar);
        } else {
            // vector
            avatarView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.avatar));
        }

        /**
        if (contactObject != null) {

            if (contactObject.isTelemarketer()) {
                setTelemarketerAvatar(context);
                return;
            } else if (!TextUtils.isEmpty(contactObject.getContent()) && contactObject.getContent().equals("Telemarketer")) {
                setTelemarketerAvatar(context);
                return;
            }

            String mContactId = contactObject.getContactId();
            if (!TextUtils.isEmpty(mContactId)) {
                Uri avatarUri = ContactsUtils.getUserPhotoUri(mContactId);

                if (ContactsUtils.hasPhotoStream(avatarUri, context)) {
                    // load from uri
                    Picasso.with(context).load(avatarUri).into(avatarView);
                    avatarView.setLayoutParams(maxLP);
                    rootView.setBackgroundColor(transparent);
                } else {
                    avatarUri = ContactsUtils.getProfilePhotoUri(contactObject);
                    if (avatarUri != null) {
                        // load from uri
                        Picasso.with(context).load(avatarUri).into(avatarView);
                        avatarView.setLayoutParams(maxLP);
                        rootView.setBackgroundColor(transparent);
                    } else if (!TextUtils.isEmpty(contactObject.getContent()) && contactObject.getContent().equals("Telemarketer")) {
                        // telelmarketer
                        setTelemarketerAvatar(context);
                    } else {
                        // show initials if name exists, otherwise show default avatar
                        if (!TextUtils.isEmpty(contactObject.getContent()) &&
                                !Patterns.PHONE.matcher(contactObject.getContent()).matches()) {
                            avatarView.setVisibility(View.GONE);
                            initialsView.setText(StringUtils.getInitials(contactObject.getContent()));
                            isInitialsView = true;
                        }
                    }
                }
            }
        }
         **/
    }

    /*
        can't use picasso because it doesn't work with vectors
     */

    /**
    public void setTelemarketerAvatar(Context context) {
        avatarView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.telemarketer_no_bkgnd));
        avatarView.setVisibility(View.VISIBLE);
        avatarView.setLayoutParams(smallLP);
        rootView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_edges_with_red));
        initialsView.setText("");  // nothing to show...
        initialsView.setVisibility(View.GONE);
    }

    public void setIcon(Constants.MapType mapType) {
        switch (mapType) {
            case Friend:
                // use set avatar
                break;
            case Resident:
                rootView.setBackgroundColor(residentBackground);
                avatarView.setImageResource(R.drawable.home_white);
                break;
            case SexOffender:
                break;
        }
    }
     **/

    public ImageView getAvatarView() {
        return avatarView;
    }

    public OpenSansTextView getInitialsView() {
        return initialsView;
    }

    public boolean isInitialsView() {
        return this.isInitialsView;
    }

    public void setDefaultAvatarSize(int width, int height) {
        smallLP = new RelativeLayout.LayoutParams(width, height);
        smallLP.addRule(RelativeLayout.CENTER_IN_PARENT);
    }
}
