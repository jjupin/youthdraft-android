package com.youthdraft.youthdraftcoach.activity.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.youthdraft.youthdraftcoach.R;

/**
 * Created by marty331 on 1/25/16.
 */
public class PreLogin extends AppCompatActivity implements View.OnClickListener{

    private static String LOG_TAG = "PreLogin";
    Button newAccount, loginAccount;
    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_ACCOUNT = "coach_account";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_LEAGUE = "league";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_login);
        newAccount = (Button) findViewById(R.id.bNewAccount);
        newAccount.setOnClickListener(this);

        loginAccount = (Button) findViewById(R.id.login_with_password);
        loginAccount.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bNewAccount:
                Intent newA = new Intent(PreLogin.this, NewAccount.class);
                startActivity(newA);
                break;
            case R.id.login_with_password:
                Intent loginP = new Intent(PreLogin.this, ExistingAccount.class);
                startActivity(loginP);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {

        /* Update authenticated user and show login buttons */

        setAccountVal(null);
        setUidVal(null);
        setLeagueVal(null);
        setPropertyCoachSportl(null);
    }

    public void setUidVal(String val){
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_UID, val);
        editor.apply();
    }

    public void setAccountVal(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_ACCOUNT, val);
        editor.apply();
    }

    public void setLeagueVal(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_LEAGUE, val);
        editor.apply();
    }
    public void setPropertyCoachSportl(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_COACH_SPORT, val);
        editor.apply();
    }





}