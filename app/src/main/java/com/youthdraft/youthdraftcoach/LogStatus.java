package com.youthdraft.youthdraftcoach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.activity.signup.ExistingAccount;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.utility.DataUtils;

/**
 * Created by marty331 on 1/27/16.
 */
public class LogStatus extends Fragment {

    private static String LOG_TAG = "LogStatus";
    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_ACCOUNT = "coach_account";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_EMAIL = "coach_email";
    public static final String PROPERTY_LEAGUE = "league";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";
    public static final String PROPERTY_COACH_SEASON = "coach_season";
    public static final String PROPERTY_HEIGHT = "height_weight";
    public static final String PROPERTY_WEIGHT = "weight_weight";
    public static final String PROPERTY_SIZE   = "size_weight";
    public static final String PROPERTY_ACCEL = "accel_weight";
    public static final String PROPERTY_SPEED = "speed_weight";
    public static final String PROPERTY_HITTING = "hitting_weight";
    public static final String PROPERTY_HITTP = "hittingp_weight";
    public static final String PROPERTY_FIELDING= "fielding_weight";
    public static final String PROPERTY_CATCHING = "catching_weight";
    public static final String PROPERTY_THROW = "throwing_weight";
    public static final String PROPERTY_THROWA = "throwinga_weight";
    public static final String PROPERTY_ARM = "arm_weight";
    public static final String PROPERTY_TOUGHTNESS = "toughness_weight";
    public static final String PROPERTY_AWARE = "aware_weight";
    public static final String PROPERTY_FOCUS = "focus_weight";
    public static final String PROPERTY_EXP = "exp_weight";

    TextView textView;
    Button logout;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    AuthData authData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_status, container, false);

        logout = (Button) view.findViewById(R.id.bLogout);
        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        authData = mFirebaseRef.getAuth();
        if (authData != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        } else {
            Intent intent = new Intent(getContext(), ExistingAccount.class);
            startActivity(intent);
        }



        return view;
    }

    private void logout() {
        if (this.authData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();

            setAccountVal(null);
            setUidVal(null);
            setEmailVal(null);
            setLeagueVal(null);
            setFocusVal(0);
            setAccelVal(0);
            setArmStrnVal(0);
            setAwareVal(0);
            setTufVal(0);
            setThrowAccVal(0);
            setThrowval(0);
            setHitPowVal(0);
            setHitVal(0);
            setHeightVal(0);
            setExpVal(0);
            setCatchVal(0);
            setFieldVal(0);
            setOSpeedVal(0);
            setWeightVal(0);
            setSizeVal(0);

            PlayerManager.clearCurrentData();
            PlayerManager.clearTimeSlots();
            DataUtils.clearFirebaseConnections();


            setPropertyCoachSport(null);
            Toast.makeText(getContext(), "Logged out of YouthDraft.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void setAccountVal(String val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_ACCOUNT, val);
        editor.apply();
    }

    public void setEmailVal(String val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_EMAIL, val);
        editor.apply();
    }

    public void setUidVal(String val){
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_UID, val);
        editor.apply();
    }
    public void setLeagueVal(String val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_LEAGUE, val);
        editor.apply();
    }
    public void setPropertyCoachSport(String val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_COACH_SPORT, val);
        editor.apply();
    }

    public void setExpVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_EXP, val);
        editor.apply();

    }
    public void setFocusVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_FOCUS, val);
        editor.apply();
    }

    public void setAwareVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_AWARE, val);
        editor.apply();
    }
    public void setTufVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_TOUGHTNESS, val);
        editor.apply();
    }

    public void setArmStrnVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_ARM, val);
        editor.apply();
    }
    public void setThrowAccVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_THROWA, val);
        editor.apply();
    }

    public void setCatchVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_CATCHING, val);
        editor.apply();
    }
    public void setFieldVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_FIELDING, val);
        editor.apply();
    }

    public void setHitPowVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_HITTP, val);
        editor.apply();
    }
    public void setOSpeedVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_SPEED, val);
        editor.apply();

    }
    public void setWeightVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_WEIGHT, val);
        editor.apply();
    }
    public void setSizeVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_SIZE, val);
        editor.apply();
    }
    public void setThrowval(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_THROW, val);
        editor.apply();

    }
    public void setHitVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_HITTING, val);
        editor.apply();

    }
    public void setHeightVal(int val) {
        Context context;
        context = getActivity();
        Log.d(LOG_TAG, "setHeightVal =" + val);
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_HEIGHT, val);
        editor.apply();

    }
    public void setAccelVal(int val) {
        Context context;
        context = getActivity();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_ACCEL, val);
        editor.apply();

    }


}
