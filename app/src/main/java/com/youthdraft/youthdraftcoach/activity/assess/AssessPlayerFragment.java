package com.youthdraft.youthdraftcoach.activity.assess;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

//import com.appyvet.rangebar.RangeBar;

/**
 * Created by marty331 on 1/22/16.
 */
public class AssessPlayerFragment extends Fragment  {
    public static String LOG_TAG = "AssessPlayerFragment";


    //RangeBar heightp;
    SeekBar heightp;
    SeekBar weightp, hittingp, batp, infieldp, outfieldp, throwp, armp, speedp, basep;
    EditText playerNotes;
    Button saveButton;
    Spinner playerSpinner, timeSlot;
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

    public AssessPlayerFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_view, container, false);
        playId = null; //(TextView) view.findViewById(R.id.tvID);
        playerSpinner = (Spinner) view.findViewById(R.id.sPlayerName);
        timeSlot = (Spinner) view.findViewById(R.id.sTimeSlot);
        birthdayval = (TextView) view.findViewById(R.id.tvBirthday);
        heightp = (SeekBar) view.findViewById(R.id.sHeight);
        weightp = (SeekBar) view.findViewById(R.id.sWeight);
        hittingp = (SeekBar) view.findViewById(R.id.sHitMech);
        batp = (SeekBar) view.findViewById(R.id.sBatSpeed);
        infieldp = (SeekBar) view.findViewById(R.id.sInfield);
        outfieldp = (SeekBar) view.findViewById(R.id.sOutfield);
        throwp = (SeekBar) view.findViewById(R.id.sThrowing);
        armp = (SeekBar) view.findViewById(R.id.sArm);
        speedp = (SeekBar) view.findViewById(R.id.sSpeed);
        basep = (SeekBar) view.findViewById(R.id.sBase);

        draft = (CheckBox) view.findViewById(R.id.cbDraft);
        playerNotes = (EditText) view.findViewById(R.id.etNote);
        saveButton = (Button) view.findViewById(R.id.bSavePlayer);

        heightpVal = (TextView) view.findViewById(R.id.tvHeightVal);
        weightpVal = (TextView) view.findViewById(R.id.tvWeightVal);
        hittingVal= (TextView) view.findViewById(R.id.tvHitMecVal);
        batVal = (TextView) view.findViewById(R.id.tvBatSpeedVal);
        infieldVal = (TextView) view.findViewById(R.id.tvInfieldVal);
        outfieldVal = (TextView) view.findViewById(R.id.tvOutfieldVal);
        throwingVal= (TextView) view.findViewById(R.id.tvThrowingVal);
        armVal = (TextView) view.findViewById(R.id.tvArmVal);
        speedVal = (TextView) view.findViewById(R.id.tvSpeedVal);
        baseVal = (TextView) view.findViewById(R.id.tvBaseVal);

        act = (LinearLayout) view.findViewById(R.id.activityassess);
        play = (LinearLayout) view.findViewById(R.id.playerassess);
        play.setVisibility(View.VISIBLE);
        activityList.clear();

        playMap = new HashMap<String, PlayerInfo>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Players....");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            Log.d(LOG_TAG, "onCreate NULL");
        } else {
            Log.d(LOG_TAG, "onCreate not NULL");
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        Firebase.setAndroidContext(getContext());
        accountuid = getUid(getContext());
        leagueid = getLeague(getContext());
        sport = getPropertyCoachSport(getContext());
        season = getPropertyCoachSeason(getContext());
        coachYear = getPropertyCoachYear(getContext());
        progressDialog.show();

        loadUi();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(LOG_TAG, "onSaveInstanceState");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "onViewCreated");
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



    public void loadUi(){


        // Create an ArrayAdapter using the string array and a default spinner layout
        //final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.players_array, android.R.layout.simple_spinner_item);
        final ArrayList<String> spinnerArray = new ArrayList<String>();
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, playerNames);
        final ArrayList<String> timeArray = new ArrayList<>();
        final ArrayAdapter<String> timeArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, timelist);

        // Specify the layout to use when the list of choices appears
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d(LOG_TAG, "account ="+accountuid+" leagueid ="+leagueid+" sport="+sport+" season="+season+" coachyear="+coachYear);
        firebaseplayers = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid +"/ranked/"+sport+"/"+season+coachYear);

        firebaseplayers.orderByChild("prank").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot playerScoresShot : dataSnapshot.getChildren()){
                    PlayerScores ps = playerScoresShot.getValue(PlayerScores.class);
                    Log.d(LOG_TAG, "ps ="+ps.getFullname());
                    playerScoresList.add(ps);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Use Firebase to populate the list.
        Log.d(LOG_TAG, "leagueid ="+leagueid+" sport="+sport+" season="+season+" coachYear="+coachYear);
        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/league/id/"+leagueid+"/sport/"+sport+"/"+season+coachYear+"/players/");///kys/sport/baseball/players/mcb123
        firebase.orderByChild("tryout_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnap : dataSnapshot.getChildren()){
                    PlayerInfo playerInfo = postsnap.getValue(PlayerInfo.class);
                    playerInfos.add(playerInfo);
                    Log.d(LOG_TAG, "playerInfos playerid ="+playerInfo.getLeagueid());
                    String dt = playerInfo.getDateValues();
                    if (timelist.contains(dt) == false){
                        timelist.add(dt);
                        timeCheck.add(playerInfo.getDateValues());
                    }
                    timeArrayAdapter.notifyDataSetChanged();
                }
                for (int i = 0; i < timelist.size(); i++){
                    ArrayList<String> listPlayers = new ArrayList<String>();
                    for (DataSnapshot timeSnap : dataSnapshot.getChildren()){
                        PlayerInfo playerPick = timeSnap.getValue(PlayerInfo.class);
                        //Log.d(LOG_TAG, "playerPick date values ="+playerPick.getDateValues());
                        //Log.d(LOG_TAG, "timelist ="+timelist.get(i));
                        if (playerPick.getDateValues().equals(timelist.get(i))){
                            listPlayers.add(playerPick.getFullname());
                        }
                        //Log.d(LOG_TAG, "listPlayers ="+listPlayers.size());
                        playerList.put(timelist.get(i), listPlayers);
                    }
                }

                timeSlot.setAdapter(timeArrayAdapter);
                timeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String datetime = timeSlot.getSelectedItem().toString();
                        //Log.d(LOG_TAG, "datetime =" + datetime);
                        String[] dt = datetime.split(" ");
                        String playday = dt[0];
                        String playtime = dt[1] + " " + dt[2];
                        //Log.d(LOG_TAG, "playday =" + playday + " playtime =" + playtime);


                        Log.d(LOG_TAG, "playday ="+playday+" playtime ="+playtime);
                        //spinnerArray.clear();
                        playerNames.clear();
                        playerNameID.clear();


                        playerSpinner.setAdapter(null);
                        for (int i = 0; i < playerInfos.size(); i++){
                            if (playerInfos.get(i).getDateValues().equals(datetime)){

                                playerNames.add(playerInfos.get(i).getFullname().trim());
                                playerNameID.put(playerInfos.get(i).getFullname().trim(), playerInfos.get(i).getLeagueid());
                                Log.d(LOG_TAG, "playerInfos name ="+playerInfos.get(i).getFullname().trim()+" playerInfos id="+playerInfos.get(i).getLeagueid());
                                Log.d(LOG_TAG, "playerNameID ="+playerNameID.keySet().toString());
                                Log.d(LOG_TAG, "playerNameID count ="+playerNameID.size());
                            }


                        }

                        spinnerArrayAdapter.notifyDataSetChanged();
                        playerSpinner.setAdapter(spinnerArrayAdapter);
                        playerSpinner.setSelection(0);
                        //Log.d(LOG_TAG, "playerSpinner count ="+playerSpinner.getAdapter().getCount());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                // Apply the adapter to the spinner
                playerSpinner.setAdapter(spinnerArrayAdapter);

                playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(LOG_TAG, "playerSpinner =" + playerSpinner.getSelectedItemPosition());
                        int selectedNum = playerSpinner.getSelectedItemPosition();
                        String selectedName = playerSpinner.getSelectedItem().toString();
                        //String[] splited = play.split("\\s+");
                        Log.d(LOG_TAG, "selectedNum =" + selectedNum);
                        Log.d(LOG_TAG, "selectedString =" + selectedName.trim());
                        //try{
                        Log.d(LOG_TAG, "player ="+playerNameID.get(selectedName));
                        boolean playerFound = false;

                        for (int i = 0; i < playerScoresList.size(); i++){
                            if (playerScoresList.get(i).getFullname().equals(selectedName)){
                                playerFound = true;
                                playerScoresPosition = i;
                                Log.d(LOG_TAG, "playerscoresposition ="+playerScoresPosition);
                                Log.d(LOG_TAG, "playerScoresList ="+playerScoresList.get(i).getFullname());
                                playerid = playerScoresList.get(i).getPlayerid();
                                first = playerScoresList.get(i).getFirstname().replaceAll("\\s+","");
                                last = playerScoresList.get(i).getLastname().replaceAll("\\s+","");
                                midInitial = playerScoresList.get(i).getMidinitial();
                                birthdayval.setText(playerScoresList.get(i).getBirth());
                                jer = playerScoresList.get(i).getJersey();
                                playId.setText(String.valueOf(jer));
                                heightp.setProgress(playerScoresList.get(i).getPheight()); //setTickStart(playerScoresList.get(i).getPheight());
                                weightp.setProgress(playerScoresList.get(i).getPweight());
                                hittingp.setProgress(playerScoresList.get(i).getPhit());
                                batp.setProgress(playerScoresList.get(i).getPbat());
                                infieldp.setProgress(playerScoresList.get(i).getPinfield());
                                outfieldp.setProgress(playerScoresList.get(i).getPoutfield());
                                throwp.setProgress(playerScoresList.get(i).getPthrow());
                                armp.setProgress(playerScoresList.get(i).getParm());
                                speedp.setProgress(playerScoresList.get(i).getPspeed());
                                basep.setProgress(playerScoresList.get(i).getPbase());

                                String drafted = playerScoresList.get(i).getPdraft();
                                //Log.d(LOG_TAG, "drafted ="+drafted);
                                //Log.d(LOG_TAG, "draft test ="+drafted.equals("No"));
                                if (drafted.equals("No")) {
                                    draft.setChecked(true);
                                } else {
                                    draft.setChecked(false);
                                }
                                playerNotes.setText(playerScoresList.get(i).getPnote());
                            }
                        }
                        if (!playerFound){
                            Log.d(LOG_TAG, "selectedName ="+selectedName);
                            for (int p = 0; p < playerInfos.size(); p++){
                                //Log.d(LOG_TAG, "player not found, checking");
                                //Log.d(LOG_TAG, "playerInfos fullname ="+playerInfos.get(p).getFullname().trim());

                                if (playerInfos.get(p).getFullname().trim().equals(selectedName)){
                                    Log.d(LOG_TAG, "player not found playerScoresList ="+playerInfos.get(p).getFullname());
                                    playerid = playerInfos.get(p).getLeagueid();
                                    first = playerInfos.get(p).getFirstname().replaceAll("\\s+","");
                                    last = playerInfos.get(p).getLastname().replaceAll("\\s+","");
                                    midInitial = playerInfos.get(p).getMiddlename();
                                    birthdayval.setText(playerInfos.get(p).getBirthday());
                                    jer = playerInfos.get(p).getJersey();
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



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**
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
         **/


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

                double height_weight = getPropertyHeight(getContext())/100;
                double weight_weight = getPropertyWeight(getContext())/100;
                double hitting_weight = getHit(getContext())/100;
                double batspeed_weight = getPropertyBatSpeed(getContext())/100;
                double infield_weight = getInfield(getContext())/100;
                double outfield_weight = getOutfield(getContext()) /100;
                double throw_weight = getThrow(getContext())/100;
                double arm_weight = getPropertyArm(getContext())/100;
                double speed_weight = getPropertySpeed(getContext())/100;
                double base_weight = getPropertyBase(getContext())/100;


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
                String accountuid = getUid(getContext());
                double rawscore =  hett + wett + hitt + batt + infl + outfl + thr + arms + sped + bse;
                float rranked = (float)ranked;
                float rrawscore  = (float)rawscore;

                PlayerScores playerScores = new PlayerScores(playid, first.trim(), last.trim(), midInitial, bir,  jer, hett, wett,  hitt, batt, infl, outfl, thr, arms, sped, bse, draftable, notes, dateval, ranked, false);
                firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid +"/ranked/"+sport+"/"+season+coachYear+"/"+playid);

                firebase.setValue(playerScores, - playerScores.getPrank());
                Log.d(LOG_TAG, "playid ="+playid);
                Log.d(LOG_TAG, "save fullname ="+first+" "+last);
                if (playerScoresPosition < -1){
                    playerScoresList.add(playerScoresPosition, new PlayerScores(playid, first, last, midInitial, bir, jer, hett, wett, hitt, batt, outfl, infl, thr, arms, sped, bse, draftable, notes, dateval, ranked, false));
                }

            }
        });

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

    public void setFragVal(int val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_FRAG, val);
        editor.apply();
    }

    private int getCategoryPos(String category) {
        return playerScoresList.indexOf(category);
    }

}