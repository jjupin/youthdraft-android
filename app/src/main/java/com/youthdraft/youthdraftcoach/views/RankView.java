package com.youthdraft.youthdraftcoach.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.adapter.AlertAdapter;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerRank;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marty331 on 1/25/16.
 */
public class RankView extends Fragment implements  View.OnClickListener {


    Context context;
    public static String LOG_TAG = "RankView";
    public static final String PROPERTY_FRAG = "current_frag";
    public static final String PROPERTY_PREFERENCES = "MyPrefs";
    public static final String PROPERTY_UID = "coach_uid";
    public static final String PROPERTY_LEAGUE = "league";
    public static final String PROPERTY_COACH_SPORT = "coach_sport";
    public static final String PROPERTY_COACH_SEASON = "coach_season";
    public static final String PROPERTY_COACH_YEAR = "coach_year";
    public static final String PROPERTY_HEIGHT = "height_weight";
    public static final String PROPERTY_WEIGHT = "weight_weight";
    public static final String PROPERTY_SPEED = "speed_weight";
    public static final String PROPERTY_HITTING = "hitting_weight";
    public static final String PROPERTY_BAT_SPEED = "bat_speed_weight";
    public static final String PROPERTY_INFIELD = "infield_weight";
    public static final String PROPERTY_OUTFIELD = "outfield_weight";
    public static final String PROPERTY_THROW = "throwing_weight";
    public static final String PROPERTY_ARM = "arm_weight";
    public static final String PROPERTY_BASE_RUNNING = "base_running_weight";

    int listindex, listtop;
    ListView listView;
    ProgressDialog progressDialog;
    TextView playName, playLast, playScore, playHeight, playWeight, playHit, playBat, playInfield, playOutfield, playThrow, playArm, playSpeed, playBaseRunning, playDraft, playDate;
    EditText playNote;
    AlertAdapter mAlertAdapter;
    FloatingActionButton floatingActionButton;//, fabCalc;
    //RecalcRanks recalcRanks;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private HashMap<String, PlayerScores> mPlayerScoreHashMap;
    private HashMap<String, PlayerRank> mMPlayersHashMap;
    List<String> playList;
    List<String> rankOrder;
    List<String> playOrder;
    List<PlayerRank> playerRanks;
    List<PlayerScores> playerScoresList;

    public RankView() {
    }

    Firebase firebase, firebaseRank, firebasePrank, firebaseWeight;
    String accountuid, leagueid, sport, season, year;
    int sHeight, sWeight, sHit, sBat, sInfield, sOutfield, sThrow, sArm, sSpeed, sBase;
    String[] ranks = null;
    private WebView mWebView;


