package com.youthdraft.youthdraftcoach.activity.assess;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.datamodel.PlayerDBHelper;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.adapter.RVAdapter;
import com.youthdraft.youthdraftcoach.datamodel.RankDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AssessFragment extends Fragment {

    public static String LOG_TAG = "AssessFragment";

    Spinner actSpinner, actTime;

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

    RankDBHelper mHelper;
    Firebase firebase, firebaseActivities, firebaseplayers;
    String accountuid, leagueid, sport, season, year;
    PlayerDBHelper playerDBHelper;
    LinearLayout act, scrollin;
    List<String> activityList = new ArrayList<String>();
    ScrollView scrollView;
    String actval;
    RecyclerView rv;
    private List<PlayerInfo> playerInfos;
    List<PlayerInfo> actPlayers;
    List<PlayerScores> playerScores;
    List<PlayerScores> assessPlayers;
    HashMap<String, PlayerScores> assessHash = new HashMap<>();
    ProgressDialog progressDialog;


    public AssessFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view, container, false);


        scrollView = (ScrollView) view.findViewById(R.id.scrollactivity);

        scrollin = (LinearLayout) view.findViewById(R.id.scrolllin);

        actSpinner = (Spinner) view.findViewById(R.id.sActivity);
        actTime = (Spinner) view.findViewById(R.id.sAssessDate);


        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        act = (LinearLayout) view.findViewById(R.id.activityassess);
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
        playerInfos = new ArrayList<>();
        playerScores = new ArrayList<>();
        assessPlayers = new ArrayList<>();
        actPlayers = new ArrayList<>();
    }

    public void getActivities(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, activityList);
        firebaseActivities = new Firebase("https://blistering-fire-5846.firebaseio.com/league/id/"+leagueid+"/sport/"+sport+"/activities");
        firebaseActivities.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(LOG_TAG, "activities ="+dataSnapshot.getChildren().toString());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    activityList.clear();
                    Log.d(LOG_TAG, "activity ="+dataSnapshot1.child("activity").getValue());
                    //String al = dataSnapshot1.child("activity").getValue().toString();
                    for (DataSnapshot postsnap : dataSnapshot.getChildren()){
                        com.youthdraft.youthdraftcoach.ActivityInfo activityInfo1 = postsnap.getValue(com.youthdraft.youthdraftcoach.ActivityInfo.class);
                        activityList.add(activityInfo1.getActivity());
                    }
                    adapter.notifyDataSetChanged();

                    //activityList.add(al);
                    //actcount++;
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                actSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
        Log.d(LOG_TAG, "onResume leagueid ="+leagueid);
        sport = getPropertyCoachSport(getContext());
        season = getPropertyCoachSeason(getContext());
        year = getPropertyCoachYear(getContext());
        playerDBHelper = new PlayerDBHelper(getContext());
        progressDialog.show();
        getActivities();

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

    public void loadUi(){
        mHelper = new RankDBHelper(getContext());
        mHelper.getWritableDatabase();

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayList<String> spinnerArray = new ArrayList<String>();
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        final ArrayList<String> timeArray = new ArrayList<>();
        final ArrayAdapter<String> timeArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, timeArray);

        // Specify the layout to use when the list of choices appears
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Get PlayerScores for Firebase
        Log.d(LOG_TAG, "season ="+season);
        Log.d(LOG_TAG, "year ="+year);
        firebaseplayers = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid +"/ranked/"+sport+"/"+season+year);
        firebaseplayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot playerScoresShot : dataSnapshot.getChildren()){
                    PlayerScores ps = playerScoresShot.getValue(PlayerScores.class);
                    playerScores.add(ps);
                    assessHash.put(ps.getPlayerid(), ps);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        // Use Firebase to populate the list.
        Log.d(LOG_TAG, "leagueid ="+leagueid+" sport="+sport+" season ="+season+" year="+year);
        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/league/id/"+leagueid+"/sport/"+sport+"/"+season+year+"/players/");///kys/sport/baseball/players/mcb123

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("There are " + dataSnapshot.getChildrenCount() + " players");

                System.out.println("value =" + dataSnapshot.getValue());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(LOG_TAG, "datas =" + postSnapshot.getValue().toString());
                    PlayerInfo playerInfo = postSnapshot.getValue(PlayerInfo.class);
                    /*System.out.println(playerInfo.getFirstname() + " - " + playerInfo.getLastname() + " - " + playerInfo.getMiddlename() + " - " + playerInfo.getBirthday() + " - " + playerInfo.getGender() +
                            " - " + playerInfo.getLeagueid() + " - " + playerInfo.getLeagueage() + " - " + playerInfo.getPriorteam() + " - " + playerInfo.getJersey()+" - "+playerInfo.getSport());*/

                    String daytime = playerInfo.getTryout_date() + " " + playerInfo.getTryout_time();
                    if (timeArray.contains(daytime)) {
                        Log.d(LOG_TAG, "playerinfo tryoutime in list" + daytime);
                    } else {
                        timeArray.add(daytime);
                        Log.d(LOG_TAG, "new tryouttime =" + daytime);
                        timeArrayAdapter.notifyDataSetChanged();
                    }
                    Log.d(LOG_TAG, "playerInfo =" + playerInfo.getFirstname());

                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, "firebaseError =" + firebaseError.toString());

            }
        });

        actSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(LOG_TAG, "actSpinner =" + actSpinner.getSelectedItem().toString());
                actval = actSpinner.getSelectedItem().toString();
                //Log.d(LOG_TAG, "actval =" + actval);
                playerInfos.clear();
                assessPlayers.clear();
                actPlayers.clear();
                rv.setAdapter(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set up Assess by Activity

        actTime.setAdapter(timeArrayAdapter);
        actTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scrollin.removeAllViews();
                playerInfos.clear();
                assessPlayers.clear();
                actPlayers.clear();
                Log.d(LOG_TAG, "assessPlayers size ="+assessPlayers.size());
                final String datetime = actTime.getSelectedItem().toString();
                String[] dt = datetime.split(" ");
                String ddate = dt[0];
                final String ttime = dt[1]+" "+dt[2];
//                Log.d(LOG_TAG, "actTime ="+datetime);
//                Log.d(LOG_TAG, "leagueid ="+leagueid+" sport="+sport+" season ="+season+" year="+year);
                firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/league/id/"+leagueid+"/sport/"+sport+"/"+season+year+"/players");
                Query query = firebase.orderByChild("tryout_date").equalTo(ddate);//.orderByChild("tryout_time").equalTo(ttime);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int pnum = 0;
                        for (DataSnapshot postsnap : dataSnapshot.getChildren()){
                            //Log.d(LOG_TAG, "postsnap child ="+postsnap.child("tryout_time").getValue().toString()+"| ttime ="+ttime+"|");
                            //Log.d(LOG_TAG, "postsnap test ="+postsnap.child("tryout_time").getValue().toString().equals(ttime));
                            if (postsnap.child("tryout_time").getValue().toString().equals(ttime)){
                                PlayerInfo playerInfo = postsnap.getValue(PlayerInfo.class);
                                playerInfos.add(playerInfo);

                            }
                        }
                        //Log.d(LOG_TAG, "ttime ="+datetime);
                        for (int i = 0; i < playerInfos.size(); i++){
                            if (playerInfos.get(i).getDateValues().equals(datetime)){
                                actPlayers.add(playerInfos.get(i));
                            }
                        }
                        Log.d(LOG_TAG, "actPlayers size ="+actPlayers.size());

                        for (int i = 0; i < actPlayers.size(); i++){
                            if(assessHash.containsKey(actPlayers.get(i).getLeagueid())){
                                PlayerScores p = assessHash.get(actPlayers.get(i).getLeagueid());
                                assessPlayers.add(p);
                            } else {
                                PlayerScores ps = new PlayerScores(actPlayers.get(i).getLeagueid(),actPlayers.get(i).getFirstname(), actPlayers.get(i).getLastname(), actPlayers.get(i).getMiddlename(), actPlayers.get(i).getBirthday(), actPlayers.get(i).getJersey(),0,0,0,0,0,0,0,0,0,0,"Yes","","", 0.0, false);
                                assessPlayers.add(ps);
                                //Log.d(LOG_TAG, "added ps:"+ps.getFullname());
                            }
                        }

                        Log.d(LOG_TAG, "assessPlayers size ="+assessPlayers.size());

                        RVAdapter adapter = new RVAdapter(assessPlayers);
                        adapter.setActivityName(actval, accountuid, sport, season, leagueid, year);
                        rv.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Nothing selected.", Toast.LENGTH_LONG).show();

            }
        });


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

}
