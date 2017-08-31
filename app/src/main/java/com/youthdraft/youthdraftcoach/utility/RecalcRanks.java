package com.youthdraft.youthdraftcoach.utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.datamodel.RankContentProvider;
import com.youthdraft.youthdraftcoach.datamodel.RankDB;

import java.util.Calendar;

/**
 * Created by marty331 on 4/21/16.
 */
public class RecalcRanks {

    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_FRAG = "current_frag";
    public static final String PROPERTY_HEIGHT = "height_weight";
    public static final String PROPERTY_WEIGHT = "weight_weight";
    public static final String PROPERTY_SPEED = "speed_weight";
    public static final String PROPERTY_HITTING = "hitting_weight";
    public static final String PROPERTY_BAT_SPEED = "bat_speed_weight";
    public static final String PROPERTY_INFIELD= "infield_weight";
    public static final String PROPERTY_OUTFIELD = "outfield_weight";
    public static final String PROPERTY_THROW = "throwing_weight";
    public static final String PROPERTY_ARM = "arm_weight";
    public static final String PROPERTY_BASE_RUNNING = "base_running_weight";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_LEAGUE = "league";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";
    public static final String PROPERTY_COACH_SEASON = "coach_season";
    public static final String PROPERTY_COACH_YEAR = "coach_year";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static String LOG_TAG = "WeightView";
    String sport, season, years;
    Cursor cursor;
    Firebase firebase, fireup, fireweights;
    Context mContext;

