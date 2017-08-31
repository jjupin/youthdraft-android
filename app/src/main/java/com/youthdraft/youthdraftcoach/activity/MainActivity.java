package com.youthdraft.youthdraftcoach.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.youthdraft.youthdraftcoach.LogStatus;
import com.youthdraft.youthdraftcoach.activity.admin.NewPlayerFragment;
import com.youthdraft.youthdraftcoach.activity.assess.AssessFragment;
import com.youthdraft.youthdraftcoach.activity.assess.AssessPlayerFragment;
import com.youthdraft.youthdraftcoach.activity.players.AssessedPlayersFragment;
import com.youthdraft.youthdraftcoach.activity.players.DraftedPlayersFragment;
import com.youthdraft.youthdraftcoach.activity.players.MyTeamRecycleFragment;
import com.youthdraft.youthdraftcoach.activity.players.PlayersFragment;
import com.youthdraft.youthdraftcoach.activity.players.RescheduleFragment;
import com.youthdraft.youthdraftcoach.activity.signup.PreLogin;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.interfaces.OnCalendarItemClickListener;
import com.youthdraft.youthdraftcoach.utility.AdminUtils;
import com.youthdraft.youthdraftcoach.utility.DataUtils;
import com.youthdraft.youthdraftcoach.utility.ScreenUtils;
import com.youthdraft.youthdraftcoach.views.RankView;
import com.youthdraft.youthdraftcoach.views.WeightView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_ACCOUNT = "coach_account";
    public static final String PROPERTY_EMAIL = "coach_email";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_FRAG = "current_frag";
    public static  final String PROPERTY_LOAD = "loaded";
    public static final String LOG_TAG = "MainActivity";
    public static final int MY_PERMISSIONS_REQUEST_EXTERNAL = 0371;
    TextView email;
    String getPropertyEmail;
    int permissionCheck;
    int id;

    private static MainActivity instance;
    private static List<OnCalendarItemClickListener> calListeners = new ArrayList<OnCalendarItemClickListener>();

    private BottomSheetBehavior mBottomSheetBehavior;
    private View mBottomSheet;
    private LinearLayout timeslotLayout;
    private View allPlayersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        this.instance = this;
        setContentView(AdminUtils.isAdmin(null, this.getBaseContext()) ? R.layout.activity_main_admin : R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        if (Firebase.getDefaultConfig().isPersistenceEnabled() == false)
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(this);

        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        ScreenUtils.setTaskBarColored(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getAccount(getApplicationContext())==null){
            Intent intent = new Intent(MainActivity.this, PreLogin.class);
            startActivity(intent);
        }



        if (ScreenUtils.isTablet(getBaseContext()) == false) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        email = (TextView) view.findViewById(R.id.tvUserEmail);

        getPermissions();

        PlayersFragment playersFrag = new PlayersFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, playersFrag);
        fragmentTransaction.commit();

        /**
        RankView rankView = new RankView();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,rankView);
        fragmentTransaction.commit();
         **/

        mBottomSheet = findViewById( R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheet.setVisibility(View.INVISIBLE);

        timeslotLayout = (LinearLayout)findViewById(R.id.timeslotLayout);

        allPlayersView = findViewById(R.id.all_players);
        allPlayersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mBottomSheet.setVisibility(View.INVISIBLE);
                for (OnCalendarItemClickListener listen : calListeners) {
                    listen.onCalendarItemClick("all");
                }
            }
        });

        DataUtils.loadDataFromFirebase(this);
    }

    //
    // Adding a hook to allow an activity result to propagate down to the fragments
    //

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static BottomSheetBehavior getBottomSheetBehavior() {
        return MainActivity.instance.mBottomSheetBehavior;
    }

    public static void addCalendarItemClickListener(OnCalendarItemClickListener newListener) {
        calListeners.add(newListener);
    }

    public static void addTimeslotsToBottomSheet(List<String> timeslots) {
        LinearLayout ll = MainActivity.instance.timeslotLayout;
        int counter = 0;
        for (String timeslot : timeslots) {

            String s = PlayerManager.getTimeslotToSecondsMap().get(timeslot);

            //Creating LinearLayout.
            LinearLayout linearlayout = new LinearLayout(MainActivity.instance);

            //Setting up LinearLayout Orientation
            linearlayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams linearlayoutlayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


            final TextView rowTextView = new TextView(MainActivity.instance);
            counter++;
            int colorId =  counter%2 == 0 ? R.color.pale_gray : R.color.pale_yellow;
            rowTextView.setBackgroundColor(ContextCompat.getColor(MainActivity.instance.getBaseContext(), colorId));
            rowTextView.setPadding(4,4,4,4);
            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            // set some properties of TextView
            rowTextView.setText(s);
            rowTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            rowTextView.setTextSize(16);

            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.instance.mBottomSheet.setVisibility(View.INVISIBLE);
                    TextView pickedTimeslot = (TextView)view;

                    for (OnCalendarItemClickListener listen : calListeners) {
                        listen.onCalendarItemClick(pickedTimeslot.getText().toString());
                    }
                }
            });

            linearlayout.addView(rowTextView);

            final TextView rowTextView2 = new TextView(MainActivity.instance);
            rowTextView2.setBackgroundColor(ContextCompat.getColor(MainActivity.instance.getBaseContext(), colorId));
            rowTextView2.setPadding(4,4,4,4);
            rowTextView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            // set some properties of TextView
            rowTextView2.setText("( " + PlayerManager.getPlayersForTimeslot(s).size() + " )");
            rowTextView2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            rowTextView2.setTextSize(14);

            linearlayout.addView(rowTextView2);

            // add the textview to the linearlayout
            ll.addView(linearlayout);
        }
    }

    public static View getBottomSheet() {
        return MainActivity.instance.mBottomSheet;
    }

    private void getPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        getPropertyEmail = getEmail(getApplicationContext());
        //Log.d(LOG_TAG, "email =" + getPropertyEmail.toString());
        email.setText(getPropertyEmail.toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_players_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    public void onSaveInstanceState(Bundle outState){
        outState.putInt("id", id);

    }
    public void onRestoreInstanceState(Bundle inState){
        id = inState.getInt("id");


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (id) {
//            case R.id.nav_camera:
//                Log.e(LOG_TAG, "assessplayer id="+id);
//                AssessPlayerFragment apFragment = new AssessPlayerFragment();
//                fragmentTransaction.replace(R.id.frame, apFragment);
//                fragmentTransaction.commit();
//                break;
//
//            case R.id.nav_gallery:
//                Log.e(LOG_TAG, "assessactivity id="+id);
//                AssessFragment aaFragment = new AssessFragment();
//                fragmentTransaction.replace(R.id.frame, aaFragment);
//                fragmentTransaction.commit();
//                break;

            case R.id.nav_my_team:
                Log.e(LOG_TAG, "myTeam id="+id);

                showNotAvailableMessage("Coach's My Team");

                /**
                if (myTeamFragment == null) {
                    myTeamFragment = new MyTeamRecycleFragment();
                }
                fragmentTransaction.replace(R.id.frame, myTeamFragment);
                fragmentTransaction.commit();
                 **/
                //setTitle("My Team Players");
                break;

            case R.id.nav_settings:
                Log.e(LOG_TAG, "myTeam id="+id);

                showNotAvailableMessage("Settings");

                //setTitle("Settings");
                break;


            case R.id.nav_drafted:
                Log.e(LOG_TAG, "myTeam id="+id);

                showNotAvailableMessage("Drafted Players");

                /**
                if (draftedPlayersFragment == null) {
                    draftedPlayersFragment = new DraftedPlayersFragment();
                }
                fragmentTransaction.replace(R.id.frame, draftedPlayersFragment);
                fragmentTransaction.commit();
                 **/
                //setTitle("Drafted PLayers");
                break;

            case R.id.nav_assessed_players:
                Log.e(LOG_TAG, "assessed players id="+id);

                if (assessedPlayersFragment == null) {
                    assessedPlayersFragment = new AssessedPlayersFragment();
                }
                fragmentTransaction.replace(R.id.frame, assessedPlayersFragment);
                fragmentTransaction.commit();
                setTitle("Assessed Players");
                break;

            case R.id.nav_nonscheduled:
                Log.e(LOG_TAG, "nonscheduled id=" +id);
                if (rescheduleFragment == null) {
                    rescheduleFragment = new RescheduleFragment();
                }
                fragmentTransaction.replace(R.id.frame, rescheduleFragment);
                fragmentTransaction.commit();
                setTitle("Schedule Players");
                break;

//            case R.id.nav_ranking:
//                RankView rankView = new RankView();
//                fragmentTransaction.replace(R.id.frame, rankView);
//                fragmentTransaction.commit();
//                setTitle("Ranked Players");
//                break;

//            case R.id.nav_weight:
//                WeightView weightView = new WeightView();
//                fragmentTransaction.replace(R.id.frame, weightView);
//                fragmentTransaction.commit();
//                break;

            case R.id.nav_logout:
                LogStatus logStatus = new LogStatus();
                fragmentTransaction.replace(R.id.frame, logStatus);
                fragmentTransaction.commit();
                break;

            case R.id.nav_new_player:
                Log.e(LOG_TAG, "nav new player = " + id);
                if (newPlayerFragment == null) {
                    newPlayerFragment = new NewPlayerFragment();
                }
                fragmentTransaction.replace(R.id.frame, newPlayerFragment);
                fragmentTransaction.commit();
                setTitle("Admin: Add New Player");
                break;



            default:
                Log.e(LOG_TAG, "default view -> players tab view");
                if (playersFragment == null) {
                    playersFragment = new PlayersFragment();
                }
                fragmentTransaction.replace(R.id.frame, playersFragment);
                fragmentTransaction.commit();
                setTitle("Players");

        }

        if (ScreenUtils.isTablet(getBaseContext()) == false) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void showNotAvailableMessage(String selection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(selection + " is currently unavailable.  \n Please try again at a later time.")
                .setTitle("Menu selection is not available.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private PlayersFragment playersFragment;
    private MyTeamRecycleFragment myTeamFragment;
    private RescheduleFragment rescheduleFragment;
    private AssessedPlayersFragment assessedPlayersFragment;
    private DraftedPlayersFragment draftedPlayersFragment;
    private NewPlayerFragment newPlayerFragment;

    public void setAccountVal(String val) {
        Context context;
        context = this;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PROPERTY_ACCOUNT, val);
        editor.apply();
    }

    private String getAccount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_ACCOUNT, null);

        return value;
    }

    private String getEmail(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_EMAIL,"Not logged in.");

        return value;
    }
}
