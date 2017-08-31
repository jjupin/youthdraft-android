package com.youthdraft.youthdraftcoach.activity.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.datamodel.UserInfo;
import com.youthdraft.youthdraftcoach.utility.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marty331 on 1/25/16.
 */
public class ExistingAccount extends AppCompatActivity implements View.OnClickListener{

    private static String LOG_TAG = "ExistingAccount";
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


    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef, mFireFind;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    /* *************************************
     *              PASSWORD               *
     ***************************************/
    private Button mPasswordLoginButton;
    EditText password, email, leagueval;
    Spinner sport, season, year;
    String sportPicked, token, provid, uid;
    String league = "";
    String playseason = "";
    String playyear = "";
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_account);

        Firebase.setAndroidContext(this);
        password = (EditText) findViewById(R.id.ea_etPassword);
        email = (EditText) findViewById(R.id.ea_etEmail);
        mPasswordLoginButton = (Button) findViewById(R.id.bLogin);
        mPasswordLoginButton.setOnClickListener(this);
        sport = (Spinner) findViewById(R.id.sEASport);
        season = (Spinner) findViewById(R.id.sEASeason);
        year = (Spinner) findViewById(R.id.sEAYear);
        leagueval = (EditText) findViewById(R.id.ea_etLeague);
        forgot = (TextView) findViewById(R.id.tvForgotPassword);
        forgot.setText(Html.fromHtml(
                        "<a href=\"https://blistering-fire-5846.firebaseapp.com/#/reset\">Forgot Password</a> "));
        forgot.setMovementMethod(LinkMovementMethod.getInstance());

        //
        // TEMP code
        //
        // Hard-coding the email address to make it fast for folks to use - for now...
        //

        //email.setText("john.bull@gmail.com"); //chris@youthdraft.com");
        //password.setText("bulls"); //"usma1994");
        leagueval.setText("strut");


        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);

        ArrayList<String> sports = new ArrayList<>();
        sports.add(0, "Baseball");
        //sports.add(1, "Softball");

        ArrayList<String> seasons = new ArrayList<>();
        seasons.add(0, "Spring");
        seasons.add(1, "Summer");
        seasons.add(2, "Fall");
        seasons.add(3, "Winter");

        ArrayList<String> years = new ArrayList<>();
        years.add(0, "2017");

        /**
        years.add(2, "2018");
        years.add(3, "2019");
        years.add(4, "2020");
         **/


        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sports);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sport.setAdapter(spinnerArrayAdapter);

        final ArrayAdapter<String> spinnerSeasonAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seasons);
        spinnerSeasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season.setAdapter(spinnerSeasonAdapter);

        final ArrayAdapter<String> spinnerYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinnerYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(spinnerYearAdapter);

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(mAuthProgressDialog.isShowing()){
                    mAuthProgressDialog.hide();
                }
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFirebaseRef.addAuthStateListener(mAuthStateListener);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bLogin:
                loginWithPassword();
                break;
        }
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
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();

            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
            setAccountVal(null);
            setUidVal(null);
            setEmailVal(null);
            setLeagueVal(null);

            PlayerManager.clearCurrentData();

            setPropertyCoachSport(null);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    /* ************************************
     *              PASSWORD              *
     **************************************
     */
    public void loginWithPassword() {

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        mAuthProgressDialog.show();
        league = leagueval.getText().toString();
        Log.d(LOG_TAG, "email =" + email.getText().toString() + " pass =" + password.getText().toString());
        mFirebaseRef.authWithPassword(email.getText().toString(), password.getText().toString(), new AuthResultHandler("password"));

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
            // Generate a new secure JWT
            Log.d(LOG_TAG, "uid =" + authData.getUid());
            Log.d(LOG_TAG, "league ="+league);
            provid = authData.getProvider().toString();
            uid = authData.getUid().toString();
            setAuthenticatedUser(authData);


            mFireFind = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+authData.getUid()+"/"+league);
            mFireFind.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //league =dataSnapshot.getValue().toString();
                    //Log.d(LOG_TAG, "league ="+league);
                    Log.d(LOG_TAG, "data =" + dataSnapshot.getChildrenCount());
                    Log.d(LOG_TAG, "key =" + dataSnapshot.getValue().toString());
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    Map<String, Object> payload = new HashMap<String, Object>();

                    for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                        Log.d(LOG_TAG, "key ="+postSnap.getKey());
                        if(postSnap.getKey().toString().equals("league")){
                            league = userInfo.getLeague();
                            setLeagueVal(league);
                            payload.put("league", league);
                        }
                        if(postSnap.getKey().toString().equals("sport")){
                            sportPicked = sport.getSelectedItem().toString();
                            Log.d(LOG_TAG, "sportpiced =" + sportPicked);
                            setPropertyCoachSport(sportPicked);
                            payload.put("sport", sportPicked);
                        }
                        if(postSnap.getKey().toString().equals("season")){
                            playseason = season.getSelectedItem().toString();
                            setPropertyCoachSeason(playseason);
                            Log.d(LOG_TAG, "playseason ="+playseason);

                            playyear = year.getSelectedItem().toString();
                            setPropertyCoachYear(playyear);
                            Log.d(LOG_TAG, "playyear ="+playyear);
                            payload.put("season", playseason+playyear);
                        }



                    }
                    mFirebaseRef.child("users").child(uid).updateChildren(payload);

                    Intent intent = new Intent(ExistingAccount.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pageloaded", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(LOG_TAG, "firebaseerror =" + firebaseError.toString());

                }
            });

//            Intent intent = new Intent(ExistingAccount.this, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putInt("pageloaded", 1);
//            intent.putExtras(bundle);
//            startActivity(intent);


        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }


    }
    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */

            //mPasswordLoginButton.setVisibility(View.GONE);

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
                setAccountVal(authData.getToken().toString());
                setUidVal(authData.getUid().toString());
                setEmailVal(email.getText().toString());
                if (Constants.coachesMap.containsKey(authData.getUid())) {
                    Toast.makeText(getBaseContext(), "we found coach " + Constants.coachesMap.get(authData.getUid().toString()), Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            /* No authenticated user show all the login buttons */
            mPasswordLoginButton.setVisibility(View.VISIBLE);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
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

    private String getUid(Context context){
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_UID, null);

        return value;
    }

    private String getPropertyCoachSport(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_COACH_SPORT, null);

        return value;
    }

    private String getLeague(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_LEAGUE, null);

        return value;
    }

    public void setLeagueVal(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_LEAGUE, val);
        editor.apply();
    }
    public void setPropertyCoachSport(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_COACH_SPORT, val);
        editor.apply();
    }

    public void setPropertyCoachSeason(String val){
        Context context;
        context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROPERTY_COACH_SEASON, val);
        editor.apply();
    }

    public void setPropertyCoachYear(String val){
        Context context;
        context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROPERTY_COACH_YEAR, val);
        editor.apply();
    }




}