package com.youthdraft.youthdraftcoach.activity.assess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jjupin on 12/14/16.
 */

public class AssessPlayerActivity extends AppCompatActivity {

    public static String LOG_TAG = "AssessPlayerActivity";

    //
    // UI elements
    //

    SeekBar heightp;
    SeekBar weightp, hittingp, batp, infieldp, outfieldp, throwp, armp, speedp, basep;
    EditText playerNotes;
    Button saveButton;
    TextView playerSpinner, timeSlot;
    TextView heightpVal, weightpVal, hittingVal, batVal, infieldVal, outfieldVal, throwingVal, armVal, speedVal, baseVal,
            playId, birthdayval;
    CheckBox draft;
    HashMap<String, PlayerInfo> playMap;
    SharedPreferences sharedpreferences;
    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_FRAG = "current_frag";
    public static final String PROPERTY_HEIGHT = "height_weight";
    public static final String PROPERTY_WEIGHT = "weight_weight";
    public static final String PROPERTY_HITTING = "hitting_weight";
    public static final String PROPERTY_BAT_SPEED = "bat_speed_weight";
    public static final String PROPERTY_INFIELD = "infield_weight";
    public static final String PROPERTY_OUTFIELD= "outfield_weight";
    public static final String PROPERTY_THROW = "throwing_weight";
    public static final String PROPERTY_ARM = "arm_weight";
    public static final String PROPERTY_SPEED = "speed_weight";
    public static final String PROPERTY_BASE = "base_weight";
    public static final String PROPERTY_ACCOUNT = "coach_account";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_LEAGUE = "league";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";
    public static final String PROPERTY_COACH_SEASON = "coach_season";
    public static final String PROPERTY_COACH_YEAR = "coach_year";
    String first, last, midInitial, playerid;
    ProgressDialog progressDialog;

    Firebase firebase, firebaseplayers;
    String accountuid, leagueid, sport, season, coachYear;
    int jer;

    LinearLayout play, act, scrollin;

    //
    // Player being assessed...
    //

    private PlayerInfo playerInfo;
    private PlayerScores playerScores;

