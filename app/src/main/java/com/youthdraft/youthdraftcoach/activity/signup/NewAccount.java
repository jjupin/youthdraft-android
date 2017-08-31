package com.youthdraft.youthdraftcoach.activity.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marty331 on 1/25/16.
 */
public class NewAccount extends AppCompatActivity {

    private static String LOG_TAG = "NewAccount";
    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_ACCOUNT = "coach_account";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_EMAIL = "coach_email";
    public static final String PROPERTY_LEAGUE = "league";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";
    public static final String PROPERTY_COACH_SEASON = "coach_season";
    public static final String PROPERTY_COACH_YEAR = "coach_year";

    /* *************************************
     *              GENERAL                *
     ***************************************/
    /* TextView that is used to display information about the logged in user */
    private TextView mLoggedInStatusTextView;

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    /* *************************************
     *              PASSWORD               *
     ***************************************/
    EditText password, email, token, league;
    Button savePass;
    Spinner sport;
    Spinner season;
    Spinner year;
    String sportPicked;
    String seasonPicked;
    String yearPicked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);

        Firebase.setAndroidContext(this);



        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        token = (EditText) findViewById(R.id.etToken);
        token.setText("gobble1");
        token.setEnabled(false);
        league = (EditText) findViewById(R.id.etLeague);
        league.setText("strut");
        league.setEnabled(false);
        sport = (Spinner) findViewById(R.id.sSport);
        season = (Spinner) findViewById(R.id.sSeason);
        year = (Spinner) findViewById(R.id.sYear);
        savePass = (Button) findViewById(R.id.bContinue);
        savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthProgressDialog.show();
                sportPicked = sport.getSelectedItem().toString();
                Log.d(LOG_TAG, "sportpiced =" + sportPicked);
                seasonPicked = season.getSelectedItem().toString();
                Log.d(LOG_TAG, "seasonpicked =" + seasonPicked);
                yearPicked = year.getSelectedItem().toString();
                Log.d(LOG_TAG, "yearpicked =" +yearPicked);


                Log.d(LOG_TAG, "email =" + email.getText().toString() + " password =" + password.getText().toString());
                mFirebaseRef.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Log.d(LOG_TAG, "result ="+result.get("uid"));
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        loginWithPassword();
                        //setEmailVal(email.getText().toString());

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        mAuthProgressDialog.dismiss();
                        Log.e(LOG_TAG, "error =" + firebaseError.toString());
                        Toast.makeText(getApplicationContext(), "Error: " + firebaseError.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ArrayList<String> sports = new ArrayList<>();
        sports.add(0, "Baseball");
        //sports.add(1, "Softball");

        ArrayList<String> seasons = new ArrayList<>();
        seasons.add(0, "Spring");
        seasons.add(1, "Summer");
        seasons.add(2, "Fall");
        seasons.add(3, "Winter");

        ArrayList<String> years = new ArrayList<>();
       // years.add(0, "2016");
        years.add(0, "2017");
        //years.add(2, "2018");
        //years.add(3, "2019");
        //years.add(4, "2020");

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sports);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sport.setAdapter(spinnerArrayAdapter);

        final ArrayAdapter<String> spinnerSeasonArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seasons);
        spinnerSeasonArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season.setAdapter(spinnerSeasonArray);

        final ArrayAdapter<String> spinnerYearArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinnerYearArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(spinnerYearArray);

        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        /* *************************************
         *               GENERAL               *
         ***************************************/
        mLoggedInStatusTextView = (TextView) findViewById(R.id.login_status);

        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        //mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFirebaseRef.addAuthStateListener(mAuthStateListener);

    }

    /* ************************************
     *              PASSWORD              *
     **************************************
     */
    public void loginWithPassword() {

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword(email.getText().toString(), password.getText().toString(), new AuthResultHandler("password"));

    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {

            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")
                    || authData.getProvider().equals("google")
                    || authData.getProvider().equals("twitter")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                Log.e(LOG_TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                mLoggedInStatusTextView.setText("Logged in as " + authData.getProvider().toString() );//+ " (" + authData.getProvider() + ")"+" is a Coach "+authData.getToken().toString());
                setAccountVal(authData.getToken().toString());
                setUidVal(authData.getUid().toString());
                String e = email.getText().toString();
                Log.d(LOG_TAG, "e ="+e);
                setEmailVal(e);

            }
        } else {
            /* No authenticated user show all the login buttons */
            mLoggedInStatusTextView.setVisibility(View.GONE);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if changing configurations, stop tracking firebase session.
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
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

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    public void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();


            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
            setAccountVal(null);
            setUidVal(null);
            setEmailVal(null);
            setLeagueVal(null);
            setPropertyCoachSportl(null);
        }
    }
    /**
     * This method will attempt to authenticate a user to firebase given an oauth_token (and other
     * necessary parameters depending on the provider)
     */
    private void authWithFirebase(final String provider, Map<String, String> options) {
        if (options.containsKey("error")) {
            showErrorDialog(options.get("error"));
        } else {
            mAuthProgressDialog.show();
            if (provider.equals("twitter")) {
                // if the provider is twitter, we pust pass in additional options, so use the options endpoint
                mFirebaseRef.authWithOAuthToken(provider, options, new AuthResultHandler(provider));
            } else {
                // if the provider is not twitter, we just need to pass in the oauth_token
                mFirebaseRef.authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
            }
        }
    }


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void setAccountVal(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_ACCOUNT, val);
        editor.apply();
    }

    public void setEmailVal(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_EMAIL, val);
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

    public void  setPropertyYearPicked(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_COACH_YEAR, val);
        editor.apply();
    }


    public void setPropertyCoachSeason(String val){
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_COACH_SEASON, val);
        editor.apply();
    }

    public void setUidVal(String val){
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_UID, val);
        editor.apply();
    }

    private String getAccount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_ACCOUNT, null);

        return value;
    }

    private String getLeague(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_LEAGUE, null);

        return value;
    }

    private String getPropertyCoachSport(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_COACH_SPORT, null);

        return value;
    }


    private String getUid(Context context){
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_UID, null);

        return value;
    }




    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(LOG_TAG, provider + " auth successful");
            Log.d(LOG_TAG, "token =" + token.getText().toString());
            setLeagueVal(league.getText().toString());
            setPropertyCoachSportl(sportPicked);
            setPropertyCoachSeason(seasonPicked);
            setPropertyYearPicked(yearPicked);
            String leagueVal = league.getText().toString();
            //Generate coaches node
            Map<String, Object> coachload = new HashMap<>();
            coachload.put("league", leagueVal);
            coachload.put("token", token.getText().toString());
            coachload.put("season", seasonPicked + yearPicked);
            coachload.put("sport", sportPicked);
            mFirebaseRef.child("coaches").child(authData.getUid()).setValue(coachload);


            // Generate a new secure JWT
            Map<String, Object> payload = new HashMap<String, Object>();

            //payload.put("uid", authData.getUid().toString());
            payload.put("isCoach", true);
            payload.put("provider", authData.getProvider());
            payload.put("email", email.getText().toString());
            payload.put("token", token.getText().toString());
            payload.put("league", leagueVal);
            payload.put("sport", sportPicked);
            if(sportPicked == "Baseball" || sportPicked =="Softball"){
                payload.put("wHeight", 100);
                payload.put("wWeight", 100);
                payload.put("wHitting", 100);
                payload.put("wSpeed", 100);
                payload.put("wArm", 100);
                payload.put("wBat", 100);
                payload.put("wInfield", 100);
                payload.put("wOutfield", 100);
                payload.put("wThrowing", 100);
                payload.put("wBase", 100);
            }
           /* if(sportPicked == "Softball"){
                payload.put("wHeight", 100);
                payload.put("wWeight", 100);
                payload.put("wHitting", 100);
                payload.put("wSpeed", 100);
                payload.put("wArm", 100);
                payload.put("wBat", 100);
                payload.put("wInfield", 100);
                payload.put("wOutfield", 100);
                payload.put("wThrowing", 100);
                payload.put("wBase", 100);
            }*/
            payload.put("season", seasonPicked + yearPicked );

            if(authData.getProviderData().containsKey("displayName")) {
                payload.put("displayName", authData.getProviderData().get("displayName").toString());
            }

            mFirebaseRef.child("users").child(authData.getUid()).child(leagueVal).setValue(payload);

            setAuthenticatedUser(authData);
            Intent intent = new Intent(NewAccount.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("pageloaded", 1);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }


    }

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getWebIcon(){
            String icon;
            icon = String.valueOf(R.drawable.ic_info_outline_black);
            Log.d(LOG_TAG, "webappinterface webicon =" + icon);
            return icon;
        }

        @JavascriptInterface
        public String getWebSumm(){
            String summ;
            summ =" This is a test.";
            Log.d(LOG_TAG, "webappinterface websum ="+summ);
            return summ;
        }

        @JavascriptInterface
        public String getDisasterType(){
            String distype;
            distype = "Login screen";
            return distype;
        }

        @JavascriptInterface
        public void dismissAlert(){

        }
        @JavascriptInterface
        public void createAccount(String email, String passd, String tok){
            token.setText(tok);
            mFirebaseRef.createUser(email, passd, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    loginWithPassword();

                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    // there was an error
                    mAuthProgressDialog.dismiss();
                    Log.e(LOG_TAG, "error =" + firebaseError.toString());
                    Toast.makeText(getApplicationContext(), "Error: " + firebaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }




}