    File myFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rank_view, container, false);

        // Get ListView object from xml
        listView = (ListView) view.findViewById(R.id.lvRank);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Rankings....");
        //recalcRanks = new RecalcRanks();
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        //fabCalc = (FloatingActionButton) view.findViewById(R.id.fabRecalc);
        //fabCalc.setOnClickListener(this);
        mMPlayersHashMap = new HashMap<String, PlayerRank>();
        mPlayerScoreHashMap = new HashMap<String, PlayerScores>();
        playList = new ArrayList<String>();
        rankOrder = new ArrayList<String>();
        playOrder = new ArrayList<String>();
        playDraft = (TextView) view.findViewById(R.id.tvDraftVal);
        playerRanks = new ArrayList<PlayerRank>();
        playerScoresList = new ArrayList<PlayerScores>();




        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (savedInstanceState == null) {
            //Log.d(LOG_TAG, "onCreate savedinstancstate null");
        } else {
            Log.d(LOG_TAG, "onCreate savedinstancstate not null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.d(LOG_TAG, "onActivityCreated");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Log.d(LOG_TAG, "onAttach context =" + getContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                try {
                    //createPdfPrint();
                    MainActivity.getBottomSheet().setVisibility(View.VISIBLE);
                    MainActivity.getBottomSheetBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

                    /**
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                     **/
                } finally {
                    //viewPdf();
                }


                break;
//            case R.id.fabRecalc:
//                recalcRanks.recalcPlayers(context.getApplicationContext());
//                displayRank();
//                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Firebase.setAndroidContext(getContext());
        //Log.d(LOG_TAG, "onResume");
        playList.clear();
        accountuid = getUid(getContext());
        leagueid = getPropertyLeague(getContext());
        sport = getPropertyCoachSport(getContext());
        season = getPropertyCoachSeason(getContext());
        year = getPropertyCoachYear(getContext());
        Log.d(LOG_TAG, "year ="+year);
        firebase = new Firebase("https://blistering-fire-5846.firebaseio.com/users/" + accountuid +"/"+leagueid+"/ranked/"+sport+"/"+season+year);
        firebaseWeight = new Firebase("https://blistering-fire-5846.firebaseio.com/users/" + accountuid +"/"+leagueid);
        sHeight = getHeight(getContext());
        sWeight = getWeight(getContext());
        sHit = getHit(getContext());
        sBat = getBatSpeed(getContext());
        sInfield = getInfield(getContext());
        sOutfield = getOutfield(getContext());
        sThrow = getThrow(getContext());
        sArm = getArmstrength(getContext());
        sSpeed = getSpeed(getContext());
        sBase = getBaseRun(getContext());


        displayRank();

        if(mAlertAdapter!=null)

        {
            mAlertAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d(LOG_TAG, "onPause");
    }

    public void displayRank() {
        //Log.d(LOG_TAG, "displayRank called");
        progressDialog.show();
        new GetWeights().execute();
        //new RankList().execute();

    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d(LOG_TAG, "onStart");
    }

    private void createPdfPrint() throws FileNotFoundException, DocumentException, IOException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document(PageSize.LETTER);

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //begin table creation
        Paragraph paragraph = new Paragraph();
        //create fonts
        //special font sizes
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font rbf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        rbf12.setColor(BaseColor.RED);
        //specify column widths
        float[] columnWidths = {1f, 2f, 2f, 2f, 2f, 2f};
        //create PDF table with the given widths
        PdfPTable table = new PdfPTable(columnWidths);
        // set table width a percentage of the page width
        table.setWidthPercentage(90f);

        //insert column headings
        Uri otherPath = Uri.parse("android.resource://com.youthdraft.youthdraftcoach/res/mipmap/logo.png");

        insertPicCell(table, createImageCell(otherPath.getPath()), Element.ALIGN_CENTER, 2);
        insertCell(table, "YouthDraft Ranking", Element.ALIGN_CENTER, 2, bfBold12);
        insertCell(table, "League: " + getPropertyLeague(getContext()).toString(), Element.ALIGN_LEFT, 1, bfBold12);
        insertCell(table, getDateTime(), Element.ALIGN_LEFT, 1, bfBold12);

        insertCell(table, "Rank", Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "First Name", Element.ALIGN_LEFT, 1, bfBold12);
        insertCell(table, "Last Name", Element.ALIGN_LEFT, 1, bfBold12);
        insertCell(table, "Composite Score", Element.ALIGN_LEFT, 1, bfBold12);
        insertCell(table, "Raw Score", Element.ALIGN_LEFT, 1, bfBold12);
        insertCell(table, "Birthday", Element.ALIGN_LEFT, 1, bfBold12);
        table.setHeaderRows(2);
        //Log.d(LOG_TAG, "playlist size =" + playList.size());
        //Log.d(LOG_TAG, "mMplayersHashMap =" + mMPlayersHashMap.size());
        if (mMPlayersHashMap != null) {
            for (int i = 0; i < playList.size(); i++) {
                String id = playList.get(i);// playList.get(0);
                //Log.d(LOG_TAG, "playlist id ="+playList.get(i));
                String birthday = "";
                PlayerRank playerRank1 = mMPlayersHashMap.get(id);
                int rank = i + 1;
                try {
                    birthday = playerRank1.getBirth().toString();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "birtday =" + e);
                    birthday = "";
                }
                //Log.d(LOG_TAG, "draftable ="+playerRank1.getDraftable().toString());
                if (playerRank1.getDraftable().toString().equals("Yes")) {
                    insertCell(table, String.valueOf(rank), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, playerRank1.getFirst_name().toString(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, playerRank1.getLast_name().toString(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, playerRank1.getRanking().toString(), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, String.valueOf(playerRank1.getRawscore()), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, birthday, Element.ALIGN_CENTER, 1, bf12);
                } else {
                    insertCell(table, String.valueOf(rank), Element.ALIGN_CENTER, 1, rbf12);
                    insertCell(table, playerRank1.getFirst_name().toString(), Element.ALIGN_LEFT, 1, rbf12);
                    insertCell(table, playerRank1.getLast_name().toString(), Element.ALIGN_LEFT, 1, rbf12);
                    insertCell(table, playerRank1.getRanking().toString(), Element.ALIGN_CENTER, 1, rbf12);
                    insertCell(table, String.valueOf(playerRank1.getRawscore()), Element.ALIGN_CENTER, 1, rbf12);
                    insertCell(table, birthday, Element.ALIGN_CENTER, 1, rbf12);
                }


            }

            document.add(new Chunk("")); // << this will do the trick.
            paragraph.add(table);
            paragraph.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            float[] columnWidthsWeight = {1f, 1f};
            PdfPTable table1 = new PdfPTable(columnWidthsWeight);
            insertCell(table1, "Current Weighted Values", Element.ALIGN_CENTER, 2, bfBold12);
            insertCell(table1, "Activity", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table1, "Value", Element.ALIGN_CENTER, 1, bfBold12);
            table1.setHeaderRows(2);
            insertCell(table1, "Height", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sHeight), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Weight", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sWeight), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Hitting Mechanics", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sHit), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Bat Speed", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sBat), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Infield Mechanics", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sInfield), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "OutField Mechanics", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sOutfield), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Throwing Mechanics", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sThrow), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Arm Strength", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sArm), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Speed", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sSpeed), Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, "Base Running", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table1, String.valueOf(sBase), Element.ALIGN_CENTER, 1, bf12);


            paragraph.add(table1);
            document.add(paragraph);
        }

        //Step 5: Close the document
        //Log.d(LOG_TAG, "doc page ="+document.getPageNumber());
        document.close();

    }

    public PdfPCell createImageCell(String path) throws DocumentException, IOException {


        //Image i = Image.getInstance(String.valueOf(R.mipmap.logo));
        Drawable d = getResources().getDrawable(R.mipmap.yd_logo);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapData = stream.toByteArray();
        Image img = Image.getInstance(bitmapData);
        img.scalePercent(20);
        img.setTransparency(new int[]{0xF0, 0x10});
        PdfPCell cell = new PdfPCell(img);

        return cell;
    }

    private void insertPicCell(PdfPTable table, PdfPCell image, int align, int colspan) {
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(image);
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row

        //add the call to the table
        table.addCell(cell);
    }


    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    private void viewPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    public String getDateTime() {

        Date date = new Date();
        String dateval = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss").format(date);
        return dateval;
    }


    public void setFragVal(int val) {
        Context context;
        context = getContext();
        SharedPreferences sharedpreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PROPERTY_FRAG, val);
        editor.apply();
    }

    private String getUid(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = prefs.getString(PROPERTY_UID, null);

        return value;
    }

    private String getPropertyLeague(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = preferences.getString(PROPERTY_LEAGUE, null);
        return value;
    }

    private String getPropertyCoachSport(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = preferences.getString(PROPERTY_COACH_SPORT, null);

        return value;
    }

    private String getPropertyCoachSeason(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(PROPERTY_COACH_SEASON, null);
        return value;
    }
    private  String getPropertyCoachYear(Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PROPERTY_PREFERENCES, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(PROPERTY_COACH_YEAR, null);
        return value;
    }

    private class RankList extends AsyncTask<Void, Void, String> {
        String potato;
        int rankNum = 1;
        boolean done = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(LOG_TAG, "RankList preExecute");

        }

        @Override
        protected String doInBackground(Void... params) {
            //Log.d(LOG_TAG, "RankList doInBackground");
            //Log.d(LOG_TAG, "account ="+accountuid+" sport="+sport+" season="+season);

            //sqLiteDatabase.delete("rank", null, null);
            firebasePrank = new Firebase("https://blistering-fire-5846.firebaseio.com/users/" + accountuid + "/" +leagueid+ "/ranked/" + sport + "/" + season+year+"/");
            Query queryRef = firebasePrank.orderByChild("prank");

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    rankNum++;
                    //Log.d(LOG_TAG, "ranklist number =" + postSnapshot.getChildrenCount());
                    //Log.d(LOG_TAG, "ranklist =" + postSnapshot.getChildren());
                    //Log.d(LOG_TAG, "ranklist key=" + dataSnapshot.getKey().toString());
                    String playerid = dataSnapshot.getKey().toString();
                    //Log.d(LOG_TAG, "ranklist value =" + dataSnapshot.getValue().toString());
                    PlayerScores playerScores = dataSnapshot.getValue(PlayerScores.class);

                    String id = playerScores.getPlayerid();
                    String birth = playerScores.getBirth();
                    potato = id;
                    String first = playerScores.getFirstname();
                    //Log.d(LOG_TAG, "first =" + playerScores.getFirstname());
                    String last = playerScores.getLastname();
                    String initial = "";
                    if (playerScores.getMidinitial() != null) {
                        initial = playerScores.getMidinitial();
                    } else {
                        initial = "";
                    }
                    Double rank = Math.round(playerScores.getPrank()*100.0)/100.0;
                    Log.d(LOG_TAG, "rank =" + rank);

                    String pdraft = playerScores.getPdraft();
                    int pheight = playerScores.getPheight();
                    //Log.d(LOG_TAG, "pheight =" + pheight);
                    int pweight = playerScores.getPweight();
                    //Log.d(LOG_TAG, "pweight =" + pweight);
                    int phit = playerScores.getPhit();
                    //Log.d(LOG_TAG, "phit =" + phit);
                    int pbat = playerScores.getPbat();
                    //Log.d(LOG_TAG, "pbat ="+pbat);
                    int pinfield = playerScores.getPinfield();
                    int poutfield = playerScores.getPoutfield();
                    int pthrow = playerScores.getPthrow();
                    //Log.d(LOG_TAG, "pthrow =" + pthrow);
                    int parm = playerScores.getParm();
                    //Log.d(LOG_TAG, "parm =" + parm);
                    int pspeed = playerScores.getPspeed();
                    //Log.d(LOG_TAG, "pspeed =" + pspeed);
                    int pbase = playerScores.getPbase();


                    String notes = playerScores.getPnote();
                    String dateval = playerScores.getPdate();
                    int jersey = playerScores.getJersey();
                    int rawscore = pheight + pweight + phit + pbat + pinfield + poutfield + pthrow + parm + pspeed + pbase;
                    String raw = String.valueOf(rawscore);
                    //rawscore = playerScores.getPrank();

                    playList.add(id);
                    PlayerRank playerRank = new PlayerRank(id, first, last, rank, pdraft, rawscore, birth);
                    Log.d(LOG_TAG, "playerRank first ="+playerRank.getFirst_name()+" raw="+playerRank.getRawscore());
                    playerRanks.add(playerRank);
                    playerScoresList.add(playerScores);

                    done = true;
                    //}
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

            if (done) {
                return "test";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "test";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(LOG_TAG, "RankList onPostExecute:" + s);

            if (s != null) {
                //Log.d(LOG_TAG, "playlist ="+playList.size());
                //Log.d(LOG_TAG, "mMplayersHashMap ="+mMPlayersHashMap.size());

                //Log.d(LOG_TAG, "getActivty =" + getActivity());
                //Log.d(LOG_TAG, "getContext ="+getContext());
                if (getContext() != null) {
                    Log.d(LOG_TAG, "calling updateList");
                    updateList();

                }
            }
        }
    }
    public void updateList(){
        //Log.d(LOG_TAG, "mMplayers count="+mMPlayersHashMap.size());

        for (int p = 0; p < playerRanks.size(); p++) {
            Log.d(LOG_TAG, "before player ="+playerRanks.get(p).getFirst_name()+" rank="+playerRanks.get(p).getRanking());
        }
        Collections.sort(playerRanks, new Comparator<PlayerRank>() {
            @Override
            public int compare(PlayerRank c1, PlayerRank c2) {
                return Double.compare(c1.getRanking(), c2.getRanking());
            }
        });

        //Log.d(LOG_TAG, "playerRanks size="+playerRanks.size());
        for (int p = 0; p < playerRanks.size(); p++) {
            Log.d(LOG_TAG, "player ="+playerRanks.get(p).getFirst_name()+" rank="+playerRanks.get(p).getRanking());
            mMPlayersHashMap.put(playerRanks.get(p).getRowid(), playerRanks.get(p));
            rankOrder.add(playerRanks.get(p).getRowid());
        }
        Collections.reverse(rankOrder);
        Collections.sort(playerScoresList, new Comparator<PlayerScores>() {
            @Override
            public int compare(PlayerScores c1, PlayerScores c2) {
                return Double.compare(c1.getPrank(), c2.getPrank());
            }
        });

        //Log.d(LOG_TAG, "playerRanks size="+playerRanks.size());
        for (int p = 0; p < playerScoresList.size(); p++) {
            //Log.d(LOG_TAG, "playerOrder ="+playerScoresList.get(p).getFirstname()+" rank="+playerScoresList.get(p).getPrank());
            mPlayerScoreHashMap.put(playerScoresList.get(p).getPlayerid(), playerScoresList.get(p));
            playOrder.add(playerScoresList.get(p).getPlayerid());
        }
        Collections.reverse(playOrder);
        //Log.d(LOG_TAG, "rankOrder ="+rankOrder.toString());
        mAlertAdapter = new AlertAdapter(getActivity(), mMPlayersHashMap, rankOrder);

        listView.setAdapter(mAlertAdapter);
        mAlertAdapter.notifyDataSetChanged();

        listView.refreshDrawableState();

        /*if (listView.isShown()) {
            progressDialog.dismiss();
        }*/
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        if (listindex != 0 && listtop != 0) {
            listView.setSelectionFromTop(listindex, listtop);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(LOG_TAG, "position =" + position);

                String rowID;

                //Log.d(LOG_TAG, "playlist val ="+playList.get(position).toString());
                String pos = playList.get(position).toString();
                //Log.d(LOG_TAG, "mMPlayersHashMap ="+mMPlayersHashMap.get(pos).getRowid());
                //Log.d(LOG_TAG, "playerRank1 ="+playerRank1.toString());
                rowID = mPlayerScoreHashMap.get(playOrder.get(position)).getPlayerid();

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater(null);
                View view1 = inflater.inflate(R.layout.player_ranking, null);
                alert.setView(view1);
                playName = (TextView) view1.findViewById(R.id.tvFirstName);
                playLast = (TextView) view1.findViewById(R.id.tvLastName);
                playScore = (TextView) view1.findViewById(R.id.tvCompositeScore);
                playHeight = (TextView) view1.findViewById(R.id.tvHeightScore);
                playWeight = (TextView) view1.findViewById(R.id.tvWeightScore);
                playHit = (TextView) view1.findViewById(R.id.tvHittingScore);
                playBat = (TextView) view1.findViewById(R.id.tvBattingScore);
                playInfield = (TextView) view1.findViewById(R.id.tvInfieldScore);
                playOutfield = (TextView) view1.findViewById(R.id.tvOutfieldScore);
                playThrow = (TextView) view1.findViewById(R.id.tvThrowingScore);
                playArm = (TextView) view1.findViewById(R.id.tvArmScore);
                playSpeed = (TextView) view1.findViewById(R.id.tvSpeedScore);
                playBaseRunning = (TextView) view1.findViewById(R.id.tvBaseScore);
                playDraft = (TextView) view1.findViewById(R.id.tvDraftableScore);
                playDate = (TextView) view1.findViewById(R.id.tvDatetime);

                playNote = (EditText) view1.findViewById(R.id.etNoteValue);


                //Log.d(LOG_TAG, "first pop ="+cursor.getString(cursor.getColumnIndex(RankDB.KEY_FIRSTNAME)));
                playName.setText(mPlayerScoreHashMap.get(playOrder.get(position)).getFirstname());
                playLast.setText(mPlayerScoreHashMap.get(playOrder.get(position)).getLastname());

                playScore.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPrank()));
                playHeight.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPheight()));
                playWeight.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPweight()));
                playHit.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPhit()));
                playBat.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPbat()));
                playInfield.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPinfield()));
                playOutfield.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPoutfield()));
                playThrow.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPthrow()));
                playArm.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getParm()));
                playSpeed.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPspeed()));
                playBaseRunning.setText(String.valueOf(mPlayerScoreHashMap.get(playOrder.get(position)).getPbase()));
                playDate.setText(mPlayerScoreHashMap.get(playOrder.get(position)).getPdate());


                String draft = mPlayerScoreHashMap.get(playOrder.get(position)).getPdraft();
                Log.d(LOG_TAG, "draft =" + draft.toString());
                if (draft.equals("Yes")) {
                    playDraft.setText("Draftable");
                } else {
                    playDraft.setText("Not Draftable");
                }
                playNote.setText(mPlayerScoreHashMap.get(playOrder.get(position)).getPnote());



                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });

    }


    private class GetWeights extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d(LOG_TAG, "getWeights started");
            //Log.d(LOG_TAG, "accountid =" + accountuid);

            firebaseWeight.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot1) {
                    for (DataSnapshot postSnapshot : dataSnapshot1.getChildren()) {
                        Log.d(LOG_TAG, "getweights postSnapshot ="+postSnapshot.getKey().toString());

                        if (postSnapshot.getKey().toString().equals("wHeight")) {
                            System.out.println("height value =" + postSnapshot.getValue());
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sHeight = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sHeight = 100;
                            }

                            Log.d(LOG_TAG, "sHeight =" + sHeight);
                        }
                        if (postSnapshot.getKey().toString().equals("wWeight")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sWeight = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sWeight = 100;
                            }

                            Log.d(LOG_TAG, "sweight =" + sWeight);
                        }
                        if (postSnapshot.getKey().toString().equals("wHitting")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sHit = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sHit = 100;
                            }

                            Log.d(LOG_TAG, "sHit = " + sHit);
                        }
                        if (postSnapshot.getKey().toString().equals("wBat")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sBat = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sBat = 100;
                            }

                            Log.d(LOG_TAG, "sBat=" + sBat);
                        }
                        if (postSnapshot.getKey().toString().equals("wInfield")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sInfield = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sInfield = 100;
                            }
                            Log.d(LOG_TAG, "sInfield =" + sInfield);

                        }
                        if (postSnapshot.getKey().toString().equals("wOutfield")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sOutfield = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sOutfield = 100;
                            }

                            Log.d(LOG_TAG, "sOutfield=" + sOutfield);
                        }
                        if (postSnapshot.getKey().toString().equals("wThrowing")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sThrow = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sThrow = 100;
                            }

                            Log.d(LOG_TAG, "sThrow =" + sThrow);
                        }

                        if (postSnapshot.getKey().toString().equals("wArm")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sArm = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sArm = 100;
                            }

                            Log.d(LOG_TAG, "sArm =" + sArm);
                        }
                        if (postSnapshot.getKey().toString().equals("wSpeed")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                sSpeed = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sSpeed = 100;
                            }

                            Log.d(LOG_TAG, "sSpeed =" + sSpeed);
                        }
                        if (postSnapshot.getKey().toString().equals("wBase")) {
                            if (!postSnapshot.getValue().toString().equals(null)) {
                                //sBase = postSnapshot.getValue().toString();
                                sBase = Integer.valueOf(postSnapshot.getValue().toString());
                            } else {
                                sBase = 100;
                            }
                            Log.d(LOG_TAG, "sBase =" + sBase);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return "";
        }

        @Override
        protected void onPostExecute(String value) {
            Log.d(LOG_TAG, "GetWeights post");

            new GetRanks().execute();
        }

    }

    private class GetRanks extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            firebaseRank = new Firebase("https://blistering-fire-5846.firebaseio.com/users/" + accountuid + "/" +leagueid + "/ranked/" + sport + "/" + season+year+"/");
            Log.d(LOG_TAG, "https://blistering-fire-5846.firebaseio.com/users/" + accountuid + "/" +leagueid + "/ranked/" + sport + "/" + season);

            firebaseRank.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Log.d(LOG_TAG, "getrank =" + dataSnapshot.getChildren().toString());
                    //Log.d(LOG_TAG, "getrank key=" + dataSnapshot.getKey().toString());
                    String playerid = dataSnapshot.getKey().toString();
                    /*Log.d(LOG_TAG, "playerid ="+playerid);
                    Log.d(LOG_TAG, "key ="+dataSnapshot.getKey().toString());
                    Log.d(LOG_TAG, "value="+dataSnapshot.getValue().toString());
                    Log.d(LOG_TAG, "getrank value =" + dataSnapshot.getChildren().toString());*/
                    PlayerScores playerScores = dataSnapshot.getValue(PlayerScores.class);
                    int pthrow, pheight, pweight, phit, pbat, poutf, pinf, parm, pspeed, pbase;

                    //Log.d(LOG_TAG, "firstname ="+playerScores.getFirstname());
                    try {
                        pheight = playerScores.getPheight();
                    } catch (Exception e) {
                        pheight = 0;
                    }

                    //Log.d(LOG_TAG, "pheight =" + pheight);
                    try {
                        pweight = playerScores.getPweight();
                        //Log.d(LOG_TAG, "pweight =" + pweight);
                    } catch (Exception e) {
                        pweight = 0;

                    }
                    try {
                        phit = playerScores.getPhit();
                        //Log.d(LOG_TAG, "phit =" + phit);
                        //Log.d(LOG_TAG, "sHit ="+sHit);
                    } catch (Exception e) {
                        phit = 0;

                    }
                    try {
                        pbat = playerScores.getPbat();
                        /*Log.d(LOG_TAG, "pbat =" + pbat);
                        Log.d(LOG_TAG, "sBat =" + sBat);*/

                    } catch (Exception e) {
                        pbat = 0;

                    }
                    try {
                        pinf = playerScores.getPinfield();

                    } catch (Exception e) {
                        pinf = 0;

                    }
                    try {
                        poutf = playerScores.getPoutfield();

                    } catch (Exception e) {
                        poutf = 0;

                    }
                    try {
                        pthrow = playerScores.getPthrow();
                        //Log.d(LOG_TAG, "pthrow =" + pthrow);

                    } catch (Exception e) {
                        pthrow = 0;

                    }
                    try {
                        parm = playerScores.getParm();
                        //Log.d(LOG_TAG, "parm =" + parm);

                    } catch (Exception e) {
                        parm = 0;
                    }
                    try {
                        pspeed = playerScores.getPspeed();
                        //Log.d(LOG_TAG, "pspeed =" + pspeed);

                    } catch (Exception e) {
                        pspeed = 0;
                    }
                    try {
                        pbase = playerScores.getPbase();

                    } catch (Exception e) {
                        pbase = 0;
                    }
                    Log.d(LOG_TAG, "pheight ="+pheight+" sHeight="+sHeight);
                    Log.d(LOG_TAG, "pweight ="+pweight+" sWeight="+sWeight);
                    Log.d(LOG_TAG, "phit="+phit+" sHit="+sHit);
                    Log.d(LOG_TAG, "pbat="+pbat+" sBat="+sBat);
                    Log.d(LOG_TAG, "poutf =" + poutf + " sOutfield=" + sOutfield);
                    Log.d(LOG_TAG, "pinf =" + pinf + " sInfield=" + sInfield);
                    Log.d(LOG_TAG, "pthrow="+pthrow+" sThrow="+sThrow);
                    Log.d(LOG_TAG, "parm="+parm+" sArm="+sArm);
                    Log.d(LOG_TAG, "pspeed="+pspeed+" sSpeed="+sSpeed);
                    Log.d(LOG_TAG, "pbase="+pbase+" sBase="+sBase);
                    double prank = (pheight * ((double)sHeight/100)) + (pweight * ((double)sWeight/100)) +
                            (phit * ((double)sHit/100)) + (pbat * (double)(sBat/100)) +
                            (poutf * ((double)sOutfield/100)) + (pinf * ((double)sInfield/100)) +
                            (pthrow * ((double)sThrow/100)) + (parm * ((double)sArm/100)) +
                            (pspeed * ((double)sSpeed/100)) + (pbase * ((double)sBase/100));

                    playerScores.setPrank(prank);
                    firebasePrank = new Firebase("https://blistering-fire-5846.firebaseio.com/users/" + accountuid + "/" +leagueid + "/ranked/" + sport + "/" + season +year+ "/" + playerid);

                    firebasePrank.setValue(playerScores, - playerScores.getPrank());
                    //updateList();
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(LOG_TAG, "GetRanks post");

            //updateList();
            new RankList().execute();

        }
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

}