    List<String> activityList = new ArrayList<String>();
    List<PlayerScores> playerScoresList = new ArrayList<PlayerScores>();
    List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
    List<String> timelist = new ArrayList<String>();
    List<String> timeCheck = new ArrayList<String>();
    HashMap<String, List<String>> playerList = new HashMap<String, List<String>>();
    List<String> finalPlayers = new ArrayList<String>();
    String listTime;
    List<String> playerNames = new ArrayList<String>();
    HashMap<String, String> playerNameID = new HashMap<String, String>();
    int playerScoresPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.coach_view);

        Firebase.setAndroidContext(getBaseContext());
        accountuid = getUid(getBaseContext());
        leagueid = getLeague(getBaseContext());
        sport = getPropertyCoachSport(getBaseContext());
        season = getPropertyCoachSeason(getBaseContext());
        coachYear = getPropertyCoachYear(getBaseContext());
        //progressDialog.show();

        bindActivity();
        unbundleOptionalData();
    }

    private void bindActivity() {

        playId = null; //(TextView) findViewById(R.id.tvID);
        playerSpinner = (TextView) findViewById(R.id.sPlayerName);
        timeSlot = (TextView) findViewById(R.id.sTimeSlot);
        birthdayval = (TextView) findViewById(R.id.tvBirthday);
        heightp = (SeekBar) findViewById(R.id.sHeight);
        weightp = (SeekBar) findViewById(R.id.sWeight);
        hittingp = (SeekBar) findViewById(R.id.sHitMech);
        batp = (SeekBar) findViewById(R.id.sBatSpeed);
        infieldp = (SeekBar) findViewById(R.id.sInfield);
        outfieldp = (SeekBar) findViewById(R.id.sOutfield);
        throwp = (SeekBar) findViewById(R.id.sThrowing);
        armp = (SeekBar) findViewById(R.id.sArm);
        speedp = (SeekBar) findViewById(R.id.sSpeed);
        basep = (SeekBar) findViewById(R.id.sBase);

        draft = (CheckBox) findViewById(R.id.cbDraft);
        playerNotes = (EditText) findViewById(R.id.etNote);
        saveButton = (Button) findViewById(R.id.bSavePlayer);

        heightpVal = (TextView) findViewById(R.id.tvHeightVal);
        weightpVal = (TextView) findViewById(R.id.tvWeightVal);
        hittingVal= (TextView) findViewById(R.id.tvHitMecVal);
        batVal = (TextView) findViewById(R.id.tvBatSpeedVal);
        infieldVal = (TextView) findViewById(R.id.tvInfieldVal);
        outfieldVal = (TextView) findViewById(R.id.tvOutfieldVal);
        throwingVal= (TextView) findViewById(R.id.tvThrowingVal);
        armVal = (TextView) findViewById(R.id.tvArmVal);
        speedVal = (TextView) findViewById(R.id.tvSpeedVal);
        baseVal = (TextView) findViewById(R.id.tvBaseVal);

        act = (LinearLayout) findViewById(R.id.activityassess);
        play = (LinearLayout) findViewById(R.id.playerassess);
        play.setVisibility(View.VISIBLE);
        activityList.clear();

//        playMap = new HashMap<String, PlayerInfo>();
//        progressDialog = new ProgressDialog(getBaseContext());
//        progressDialog.setMessage("Loading Players....");

        this.setupAssessementSeekBars();

        //
        // setting up the action for the save button
        //

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playid = playerid;

                String bir = birthdayval.getText().toString();
                String wet = weightpVal.getText().toString();
                String het = heightpVal.getText().toString();
                String hit = hittingVal.getText().toString();
                String bat = batVal.getText().toString();
                String inf = infieldVal.getText().toString();
                String outf = outfieldVal.getText().toString();
                String throwing = throwingVal.getText().toString();
                String armstrength = armVal.getText().toString();
                String speed = speedVal.getText().toString();
                String base = baseVal.getText().toString();
                //String pteam = priorTeamVal.getText().toString();
                //String plevel = priorLevelVal.getText().toString();
                //String leagueAge = leagueAgeVal.getText().toString();

                String draftable;
                if (draft.isChecked()){
                    draftable = "No";
                } else {
                    draftable = "Yes";
                }

                Log.d(LOG_TAG, "draftable ="+draftable);
                String drafted = "0";
                if (draftable.equals("Yes")) {
                    drafted = "1";
                }
                String notes = playerNotes.getText().toString();

                double height_weight = getPropertyHeight(getBaseContext())/100;
                double weight_weight = getPropertyWeight(getBaseContext())/100;
                double hitting_weight = getHit(getBaseContext())/100;
                double batspeed_weight = getPropertyBatSpeed(getBaseContext())/100;
                double infield_weight = getInfield(getBaseContext())/100;
                double outfield_weight = getOutfield(getBaseContext()) /100;
                double throw_weight = getThrow(getBaseContext())/100;
                double arm_weight = getPropertyArm(getBaseContext())/100;
                double speed_weight = getPropertySpeed(getBaseContext())/100;
                double base_weight = getPropertyBase(getBaseContext())/100;


                int wett = Integer.valueOf(wet);
                int hett = Integer.valueOf(het);
                int hitt = Integer.valueOf(hit);
                int batt = Integer.valueOf(bat);
                int infl = Integer.valueOf(inf);
                int outfl = Integer.valueOf(outf);
                int thr = Integer.valueOf(throwing);
                int arms = Integer.valueOf(armstrength);
                int sped = Integer.valueOf(speed);
                int bse = Integer.valueOf(base);
                Log.d(LOG_TAG, "height ="+hett+" weight ="+wett+" hitting ="+hitt+" batting ="+batt+" infield ="+infl+" outfield ="+outf+" throwing ="+thr+" arm ="+arms+" speed ="+sped+" base ="+bse);


                Calendar c = Calendar.getInstance();
                String seconds = String.valueOf(c.get(Calendar.SECOND));
                if(seconds.length()==1){
                    seconds = "0"+seconds;
                }
                String min = String.valueOf(c.get(Calendar.MINUTE));
                if(min.length()==1){
                    min = "0"+min;
                }
                String hour = String.valueOf(c.get(Calendar.HOUR));
                if(hour.length()==1){
                    hour = "0"+hour;
                }
                String year = String.valueOf(c.get(Calendar.YEAR));
                String mnt = String.valueOf(c.get(Calendar.MONTH)+1);
                String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                String dateval = year+"-"+mnt+"-"+day+" "+hour+":"+min+":"+seconds;
                Log.d(LOG_TAG, "datetime ="+dateval);



                double ranked = 0.0;
                ranked = (height_weight * hett) + (weight_weight * wett) + (hitting_weight * hitt) + (batspeed_weight * batt) +
                        (infield_weight * infl) + (outfield_weight * outfl) + (throw_weight * thr) + (arm_weight * arms) +
                        (base_weight * bse) + (speed_weight * sped);
                String accountuid = getUid(getBaseContext());
                double rawscore =  hett + wett + hitt + batt + infl + outfl + thr + arms + sped + bse;
                float rranked = (float)ranked;
                float rrawscore  = (float)rawscore;

                PlayerScores playerScores = new PlayerScores(playid, first.trim(), last.trim(), midInitial, bir,  jer, hett, wett,  hitt, batt, infl, outfl, thr, arms, sped, bse, draftable, notes, dateval, ranked, false);
                String firebaseUrl = "https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid +"/ranked/"+sport+"/"+season+coachYear+"/"+playid;
                firebase = new Firebase(firebaseUrl);
                firebase.setValue(playerScores, - playerScores.getPrank());

                // I assume it's gone up - need to put code here to catch a returned errors...

                PlayerManager.addPlayerScore(playerScores, getUid(getBaseContext()));

                Toast.makeText(getBaseContext(), "Finshed storing scores for " + playerInfo.getFullname() + ".  Player moved to Assesed region.", Toast.LENGTH_SHORT).show();
                AssessPlayerActivity.this.setResult(Activity.RESULT_OK);
                AssessPlayerActivity.this.finish();

                /**
                Log.d(LOG_TAG, "playid ="+playid);
                Log.d(LOG_TAG, "save fullname ="+first+" "+last);
                if (playerScoresPosition < -1){
                    playerScoresList.add(playerScoresPosition, new PlayerScores(playid, first, last, midInitial, bir, jer, hett, wett, hitt, batt, outfl, infl, thr, arms, sped, bse, draftable, notes, dateval, ranked));
                }
                 **/

            }
        });
    }

    private void unbundleOptionalData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String leagueidPlay = extras.getString("playerid");
            if (leagueidPlay != null && leagueidPlay.length() > 0) {
                if (PlayerManager.getPlayersMap().containsKey(leagueidPlay)) {
                    playerInfo = PlayerManager.getPlayersMap().get(leagueidPlay);
                    if (PlayerManager.getPlayerScores().containsKey(leagueidPlay)) {
                        Map<String, PlayerScores> scores = PlayerManager.getPlayerScores().get(leagueidPlay);
                        if (scores != null && scores.containsKey(getUid(getBaseContext()))) {
                            playerScores = scores.get(getUid(getBaseContext())); // just grabbing the first one for now...
                        }
                    }
                }
            }

        }

        //
        // Now, if we've found the player, go ahead and insert his data - otherwise,
        // put in the default base values.
        //

        if (playerInfo != null) {
            playerSpinner.setText(playerInfo.getFullname());
            timeSlot.setText(playerInfo.getTryout_date() + " " + playerInfo.getTryout_time());
            if (playerScores != null) {
                playerid = playerScores.getPlayerid();
                first = playerScores.getFirstname().replaceAll("\\s+", "");
                last = playerScores.getLastname().replaceAll("\\s+", "");
                midInitial = playerScores.getMidinitial();
                birthdayval.setText(playerScores.getBirth());
                jer = 0;
                //jer = playerScores.getJersey();
                playId.setText(String.valueOf(jer));
                heightp.setProgress(playerScores.getPheight()); //setTickStart(playerScoresList.get(i).getPheight());
                weightp.setProgress(playerScores.getPweight());
                hittingp.setProgress(playerScores.getPhit());
                batp.setProgress(playerScores.getPbat());
                infieldp.setProgress(playerScores.getPinfield());
                outfieldp.setProgress(playerScores.getPoutfield());
                throwp.setProgress(playerScores.getPthrow());
                armp.setProgress(playerScores.getParm());
                speedp.setProgress(playerScores.getPspeed());
                basep.setProgress(playerScores.getPbase());

                String drafted = playerScores.getPdraft();
                if (drafted.equals("No")) {
                    draft.setChecked(true);
                } else {
                    draft.setChecked(false);
                }
                playerNotes.setText(playerScores.getPnote());
            } else {
                playerid = playerInfo.getLeagueid();
                first = playerInfo.getFirstname().replaceAll("\\s+","");
                last = playerInfo.getLastname().replaceAll("\\s+","");
                midInitial = playerInfo.getMiddlename();
                birthdayval.setText(playerInfo.getLeagueage()+"");
                jer = 0;
                //jer = playerInfo.getJersey();
                heightp.setProgress(0); //setTickStart(0);
                weightp.setProgress(0);
                hittingp.setProgress(0);
                batp.setProgress(0);
                infieldp.setProgress(0);
                outfieldp.setProgress(0);
                throwp.setProgress(0);
                armp.setProgress(0);
                speedp.setProgress(0);
                basep.setProgress(0);
                draft.setChecked(false);
                playerNotes.setText(null);
            }

        }
    }

    //
    // Set up the individual seekBar widgets so that the data values are tied to the appropriate widgets accordingly
    //

    private void setupAssessementSeekBars() {
        heightp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightpVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        weightp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weightpVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hittingp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hittingVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        batp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                batVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        infieldp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                infieldVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        outfieldp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                outfieldVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        throwp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                throwingVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        armp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                armVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        speedp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        basep.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                baseVal.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        draft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    //
    // some generic utils to manipulate app preference items...
    //

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

    private String getLeague(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_LEAGUE, null);

        return value;
    }

    private String getPropertyCoachSport(Context context){
        final SharedPreferences preferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = preferences.getString(PROPERTY_COACH_SPORT, null);

        return value;
    }
    private String getPropertyCoachSeason(Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(PROPERTY_COACH_SEASON, null);
        return value;
    }

    private  String getPropertyCoachYear(Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(PROPERTY_COACH_YEAR, null);
        return value;
    }

    public int getPropertyArm(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_ARM, 100);

        return value;
    }

    public int getPropertyBase(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_BASE, 100);

        return value;
    }

    public int getPropertyBatSpeed(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_BAT_SPEED, 100);

        return value;
    }

    public int getPropertyHeight(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_HEIGHT, 100);

        return value;
    }

    public int getPropertySpeed(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_SPEED, 100);

        return value;
    }

    public int getPropertyWeight(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_WEIGHT, 100);

        return value;
    }

    private int getInfield(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_INFIELD, 100);

        return value;
    }
    private int getOutfield(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_OUTFIELD, 100);

        return value;
    }
    private int getThrow(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_THROW, 100);

        return value;
    }
    private int getHit(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_HITTING, 100);

        return value;
    }

}
