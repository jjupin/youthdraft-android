package com.youthdraft.youthdraftcoach.views;

/**
 * Created by marty331 on 1/26/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.youthdraft.youthdraftcoach.R;

import java.util.HashMap;
import java.util.Map;


public class WeightView extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    SeekBar hei, wei, osped, hitting, bat_speed, infield, outfield, throwing, arms, base_running;
    TextView heiVal, weiVal, speedVal, hittingVal, batSpeedVal, infieldVal, outfieldVal, throwingVal, armStrengthVal, baseRunVal;
    ImageButton  info;//recalc,
    private int minVal = 50;
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
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    //RecalcRanks recalcRanks;
    public static String LOG_TAG = "WeightView";
    Cursor cursor;
    Context mContext;
    Firebase firebase, fireup, fireweights;
    String sHeight, sWeight, sSpeed, sHitting, sBatSpeed, sInfield, sOutfield, sThrow, sArm, sBaseRun, leagueid;
    int iHeight, iWeight, iSpeed, iHitting, iBatSpeed, iInfield, iOutfield, iThrow, iArm, iBaseRun;

    public WeightView(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weight_view, container, false);

        mContext = getContext();
        //recalcRanks = new RecalcRanks();
        weiVal = (TextView) view.findViewById(R.id.tvWeightWeightVal);
        heiVal = (TextView) view.findViewById(R.id.tvHeightWeightVal);
        speedVal = (TextView) view.findViewById(R.id.tvSpeedWeightVal);
        hittingVal = (TextView) view.findViewById(R.id.tvHittingWeightVal);
        batSpeedVal = (TextView) view.findViewById(R.id.tvBatSpeedWeightVal);
        infieldVal = (TextView) view.findViewById(R.id.tvInfieldWeightVal);
        outfieldVal = (TextView) view.findViewById(R.id.tvOutfieldWeightVal);
        throwingVal = (TextView) view.findViewById(R.id.tvThrowingWeightVal);
        armStrengthVal = (TextView) view.findViewById(R.id.tvArmStrengthWeightVal);
        baseRunVal = (TextView) view.findViewById(R.id.tvBaseRunningWeightVal);

        //recalc = (ImageButton) view.findViewById(R.id.bRecalc);
        info = (ImageButton) view.findViewById(R.id.ibInfoWeight);

        hei = (SeekBar) view.findViewById(R.id.sHeightWeight);
        wei = (SeekBar) view.findViewById(R.id.sWeightWeight);
        osped = (SeekBar) view.findViewById(R.id.sSpeedWeight);
        hitting = (SeekBar) view.findViewById(R.id.sHittingWeight);
        bat_speed = (SeekBar) view.findViewById(R.id.sBatSpeedWeightWeight);
        infield = (SeekBar) view.findViewById(R.id.sInfieldWeight);
        outfield = (SeekBar) view.findViewById(R.id.sOutfieldWeight);
        throwing = (SeekBar) view.findViewById(R.id.sThrowingWeight);
        arms = (SeekBar) view.findViewById(R.id.sArmStrengthWeight);
        base_running = (SeekBar) view.findViewById(R.id.sBaseRunningWeight);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Firebase.setAndroidContext(getContext());
        final String accountuid = getUid(getContext());
        leagueid = getPropertyLeague(getContext());
        fireweights = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid +"/"+leagueid);
        getWeights();
        updateSliders();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void setHeightVal(int val) {
        Context context;
        context = getActivity();
        Log.d(LOG_TAG, "setHeightVal ="+val);
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_HEIGHT, val);
        editor.apply();
        saveWeight("wHeight", val);
    }


    private int getHeight(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_HEIGHT, 100);

        return value;
    }

    public void setWeightVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_WEIGHT, val);
        editor.apply();
        saveWeight("wWeight", val);
    }

    private int getWeight(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_WEIGHT, 100);

        return value;
    }

    public void setHitVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_HITTING, val);
        editor.apply();
        saveWeight("wHitting", val);
    }

    private int getHit(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_HITTING, 100);

        return value;
    }



    public void setBatSpeedVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_BAT_SPEED, val);
        editor.apply();
        saveWeight("wBat", val);

    }

    private int getBatSpeed(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_BAT_SPEED, 100);

        return value;
    }

    public void setInfield(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_INFIELD, val);
        editor.apply();
        saveWeight("wInfield", val);

    }

    private int getInfield(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_INFIELD, 100);

        return value;
    }

    public void setOutfieldVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_OUTFIELD, val);
        editor.apply();
        saveWeight("wOutfield", val);

    }

    private int getOutfield(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_OUTFIELD, 100);

        return value;
    }

    public void setThrowval(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_THROW, val);
        editor.apply();
        saveWeight("wThrowing", val);
    }


    private int getThrow(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_THROW, 100);

        return value;
    }


    public void setArmStrnVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_ARM, val);
        editor.apply();
        saveWeight("wArm", val);
    }

    private int getArmstrength(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_ARM, 100);

        return value;
    }

    public void setSpeedVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_SPEED, val);
        editor.apply();
        saveWeight("wSpeed", val);
    }

    private int getSpeed(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_SPEED, 100);

        return value;
    }

    public void setBaseRunVal(int val) {
        Context context;
        context = getActivity();
        sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_BASE_RUNNING, val);
        editor.apply();
        saveWeight("wBase", val);
    }

    private int getBaseRun(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        int value = prefs.getInt(PROPERTY_BASE_RUNNING, 100);

        return value;
    }

    private String getPropertyLeague(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = preferences.getString(PROPERTY_LEAGUE, null);
        return value;
    }


    private String getUid(Context context){
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_UID, null);

        return value;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.bRecalc:
//                recalcRanks.recalcPlayers(mContext.getApplicationContext());
//                break;
            case R.id.ibInfoWeight:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.weight_info_button);
                builder.setMessage(R.string.weight_info);
                builder.setPositiveButton("OK", null);
                builder.show();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sWeightWeight:
                weiVal.setText(String.valueOf(progress+minVal));
                setWeightVal(progress+minVal);
                break;
            case R.id.sHeightWeight:
                heiVal.setText(String.valueOf(progress+minVal));
                setHeightVal(progress+minVal);
                break;
            case R.id.sHittingWeight:
                hittingVal.setText(String.valueOf(progress+minVal));
                setHitVal(progress+minVal);
                break;
            case R.id.sBatSpeedWeightWeight:
                batSpeedVal.setText(String.valueOf(progress+minVal));
                setBatSpeedVal(progress+minVal);
                break;
            case R.id.sInfieldWeight:
                infieldVal.setText(String.valueOf(progress+minVal));
                setInfield(progress+minVal);
                break;
            case R.id.sOutfieldWeight:
                outfieldVal.setText(String.valueOf(progress+minVal));
                setOutfieldVal(progress+minVal);
                break;
            case R.id.sThrowingWeight:
                throwingVal.setText(String.valueOf(progress+minVal));
                setThrowval(progress+minVal);
                break;
            case R.id.sArmStrengthWeight:
                armStrengthVal.setText(String.valueOf(progress+minVal));
                setArmStrnVal(progress+minVal);
                break;
            case R.id.sSpeedWeight:
                speedVal.setText(String.valueOf(progress+minVal));
                setSpeedVal(progress+minVal);
                break;
            case R.id.sBaseRunningWeight:
                baseRunVal.setText(String.valueOf(progress+minVal));
                setBaseRunVal(progress+minVal);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void updateSliders() {

        hei.setProgress(getHeight(getContext()) - minVal);
        hei.setOnSeekBarChangeListener(this);
        heiVal.setText(String.valueOf(getHeight(getContext())));

        wei.setProgress(getWeight(getContext()) - minVal);
        wei.setOnSeekBarChangeListener(this);
        weiVal.setText(String.valueOf(getWeight(getContext())));

        hitting.setProgress(getHit(getContext()) - minVal);
        hitting.setOnSeekBarChangeListener(this);
        hittingVal.setText(String.valueOf(getHit(getContext())));

        bat_speed.setProgress(getBatSpeed(getContext())-minVal);
        bat_speed.setOnSeekBarChangeListener(this);
        batSpeedVal.setText(String.valueOf(getBatSpeed(getContext())));

        infield.setProgress(getInfield(getContext())-minVal);
        infield.setOnSeekBarChangeListener(this);
        infieldVal.setText(String.valueOf(getInfield(getContext())));

        outfield.setProgress(getOutfield(getContext())-minVal);
        outfield.setOnSeekBarChangeListener(this);
        outfieldVal.setText(String.valueOf(getOutfield(getContext())));

        throwing.setProgress(getThrow(getContext())-minVal);
        throwing.setOnSeekBarChangeListener(this);
        throwingVal.setText(String.valueOf(getThrow(getContext())));

        arms.setProgress(getArmstrength(getContext()) - minVal);
        arms.setOnSeekBarChangeListener(this);
        armStrengthVal.setText(String.valueOf(getArmstrength(getContext())));

        osped.setProgress(getSpeed(getContext()) - minVal);
        osped.setOnSeekBarChangeListener(this);
        speedVal.setText(String.valueOf(getSpeed(getContext())));

        base_running.setProgress(getBaseRun(getContext())-minVal);
        base_running.setOnSeekBarChangeListener(this);
        baseRunVal.setText(String.valueOf(getBaseRun(getContext())));

        //recalc.setOnClickListener(this);
        info.setOnClickListener(this);

    }


    public void getWeights(){
        Log.d(LOG_TAG, "getWeights started");
        fireweights.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot1, String s) {
                //Log.d(LOG_TAG, "user data =" + dataSnapshot1.getValue().toString());
                //System.out.println("user data =" + dataSnapshot1.getValue().toString());
                if(dataSnapshot1.getKey() == "wHeight"){
                    //System.out.println("height value ="+dataSnapshot1.getValue());
                    sHeight = dataSnapshot1.getValue().toString();
                    Log.d(LOG_TAG, "sHeight ="+sHeight);
                    iHeight = Integer.parseInt(sHeight);
                    Log.d(LOG_TAG, "iHeight ="+iHeight);
                    setHeightVal(iHeight);
                }
                if(dataSnapshot1.getKey() == "wWeight"){
                    sWeight = dataSnapshot1.getValue().toString();
                    setWeightVal(Integer.parseInt(sWeight));
                }
                if(dataSnapshot1.getKey() == "wHitting"){
                    sHitting = dataSnapshot1.getValue().toString();
                    setHitVal(Integer.parseInt(sHitting));
                }
                if(dataSnapshot1.getKey() == "wBat"){
                    sBatSpeed = dataSnapshot1.getValue().toString();
                    setBatSpeedVal(Integer.parseInt(sBatSpeed));
                }
                if(dataSnapshot1.getKey() == "wInfield"){
                    sInfield = dataSnapshot1.getValue().toString();
                    setInfield(Integer.parseInt(sInfield));
                }
                if(dataSnapshot1.getKey() == "wOutfield"){
                    sOutfield = dataSnapshot1.getValue().toString();
                    setOutfieldVal(Integer.parseInt(sOutfield));
                }
                if(dataSnapshot1.getKey() == "wThrowing"){
                    sThrow = dataSnapshot1.getValue().toString();
                    setThrowval(Integer.parseInt(sThrow));
                }
                if(dataSnapshot1.getKey() == "wArm"){
                    sArm = dataSnapshot1.getValue().toString();
                    setArmStrnVal(Integer.parseInt(sArm));
                }
                if(dataSnapshot1.getKey() == "wSpeed"){
                    sSpeed = dataSnapshot1.getValue().toString();
                    setSpeedVal(Integer.parseInt(sSpeed));
                }
                if(dataSnapshot1.getKey() == "wBase"){
                    sBaseRun = dataSnapshot1.getValue().toString();
                    setBaseRunVal(Integer.parseInt(sBaseRun));
                }

                updateSliders();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }



        });


    }

    private void saveWeight(String field, int fieldval){
        final String accountuid = getUid(getContext());
        //Log.d(LOG_TAG, "accountuid ="+accountuid);
        //Log.d(LOG_TAG, "field ="+field+" fieldval ="+fieldval);
        Map<String, Object> weightval = new HashMap<String, Object>();
        weightval.put(field, fieldval);

        fireweights.updateChildren(weightval);
    }


//    private void recalcPlayers(){
//        String[] ranks = {RankDB.KEY_ROWID, RankDB.KEY_SPEED, RankDB.KEY_HITTING,RankDB.KEY_THROWING, RankDB.KEY_ARM_STRENGTH,
//                RankDB.KEY_RATING, RankDB.KEY_HEIGHT, RankDB.KEY_WEIGHT, RankDB.KEY_BAT_SPEED, RankDB.KEY_INFIELD, RankDB.KEY_OUTFIELD, RankDB.KEY_BASE_RUNNING };
//        final double hei_weight = getHeight(getContext()) / 100;
//        final double weight_weight = getWeight(getContext())/100;
//        final double hit_weight = getHit(getContext())/100;
//        final double bat_weight = getBatSpeed(getContext())/100;
//        final double infield_weight = getInfield(getContext())/100;
//        final double outfield_weight = getOutfield(getContext())/100;
//        final double throw_weight = getThrow(getContext()) / 100;
//        final double arms_weight = getArmstrength(getContext()) / 100;
//        final double sped_weight = getSpeed(getContext()) / 100;
//        final double base_run_weight = getBaseRun(getContext())/100;
//
//
//        cursor = getActivity().getContentResolver().query(RankContentProvider.CONTENT_URI, ranks, null, null, RankDB.KEY_RATING+ " DESC");
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            Log.d(LOG_TAG, "cursor ="+cursor.getColumnNames().length);
//
//            int hei = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_HEIGHT));
//            int wei = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_WEIGHT));
//            int hitt = cursor.getInt(cursor.getColumnIndex("hitting"));
//            int bat = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_BAT_SPEED));
//            int inf = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_INFIELD));
//            int outf = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_OUTFIELD));
//            int throwing = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_THROWING));
//            int arms = cursor.getInt(cursor.getColumnIndex("arm_strength"));
//            int sped = cursor.getInt(cursor.getColumnIndex("speed"));
//            //Log.d(LOG_TAG, "baserun ="+cursor.getInt(cursor.getColumnIndex("base_running")));
//            int baserun = cursor.getInt(cursor.getColumnIndex("base_running"));
//
//
//            double ranked = 0.0;
//            ranked = (hei_weight * hei)+ (wei*weight_weight) + (hit_weight * hitt) + (bat_weight*bat)+(inf * infield_weight)+ (outf *outfield_weight) +
//                    (throw_weight * throwing) + (arms*arms_weight) +(sped_weight * sped) + (0 * base_run_weight);
//
//
//            ContentValues valuesin = new ContentValues();
//            valuesin.put(RankDB.KEY_RATING, ranked);
//            try {
//
//                int row = cursor.getInt(cursor.getColumnIndex(RankDB.KEY_ROWID));
//                String id = Integer.toString(row);
//                Uri uri = Uri.parse(RankContentProvider.CONTENT_URI + "/" + id);
//                getActivity().getContentResolver().update(uri, valuesin, null, null);
//
//            } catch (Exception e) {
//                Log.d(LOG_TAG, "exception updating friend:" + e);
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        final String accountuid = getUid(getContext());
//        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked");
//        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("There are " + dataSnapshot.getChildrenCount() + " players");
//
//                System.out.println("value =" + dataSnapshot.getValue());
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    //Log.d(LOG_TAG, "datas =" + postSnapshot.getValue().toString());
//                    PlayerScores playerScores = postSnapshot.getValue(PlayerScores.class);
//                    String playid = playerScores.getPlayerid();
//                    String first = playerScores.getFirstname();
//                    String last = playerScores.getLastname();
//                    String mid = playerScores.getMidinitial();
//                    String birth = playerScores.getBirth();
//                    int jer = playerScores.getJersey();
//
//                    int pheight = playerScores.getPheight();
//                    int pweigth = playerScores.getPweight();
//                    int phit = playerScores.getPhit();
//                    int pbat = playerScores.getPbat();
//                    int pinfield = playerScores.getPinfield();
//                    int poutfield = playerScores.getPoutfield();
//                    int pthrow = playerScores.getPthrow();
//                    int parms = playerScores.getParm();
//                    int pspeed = playerScores.getPspeed();
//                    int pbase = playerScores.getPbase();
//
//                    String pdraft = playerScores.getPdraft();
//                    String pnote = playerScores.getPnote();
//                    Calendar c = Calendar.getInstance();
//                    String seconds = String.valueOf(c.get(Calendar.SECOND));
//                    if(seconds.length()==1){
//                        seconds = "0"+seconds;
//                    }
//                    String min = String.valueOf(c.get(Calendar.MINUTE));
//                    if(min.length()==1){
//                        min = "0"+min;
//                    }
//                    String hour = String.valueOf(c.get(Calendar.HOUR));
//                    if(hour.length()==1){
//                        hour = "0"+hour;
//                    }
//                    String year = String.valueOf(c.get(Calendar.YEAR));
//                    String mnt = String.valueOf(c.get(Calendar.MONTH)+1);
//                    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
//                    String dateval = year+"-"+mnt+"-"+day+" "+hour+":"+min+":"+seconds;
//                    double ranked = 0.0;
//                    ranked = (pheight*hei_weight)+(pweigth*weight_weight) + (hit_weight * phit) + (bat_weight * pbat)+ (infield_weight * pinfield)+
//                            (outfield_weight * poutfield) + (throw_weight * pthrow) + (parms*arms_weight) + (sped_weight * pspeed) + (base_run_weight * pbase);
//                    double rawscore = pheight + pweigth + phit + pbat + pinfield + poutfield + pthrow + parms + pspeed + pbase;
//                    //fireup = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"sportid+"/"+seasonid+"/"+playid);
//                    float rranked = (float)ranked;
//                    float rrawscore = (float)rawscore;
//                    fireup = new Firebase("https://blistering-fire-5846.firebaseio.com/users/"+accountuid+"/ranked/"+playid);
//                    PlayerScores playerup = new PlayerScores(playid, first, last, mid, birth, jer, pheight, pweigth, phit, pbat, pinfield, poutfield, pthrow, parms,
//                            pspeed, pbase, pdraft, pnote, dateval, rranked, rrawscore);
//                    fireup.setValue(playerup);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//
//
//        Toast.makeText(getContext(), "Rankings recalculated", Toast.LENGTH_SHORT).show();
//    }

    public void setFragVal(int val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_FRAG, val);
        editor.apply();
    }
}