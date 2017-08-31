package com.youthdraft.youthdraftcoach.utility;

import android.content.Context;

/**
 * Created by jjupin on 1/8/17.
 */

public class AdminUtils {

    private static final String[] allowedNames = {"jupin", "turner", "thompson"};

    public static boolean isAdmin(String userEmail, Context context) {
        String email = PreferencesUtils.getEmail(context);
        if (email != null && email.length() > 0) {
            for (int i=0; i<allowedNames.length; i++) {
                if (email.indexOf(allowedNames[i]) > -1) {
                    return true;
                }
            }
        }
        return false;  // right now just returning false for everyone...
    }
}
