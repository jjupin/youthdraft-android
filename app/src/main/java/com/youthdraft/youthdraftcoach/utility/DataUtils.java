package com.youthdraft.youthdraftcoach.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.activity.assess.AssessPlayerActivity;
import com.youthdraft.youthdraftcoach.activity.players.PlayersFragment;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.interfaces.DataUtilsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.youthdraft.youthdraftcoach.activity.players.AssessedPlayersFragment.TAG;

/**
 * Created by jjupin on 1/6/17.
 */

public class DataUtils {

    private static final String LOG_TAG = "DataUtils";

    private static DataUtils instance;

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

    public static Firebase firebase, firebaseplayers;

    public static List<DataUtilsListener> listeners = new ArrayList<DataUtilsListener>();
    public static String accountuid;
    public static String leagueid;
    public static String sport;
    public static String season;
    public static String coachYear;

    public static DataUtils getInstance() {
        if (instance == null) {
            instance = new DataUtils();
        }

        return instance;
    }

    public static void addDataUtilsListener(DataUtilsListener listener, Activity activity) {
        listeners.add(listener);
    }

    public static void savePlayerInfoData(PlayerInfo player, Context context) {

        int position = PlayerManager.getPlayerDBPosiiton(player);
        if (position < 0) {
            position = PlayerManager.getHighestDBPosition() + 1;  // need to go past the highest one...
            PlayerManager.addPlayer(player, position, accountuid);
        }

        PlayerInfo strippedPlayer = removeStatsFromPlayerInfo(player);

        // "league/id/" + leagueid + "/sport/" +sport+"/"+season+coachYear+"/players/"+
        // String firebaseUrl = "https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid +"/ranked/"+sport+"/"+season+coachYear+"/"+playerid;

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                PlayerInfo player = dataSnapshot.getValue(PlayerInfo.class);
                // ...
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.w(TAG, "loadPost:onCancelled", firebaseError.toException());
            }
        };

        String firebaseURL =  "https://blistering-fire-5846.firebaseio.com/league/id/" + leagueid + "/sport/" + sport + "/" + season + coachYear + "/players/" + (position+"");
        Firebase firebase = new Firebase(firebaseURL);
        firebase.addValueEventListener(postListener);
        firebase.setValue(strippedPlayer);

        //firebase.setValue(playerScores, - playerScores.getPrank());

    }

    private static PlayerInfo removeStatsFromPlayerInfo(PlayerInfo player) {
        PlayerInfo stripped = new PlayerInfo();

        stripped.setBirthday(player.getBirthday());
        stripped.setPlayerid(player.getPlayerid());
        stripped.setFirstname(player.getFirstname());
        stripped.setMiddlename(player.getMiddlename());
        stripped.setLastname(player.getLastname());
        stripped.setTryout_date(player.getTryout_date());
        stripped.setTryout_time(player.getTryout_time());
        stripped.setJersey(player.getJersey());
        stripped.setPriorlevel(player.getPriorlevel());
        stripped.setPriorteam(player.getPriorteam());
        stripped.setDrafted(player.isDrafted());
        stripped.setDraftedTeam(player.getDraftedTeam());
        stripped.setLeagueage(player.getLeagueage());
        stripped.setLeagueid(player.getLeagueid());
        stripped.setBib(player.getBib());
        stripped.setGender(player.getGender());
        stripped.setSport(player.getSport());

        stripped.setCoachsKid(player.isCoachsKid());
        stripped.setReschedule(player.isReschedule());

        return stripped;
    }

    public static PlayerInfo copyPlayerInfoIntoAnotherPlayerInfo(PlayerInfo newPlayer, PlayerInfo oldPlayer) {
        oldPlayer.setTryout_date(newPlayer.getTryout_date());
        oldPlayer.setTryout_time(newPlayer.getTryout_time());
        oldPlayer.setLeagueid(newPlayer.getLeagueid());
        oldPlayer.setLeagueage(newPlayer.getLeagueage());
        oldPlayer.setLastname(newPlayer.getLastname());
        oldPlayer.setFirstname(newPlayer.getFirstname());
        oldPlayer.setMiddlename(newPlayer.getMiddlename());
        oldPlayer.setBib(newPlayer.getBib());
        oldPlayer.setCoachsKid(newPlayer.isCoachsKid());
        oldPlayer.setReschedule(newPlayer.isReschedule());
        oldPlayer.setDrafted(newPlayer.isDrafted());
        oldPlayer.setDraftedTeam(newPlayer.getDraftedTeam());

        return oldPlayer;
    }

    public static void savePlayerAssessmentScores(PlayerInfo player, Context context) {

        String accountuid = getUid(context);
        String leagueid = getLeague(context);
        String sport = getPropertyCoachSport(context);
        String season = getPropertyCoachSeason(context);
        String coachYear = getPropertyCoachYear(context);

        String playerid = player.getLeagueid();

        int wett = player.getWeight();
        int hett = player.getHeight();
        int hitt = player.getHitting();
        int batt = player.getBat();
        int infl = player.getInfield();
        int outfl = player.getOutfield();
        int thr = player.getThrowing();
        int arms = player.getArm();
        int sped = player.getSpeed();
        int bse = player.getBase();
        Log.d(LOG_TAG, "height ="+hett+" weight ="+wett+" hitting ="+hitt+" batting ="+batt+" infield ="+infl+" outfield ="+outfl+" throwing ="+thr+" arm ="+arms+" speed ="+sped+" base ="+bse);


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



        double ranked = 0.0;  // weighting is turned off for now...
        /**
        ranked = (height_weight * hett) + (weight_weight * wett) + (hitting_weight * hitt) + (batspeed_weight * batt) +
                (infield_weight * infl) + (outfield_weight * outfl) + (throw_weight * thr) + (arm_weight * arms) +
                (base_weight * bse) + (speed_weight * sped);
         **/
        double rawscore = hett + wett + hitt + batt + infl + outfl + thr + arms + bse + sped;
        ranked = rawscore;

        float rranked = (float)ranked;
        float rrawscore  = (float)rawscore;

        PlayerScores playerScores = new PlayerScores();
        if (PlayerManager.getCurrentSwapScoresForPlayer(player) != null) {
            playerScores = PlayerManager.getCurrentSwapScoresForPlayer(player);
        }

        playerScores = generatePlayerScoresFromPlayerInfo(player, playerScores);
        PlayerManager.swapInPlayerScoresForPlayerInfo(player, playerScores);

        //PlayerScores playerScores = new PlayerScores(playid, first.trim(), last.trim(), midInitial, bir,  jer, hett, wett,  hitt, batt, infl, outfl, thr, arms, sped, bse, draftable, notes, dateval, ranked);
        String firebaseUrl = "https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid +"/ranked/"+sport+"/"+season+coachYear+"/"+playerid;
        Firebase firebase = new Firebase(firebaseUrl);
        firebase.setValue(playerScores, - playerScores.getPrank());

        // I assume it's gone up - need to put code here to catch a returned errors...

        PlayerManager.addPlayerScore(playerScores, getUid(context));

        Toast.makeText(context, "Finshed storing scores for " + player.getFullname() + ".", Toast.LENGTH_SHORT).show();
        //AssessPlayerActivity.this.setResult(Activity.RESULT_OK);
        //AssessPlayerActivity.this.finish();

        /**
         Log.d(LOG_TAG, "playid ="+playid);
         Log.d(LOG_TAG, "save fullname ="+first+" "+last);
         if (playerScoresPosition < -1){
         playerScoresList.add(playerScoresPosition, new PlayerScores(playid, first, last, midInitial, bir, jer, hett, wett, hitt, batt, outfl, infl, thr, arms, sped, bse, draftable, notes, dateval, ranked));
         }
         **/
    }

    private static PlayerScores generatePlayerScoresFromPlayerInfo(PlayerInfo player, PlayerScores scores) {

        scores.setPlayerid(player.getLeagueid());
        scores.setFirstname(player.getFirstname());
        scores.setLastname(player.getLastname());
        if (player.getMiddlename() != null && player.getMiddlename().length() > 0) {
            scores.setMidinitial(player.getMiddlename().substring(0,1));
        }

        scores.setBirth(player.getBirthday());
        scores.setJersey(player.getJersey());

        scores.setCatcher(player.isCatcher());
        scores.setPitcher(player.isPitcher());
        scores.setCompletedAssessment(player.hasCompletedAssessment());

        //        this.pweight = pweight;

        scores.setPhit(player.getHitting());
        scores.setPbat(player.getBat());
        scores.setPoutfield(player.getOutfield());
        scores.setPinfield(player.getInfield());
        scores.setPthrow(player.getThrowing());
        scores.setParm(player.getArm());
        scores.setPspeed(player.getSpeed());
        scores.setPbase(player.getBase());
        scores.setPdraft(player.isCoachsKid() ? "Not Draftable" : "Draftable");
        scores.setPitcher(player.isPitcher());
        scores.setCatcher(player.isCatcher());

        scores.setCustom01(player.getCustom01());
        scores.setCustom02(player.getCustom02());
        scores.setCustom03(player.getCustom03());
        scores.setCustom04(player.getCustom04());
        scores.setCustom05(player.getCustom05());

        scores.setCustomNote01(player.getCustomNote01());
        scores.setCustomNote02(player.getCustomNote02());
        scores.setCustomNote03(player.getCustomNote03());
        scores.setCustomNote04(player.getCustomNote04());
        scores.setCustomNote05(player.getCustomNote05());


        scores.setPnoteHit(player.getNoteHitting());
        scores.setPnoteBat(player.getNoteBat());
        scores.setPnoteInfield(player.getNoteInfield());
        scores.setPnoteOutfield(player.getNoteOutfield());
        scores.setPnoteThrow(player.getNoteThrowing());
        scores.setPnoteArm(player.getNoteArm());
        scores.setPnoteSpeed(player.getNoteSpeed());
        scores.setPnoteBase(player.getNoteBase());

        /**

        this.poutfield = poutfield;
        this.pinfield = pinfield;
        this.pthrow = pthrow;
        this.parm = parm;
        this.pspeed = pspeed;
        this.pbase = pbase;
        this.pdraft = pdraft;
        this.pnote = pnote;
        this.pdate = pdate;
        this.prank = prank;
        this.completedAssessment = assessmentCompleted;

         **/
        return scores;
    }

    public static void loadDataFromFirebase(Activity activity) {

            accountuid = PreferencesUtils.getUid(activity.getBaseContext());
            leagueid = PreferencesUtils.getLeague(activity.getBaseContext());
            sport = PreferencesUtils.getPropertyCoachSport(activity.getBaseContext());
            season = PreferencesUtils.getPropertyCoachSeason(activity.getBaseContext());
            coachYear = PreferencesUtils.getPropertyCoachYear(activity.getBaseContext());

        Firebase.setAndroidContext(activity.getBaseContext());
        getInstance().getData();
    }

    public static void clearFirebaseConnections() {
        firebase = null;
        firebaseplayers = null;
    }

    public void getData() {
        new GetScores().execute();
    }

    //
    // some generic utils to manipulate app preference items...
    //

    public static String getAccount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_ACCOUNT, null);

        return value;
    }


    public static String getUid(Context context){
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_UID, null);

        return value;
    }

    public static String getLeague(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_LEAGUE, null);

        return value;
    }

    public static String getPropertyCoachSport(Context context){
        final SharedPreferences preferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = preferences.getString(PROPERTY_COACH_SPORT, null);

        return value;
    }
    public static String getPropertyCoachSeason(Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(PROPERTY_COACH_SEASON, null);
        return value;
    }

    public static String getPropertyCoachYear(Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(PROPERTY_COACH_YEAR, null);
        return value;
    }

    public static int getPropertyArm(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_ARM, 100);

        return value;
    }

    public static int getPropertyBase(Context context) {
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


    //
    // Async tasks for grabbing the data
    //

    //
    // Note:  I hated to do this, but maybe somebody can correct me on what was going on - but for some
    //        odd reason, Java was getting confused on the return types of the asyntasks such that with
    //        two of them running, if their generic params matched, it was returning to the "onPostExecute"
    //        in the wrong task - ie, for GetScores, it was calling the "onPostExecute" of the GetPlayers.
    //

    private class GetScores extends AsyncTask<Void, Void, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PlayerManager.clearCurrentData();  // start everything afresh...
        }

        @Override
        protected Long doInBackground(Void... params) {
                String urlStr = "https://blistering-fire-5846.firebaseio.com/users/" + accountuid + "/" + leagueid + "/ranked/" + sport + "/" + season + coachYear;
                firebaseplayers = new Firebase(urlStr);
                firebaseplayers.orderByChild("prank").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot playerScoresShot : dataSnapshot.getChildren()) {
                            PlayerScores ps = playerScoresShot.getValue(PlayerScores.class);
                            Log.d(TAG, "ps =" + ps.getFullname());
                            //playerScoresList.add(ps);
                            //playerScoresMap.put(ps.getPlayerid(), ps);
                            PlayerManager.addPlayerScore(ps, accountuid);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e(TAG, "firebase retrieval of scores failed.");
                    }
                });

            return 0L;
        }

        @Override
        protected void onPostExecute(Long bogusLong) {

            Log.e(TAG, "got our scores - let's go get the players now...");
            new GetPlayers().execute();

        }
    }

    private class GetPlayers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (PlayerManager.getAllPlayers().size() <= 0) {

                Log.d(TAG, "leagueid =" + leagueid + " sport=" + sport + " season=" + season + " coachYear=" + coachYear);
                    firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/league/id/" + leagueid + "/sport/" + sport + "/" + season + coachYear + "/players/");///kys/sport/baseball/players/mcb123
                    firebase.orderByChild("lastname").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //PlayerManager.clearCurrentData();  // need to remove this and do an incremental add (when I get the time.)...
                            PlayerManager.clearTimeSlots();

                            Random rn = new Random();

                            for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                                PlayerInfo playerInfo = postsnap.getValue(PlayerInfo.class);
                                String positionStr = postsnap.getKey();

                                PlayerManager.addPlayer(playerInfo, Integer.parseInt(positionStr), accountuid);  // making a local copy for this fragment...
                                Log.d(TAG, "playerInfo playerid =" + playerInfo.getLeagueid());
                                String dt = playerInfo.getDateValues();

                                if (PlayerManager.getPlayerScoresForCoachId(accountuid) != null) {
                                    PlayerManager.swapInPlayerScoresForPlayerInfo(playerInfo, PlayerManager.getPlayerScoresForCoachId(accountuid).get(playerInfo.getLeagueid()));
                                }

                                //if (playerScoresMap.containsKey(playerInfo.getLeagueid())) {
                                //    PlayerManager.swapInPlayerScoresForPlayerInfo(playerInfo, playerScoresMap.get(playerInfo.getLeagueid()));
                                //}
                            }

                            for (DataUtilsListener listener : listeners) {
                                listener.cleanUpPlayersAndScoresAfterDownloadCompletes();
                            }
                        }


                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.e(TAG, "firebase retrieval of players failed.");
                        }
                    });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.e(TAG, "got our players...");

            //Toast.makeText(getActivity().getBaseContext(), "Finished retrieval all players....", Toast.LENGTH_SHORT).show();
        }
    }
}
