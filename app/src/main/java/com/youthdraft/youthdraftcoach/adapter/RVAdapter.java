package com.youthdraft.youthdraftcoach.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.datamodel.RankContentProvider;
import com.youthdraft.youthdraftcoach.datamodel.RankDB;
import com.youthdraft.youthdraftcoach.datamodel.RankDBHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marty331 on 1/26/16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    String actNameVal;
    TextView actVal;
    //String ac
    //SeekBar seekBar;
    String actName;
    RankDBHelper mHelper;
    Context mContext;
    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";
    public static final String PROPERTY_COACH_SEASON = "coach_season";
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
    public static final String PROPERTY_LEAGUE = "league";
    Firebase firebase;
    public static String LOG_TAG = "RVAdapter";
    String accountuid, sport, season, leagueid, years;


    public RVAdapter(Context context){
        mContext = context.getApplicationContext();
        mHelper = new RankDBHelper(mContext);
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView playerName;
        TextView playerFirst;
        TextView playerID;
        TextView activityName;
        TextView activityScore;
        SeekBar seekBar;
        Context mContext;




        PersonViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv);
            playerName = (TextView)itemView.findViewById(R.id.tvPlayerActivityName);
            playerFirst = (TextView) itemView.findViewById(R.id.tvPlayerActivityFirstName);
            playerID = (TextView)itemView.findViewById(R.id.tvPlayerID);
            activityName = (TextView)itemView.findViewById(R.id.tvPlayerActivity);
            activityScore = (TextView)itemView.findViewById(R.id.tvPlayerActivityVal);
            seekBar = (SeekBar)itemView.findViewById(R.id.sPlayerActivity);

        }
    }




    List<PlayerScores> playerInfos;

    public RVAdapter(List<PlayerScores> playerInfos){
        this.playerInfos = playerInfos;
    }
    public void setActivityName(String s, String acct, String spt, String sea, String league, String year){
        actNameVal = s;
        accountuid = acct;
        sport = spt;
        season = sea;
        leagueid = league;
        years = year;
        //Log.d(LOG_TAG, "actNameVal ="+actNameVal);

        if (actNameVal!= null && actNameVal.equals("Height")){
            //actVal.setText("0");
//            Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
//            Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pheight";
        }
        if (actNameVal!= null && actNameVal.equals("Weight")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pweight";
        }
        if (actNameVal!= null && actNameVal.equals("Hitting Mechanics")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "phit";
        }
        if (actNameVal!= null && actNameVal.equals("Bat Speed")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pbat";
        }
        if (actNameVal!= null && actNameVal.equals("Infield Mechanics")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pinfield";
        }
        if (actNameVal!= null && actNameVal.equals("Outfield Mechanics")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "poutfield";
        }
        if (actNameVal!= null && actNameVal.equals("Throwing Mechanics")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pthrow";
        }
        if (actNameVal!= null && actNameVal.equals("Arm Strength")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "parm";
        }
        if (actNameVal!= null && actNameVal.equals("Speed")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pspeed";
        }
        if (actNameVal!= null && actNameVal.equals("Base Running")){
            //actVal.setText("0");
            //Log.d(LOG_TAG, "activity ="+actNameVal+" actval ="+actVal);
            actName = "pbase";
        }

    }

    @Override
    public int getItemCount() {
        return playerInfos.size();
    }



    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_activity, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {

        holder.playerName.setText(playerInfos.get(position).getLastname()+", ");
        holder.playerFirst.setText(playerInfos.get(position).getFirstname());
        holder.playerID.setText(playerInfos.get(position).getPlayerid());
        holder.activityName.setText(actNameVal);
        holder.activityScore.setText("0");
        //Log.d(LOG_TAG, "actNameVal ="+actNameVal);
        if (actNameVal == null) {
            actNameVal = "";
        }
        if (actNameVal.equals("Height")){
            holder.seekBar.setProgress(playerInfos.get(position).getPheight());
        }
        if (actNameVal.equals("Weight")){
            holder.seekBar.setProgress(playerInfos.get(position).getPweight());
        }
        if (actNameVal.equals("Hitting Mechanics")){
            holder.seekBar.setProgress(playerInfos.get(position).getPhit());
        }
        if (actNameVal.equals("Bat Speed")){
            holder.seekBar.setProgress(playerInfos.get(position).getPbat());
        }
        if (actNameVal.equals("Infield Mechanics")){
            holder.seekBar.setProgress(playerInfos.get(position).getPinfield());
        }
        if (actNameVal.equals("Outfield Mechancis")){
            holder.seekBar.setProgress(playerInfos.get(position).getPoutfield());
        }
        if (actNameVal.equals("Throwing Mechanics")){
            holder.seekBar.setProgress(playerInfos.get(position).getPthrow());
        }
        if (actNameVal.equals("Arm Strength")){
            holder.seekBar.setProgress(playerInfos.get(position).getParm());
        }
        if (actNameVal.equals("Speed")){
            holder.seekBar.setProgress(playerInfos.get(position).getPspeed());
        }
        if (actNameVal.equals("Base Running")){
            holder.seekBar.setProgress(playerInfos.get(position).getPbase());
        }



        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //actVal.setText(String.valueOf(progress));
                if (seekBar.isShown()){


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
                    String mnt = String.valueOf(c.get(Calendar.MONTH) + 1);
                    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                    String dateval = year+"-"+mnt+"-"+day+" "+hour+":"+min+":"+seconds;

                    if (actNameVal.equals("Height")){
                        playerInfos.get(position).setPheight(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/"+leagueid+"/ranked/"+sport+"/"+season+years+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pheight", progress);
                        playerInfos.get(position).setPheight(progress);
                        //updateRank(playid, "height", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname().replaceAll("\\s+","");
                        String lastname = playerInfos.get(position).getLastname().replaceAll("\\s+","");
                        int jersey = playerInfos.get(position).getJersey();
//                        Log.d(LOG_TAG, "pheight ="+playerInfos.get(position).getPheight());
//                        Log.d(LOG_TAG, "parm ="+playerInfos.get(position).getParm());
                        String pdate = dateLook();
                        //firstname.replaceAll("\\s+","");
                        Log.d(LOG_TAG, "firstname ="+firstname);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);

                        firebase.updateChildren(scores);


                    }
                    if (actNameVal.equals("Weight")){
                        playerInfos.get(position).setPweight(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        Log.d(LOG_TAG, "player ="+playerInfos.get(position).getFirstname() +" "+playerInfos.get(position).getPweight()+" rank ="+playerInfos.get(position).getPrank());
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pweight", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
//                        scores.put("pheight", playerInfos.get(position).getHeight());
//                        scores.put("phit", playerInfos.get(position).getHitting());
//                        scores.put("pbat", playerInfos.get(position).getBat());
//                        scores.put("pinfield", playerInfos.get(position).getInfield());
//                        scores.put("poutfield", playerInfos.get(position).getOutfield());
//                        scores.put("pthrow", playerInfos.get(position).getThrowing());
//                        scores.put("parm", playerInfos.get(position).getArm());
//                        scores.put("pspeed", playerInfos.get(position).getSpeed());
//                        scores.put("pbase", playerInfos.get(position).getBase());

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Hitting Mechanics")){
                        playerInfos.get(position).setPhit(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("phit", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
//                        scores.put("pheight", playerInfos.get(position).getHeight());
//                        scores.put("pweight", playerInfos.get(position).getWeight());
//                        scores.put("pbat", playerInfos.get(position).getBat());
//                        scores.put("pinfield", playerInfos.get(position).getInfield());
//                        scores.put("poutfield", playerInfos.get(position).getOutfield());
//                        scores.put("pthrow", playerInfos.get(position).getThrowing());
//                        scores.put("parm", playerInfos.get(position).getArm());
//                        scores.put("pspeed", playerInfos.get(position).getSpeed());
//                        scores.put("pbase", playerInfos.get(position).getBase());

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Bat Speed")){
                        playerInfos.get(position).setPbat(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pbat", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
//                        scores.put("pheight", playerInfos.get(position).getHeight());
//                        scores.put("pweight", playerInfos.get(position).getWeight());
//                        scores.put("phit", playerInfos.get(position).getHitting());
//                        scores.put("pinfield", playerInfos.get(position).getInfield());
//                        scores.put("poutfield", playerInfos.get(position).getOutfield());
//                        scores.put("pthrow", playerInfos.get(position).getThrowing());
//                        scores.put("parm", playerInfos.get(position).getArm());
//                        scores.put("pspeed", playerInfos.get(position).getSpeed());
//                        scores.put("pbase", playerInfos.get(position).getBase());

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Infield Mechanics")){
                        playerInfos.get(position).setPinfield(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pinfield", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
//                        scores.put("pheight", playerInfos.get(position).getHeight());
//                        scores.put("pweight", playerInfos.get(position).getWeight());
//                        scores.put("phit", playerInfos.get(position).getHitting());
//                        scores.put("pbat", playerInfos.get(position).getBat());
//                        scores.put("poutfield", playerInfos.get(position).getOutfield());
//                        scores.put("pthrow", playerInfos.get(position).getThrowing());
//                        scores.put("parm", playerInfos.get(position).getArm());
//                        scores.put("pspeed", playerInfos.get(position).getSpeed());
//                        scores.put("pbase", playerInfos.get(position).getBase());

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Outfield Mechanics")){
                        playerInfos.get(position).setPoutfield(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("poutfield", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
                        /*scores.put("pheight", playerInfos.get(position).getPheight());
                        scores.put("pweight", playerInfos.get(position).getPweight());
                        scores.put("phit", playerInfos.get(position).getPhit());
                        scores.put("pbat", playerInfos.get(position).getBat());
                        scores.put("pinfield", playerInfos.get(position).getInfield());
                        scores.put("pthrow", playerInfos.get(position).getThrowing());
                        scores.put("parm", playerInfos.get(position).getArm());
                        scores.put("pspeed", playerInfos.get(position).getSpeed());
                        scores.put("pbase", playerInfos.get(position).getBase());*/

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Throwing Mechanics")){
                        playerInfos.get(position).setPthrow(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pthrow", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
                        /*scores.put("pheight", playerInfos.get(position).getHeight());
                        scores.put("pweight", playerInfos.get(position).getWeight());
                        scores.put("phit", playerInfos.get(position).getHitting());
                        scores.put("pbat", playerInfos.get(position).getBat());
                        scores.put("pinfield", playerInfos.get(position).getInfield());
                        scores.put("poutfield", playerInfos.get(position).getOutfield());
                        scores.put("parm", playerInfos.get(position).getArm());
                        scores.put("pspeed", playerInfos.get(position).getSpeed());
                        scores.put("pbase", playerInfos.get(position).getBase());*/

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Arm Strength")){
                        playerInfos.get(position).setParm(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("parm", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
                        /*scores.put("pheight", playerInfos.get(position).getHeight());
                        scores.put("pweight", playerInfos.get(position).getWeight());
                        scores.put("phit", playerInfos.get(position).getHitting());
                        scores.put("pbat", playerInfos.get(position).getBat());
                        scores.put("pinfield", playerInfos.get(position).getInfield());
                        scores.put("poutfield", playerInfos.get(position).getOutfield());
                        scores.put("pthrow", playerInfos.get(position).getThrowing());
                        scores.put("pspeed", playerInfos.get(position).getSpeed());
                        scores.put("pbase", playerInfos.get(position).getBase());*/

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Speed")){
                        playerInfos.get(position).setPspeed(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pspeed", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
                        /*scores.put("pheight", playerInfos.get(position).getHeight());
                        scores.put("pweight", playerInfos.get(position).getWeight());
                        scores.put("phit", playerInfos.get(position).getHitting());
                        scores.put("pbat", playerInfos.get(position).getBat());
                        scores.put("pinfield", playerInfos.get(position).getInfield());
                        scores.put("poutfield", playerInfos.get(position).getOutfield());
                        scores.put("pthrow", playerInfos.get(position).getThrowing());
                        scores.put("parm", playerInfos.get(position).getArm());
                        scores.put("pbase", playerInfos.get(position).getBase());*/

                        firebase.updateChildren(scores);
                    }
                    if (actNameVal.equals("Base Running")){
                        playerInfos.get(position).setPbase(progress);
                        holder.activityScore.setText(String.valueOf(progress));
                        Log.d(LOG_TAG, "progress =" + progress + " for " + actNameVal + " at position: " + position);
                        String playid = playerInfos.get(position).getPlayerid();
                        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+sport+"/"+season+"/"+playid);
                        Map<String, Object> scores = new HashMap<String, Object>();
                        scores.put("pbase", progress);
                        String birth = playerInfos.get(position).getBirth();
                        String firstname = playerInfos.get(position).getFirstname();
                        String lastname = playerInfos.get(position).getLastname();
                        int jersey = playerInfos.get(position).getJersey();
                        String pdate = dateLook();
                        scores.put("birth", birth);
                        scores.put("firstname", firstname);
                        scores.put("lastname", lastname);
                        scores.put("jersey",jersey);
                        scores.put("pdate", pdate);
                        scores.put("playerid", playid);
                        scores.put("pnote", "");
                        scores.put("pdraft", "Yes");
                        /*scores.put("pheight", playerInfos.get(position).getHeight());
                        scores.put("pweight", playerInfos.get(position).getWeight());
                        scores.put("phit", playerInfos.get(position).getHitting());
                        scores.put("pbat", playerInfos.get(position).getBat());
                        scores.put("pinfield", playerInfos.get(position).getInfield());
                        scores.put("poutfield", playerInfos.get(position).getOutfield());
                        scores.put("pthrow", playerInfos.get(position).getThrowing());
                        scores.put("parm", playerInfos.get(position).getArm());
                        scores.put("pspeed", playerInfos.get(position).getSpeed());*/

                        firebase.updateChildren(scores);
                    }

                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
    //Update rank is not working, dropping for now 2016-04-21
    private void updateRank(String id, String attribute, int score){
        //context = context.getApplicationContext();

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

        if (exists(id)){
            ContentValues valuesin = new ContentValues();
            valuesin.put(attribute, score);
            valuesin.put("date", dateval);
            try {

                int row = playerIDLookup(id);
                String rid = Integer.toString(row);
                Log.d(LOG_TAG, "Row id ="+rid);
                Uri uri = Uri.parse(RankContentProvider.CONTENT_URI + "/" + rid);
                mContext.getContentResolver().update(uri, valuesin, null, null);

            } catch (Exception e) {
                Log.d(LOG_TAG, "exception updating friend:" + e);
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(attribute, score);
            values.put("date", dateval);
            try{
            Uri uri = mContext.getContentResolver().insert(
                    RankContentProvider.CONTENT_URI, values);

            String alertId = uri.getPathSegments().get(1);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Exception loading friend:" + String.valueOf(e));
        }
        }

    }

    public boolean exists(String key) {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RankDB.DATABASE_TABLE);
        Cursor c = qb.query(db, null, RankDB.KEY_PLAYID + " = ?", new String[]{key}, null, null, null);
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }
    public int playerIDLookup(String key) {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RankDB.DATABASE_TABLE);
        Cursor c = qb.query(db, null, RankDB.KEY_PLAYID + " = ?", new String[]{key}, null, null, null);
        try {
            c.moveToFirst();

            int rowId = c.getInt(c.getColumnIndexOrThrow(RankDB.KEY_ROWID));
            return rowId;
        } finally {
            c.close();
        }
    }

    public String dateLook(){
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
        return dateval;
    }



}