    public void recalcPlayers(Context context){

        this.mContext = context.getApplicationContext();
        String[] ranks = {RankDB.KEY_ROWID, RankDB.KEY_SPEED, RankDB.KEY_HITTING,RankDB.KEY_THROWING, RankDB.KEY_ARM_STRENGTH,
                RankDB.KEY_RATING, RankDB.KEY_HEIGHT, RankDB.KEY_WEIGHT, RankDB.KEY_BAT_SPEED, RankDB.KEY_INFIELD, RankDB.KEY_OUTFIELD, RankDB.KEY_BASE_RUNNING };
        final double hei_weight = getHeight(mContext.getApplicationContext()) / 100;
        Log.d(LOG_TAG, "hei_weight ="+hei_weight);
        final double weight_weight = getWeight(mContext.getApplicationContext())/100;
        final double hit_weight = getHit(mContext.getApplicationContext())/100;
        final double bat_weight = getBatSpeed(mContext.getApplicationContext())/100;
        final double infield_weight = getInfield(mContext.getApplicationContext())/100;
        final double outfield_weight = getOutfield(mContext.getApplicationContext())/100;
        final double throw_weight = getThrow(mContext.getApplicationContext()) / 100;
        final double arms_weight = getArmstrength(mContext.getApplicationContext()) / 100;
        final double sped_weight = getSpeed(mContext.getApplicationContext()) / 100;
        final double base_run_weight = getBaseRun(mContext.getApplicationContext())/100;

        sport = getPropertyCoachSport(context.getApplicationContext());
        season = getPropertyCoachSeason(context.getApplicationContext());
        years = getPropertyCoachYear(context.getApplicationContext());


        cursor = mContext.getApplicationContext().getContentResolver().query(RankContentProvider.CONTENT_URI, ranks, null, null, RankDB.KEY_RATING+ " DESC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Log.d(LOG_TAG, "cursor ="+cursor.getColumnNames().length);

            int hei = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_HEIGHT));
            int wei = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_WEIGHT));
            int hitt = cursor.getInt(cursor.getColumnIndex("hitting"));
            int bat = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_BAT_SPEED));
            int inf = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_INFIELD));
            int outf = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_OUTFIELD));
            int throwing = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_THROWING));
            int arms = cursor.getInt(cursor.getColumnIndex("arm_strength"));
            int sped = cursor.getInt(cursor.getColumnIndex("speed"));
            //Log.d(LOG_TAG, "baserun ="+cursor.getInt(cursor.getColumnIndex("base_running")));
            int baserun = cursor.getInt(cursor.getColumnIndex("base_running"));


            double ranked = 0.0;
            ranked = (hei_weight * hei)+ (wei*weight_weight) + (hit_weight * hitt) + (bat_weight*bat)+(inf * infield_weight)+ (outf *outfield_weight) +
                    (throw_weight * throwing) + (arms*arms_weight) +(sped_weight * sped) + (0 * base_run_weight);


            ContentValues valuesin = new ContentValues();
            valuesin.put(RankDB.KEY_RATING, ranked);
            try {

                int row = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_ROWID));
                String id = Integer.toString(row);
                Uri uri = Uri.parse(RankContentProvider.CONTENT_URI + "/" + id);
                mContext.getApplicationContext().getContentResolver().update(uri, valuesin, null, null);

            } catch (Exception e) {
                Log.d(LOG_TAG, "exception updating friend:" + e);
            }
            cursor.moveToNext();
        }
        cursor.close();
        final String accountuid = getUid(mContext.getApplicationContext());
        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " players");

                System.out.println("value =" + dataSnapshot.getValue());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Log.d(LOG_TAG, "datas =" + postSnapshot.getValue().toString());
                    PlayerScores playerScores = postSnapshot.getValue(PlayerScores.class);
                    String playid = playerScores.getPlayerid();
                    Log.d(LOG_TAG, "playerid ="+playid);
                    String first = playerScores.getFirstname();
                    String last = playerScores.getLastname();
                    String mid = playerScores.getMidinitial();
                    String birth = playerScores.getBirth();
                    int jer = playerScores.getJersey();

                    int pheight = playerScores.getPheight();
                    int pweigth = playerScores.getPweight();
                    int phit = playerScores.getPhit();
                    int pbat = playerScores.getPbat();
                    int pinfield = playerScores.getPinfield();
                    int poutfield = playerScores.getPoutfield();
                    int pthrow = playerScores.getPthrow();
                    int parms = playerScores.getParm();
                    int pspeed = playerScores.getPspeed();
                    int pbase = playerScores.getPbase();

                    String pdraft = playerScores.getPdraft();
                    String pnote = playerScores.getPnote();
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
                    double ranked = 0.0;
                    ranked = (pheight*hei_weight)+(pweigth*weight_weight) + (hit_weight * phit) + (bat_weight * pbat)+ (infield_weight * pinfield)+
                            (outfield_weight * poutfield) + (throw_weight * pthrow) + (parms*arms_weight) + (sped_weight * pspeed) + (base_run_weight * pbase);
                    double rawscore = pheight + pweigth + phit + pbat + pinfield + poutfield + pthrow + parms + pspeed + pbase;
                    //fireup = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"sportid+"/"+seasonid+"/"+playid);
                    //float rranked = (float)ranked;
                    //float rrawscore = (float)rawscore;
                    fireup = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                    PlayerScores playerup = new PlayerScores(playid, first, last, mid, birth, jer, pheight, pweigth, phit, pbat, pinfield, poutfield, pthrow, parms,
                            pspeed, pbase, pdraft, pnote, dateval, ranked, false);//, rawscore);
                    fireup.setValue(playerup);

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        Toast.makeText(mContext.getApplicationContext(), "Rankings recalculated", Toast.LENGTH_SHORT).show();
    }

    private int getHeight(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_HEIGHT, 100);

        return value;
    }

    private int getWeight(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_WEIGHT, 100);

        return value;
    }
    private int getHit(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_HITTING, 100);

        return value;
    }
    private int getBatSpeed(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_BAT_SPEED, 100);

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
    private int getArmstrength(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_ARM, 100);

        return value;
    }
    private int getSpeed(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_SPEED, 100);

        return value;
    }
    private int getBaseRun(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_BASE_RUNNING, 100);

        return value;
    }
    private String getUid(Context context){
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_UID, null);

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
