package com.youthdraft.youthdraftcoach.datamodel;

/**
 * Created by marty331 on 1/25/16.
 */

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerScores {
    private String playerid;
    private String firstname;
    private String lastname;
    private String midinitial;
    private String birth;
    private int jersey = 0;

    private int pheight = 0;
    private int pweight = 0;
    private int phit  = 0;
    private int pbat =0;
    private int pinfield =0;
    private int poutfield =0;
    private int pthrow =0;
    private int parm =0;
    private int pspeed =0;
    private int pbase =0;
    private String pdraft;
    private String pdate;
    private double prank =0.0;

    private String pnote;
    private String pnoteHit;
    private String pnoteBat;
    private String pnoteInfield;
    private String pnoteOutfield;
    private String pnoteThrow;
    private String pnoteArm;
    private String pnoteSpeed;
    private String pnoteBase;

    private boolean completedAssessment;
    private boolean pitcher = false;
    private boolean catcher = false;

    //private double praw;

    private String LOG_TAG = "PlayerScores";

    private int custom01;
    private int custom02;
    private int custom03;
    private int custom04;
    private int custom05;

    private String customNote01;
    private String customNote02;
    private String customNote03;
    private String customNote04;
    private String customNote05;


    public PlayerScores(){}

    public PlayerScores(String playerid, String firstname, String lastname, String midinitial, String birth,  int jersey,
                        int pheight, int pweight, int phit, int pbat, int poutfield, int pinfield, int pthrow, int parm, int pspeed, int pbase,
                        String pdraft, String pnote, String pdate, double prank, boolean assessmentCompleted){//, double praw) {
        this.playerid = playerid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.midinitial = midinitial;
        this.birth = birth;

        this.jersey = jersey;
        this.pheight = pheight;
        this.pweight = pweight;
        this.phit = phit;
        this.pbat = pbat;
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
        //this.praw = praw;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setJersey(int jersey) {
        this.jersey = jersey;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMidinitial(String midinitial) {
        this.midinitial = midinitial;
    }

    public void setParm(int parm) {
        this.parm = parm;
    }

    public void setPbase(int pbase) {
        this.pbase = pbase;
    }

    public void setPbat(int pbat) {
        this.pbat = pbat;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public void setPdraft(String pdraft) {
        this.pdraft = pdraft;
    }

    public void setPheight(int pheight) {
        this.pheight = pheight;
    }

    public void setPhit(int phit) {
        this.phit = phit;
    }

    public void setPinfield(int pinfield) {
        this.pinfield = pinfield;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public void setPnote(String pnote) {
        this.pnote = pnote;
    }

    public void setPoutfield(int poutfield) {
        this.poutfield = poutfield;
    }

    public void setPspeed(int pspeed) {
        this.pspeed = pspeed;
    }

    public void setPthrow(int pthrow) {
        this.pthrow = pthrow;
    }

    public void setPweight(int pweight) {
        this.pweight = pweight;
    }

    public String getBirth() {
        return birth;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getJersey() {
        return jersey;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFullname() {
        return firstname+" "+lastname;
    }



    public String getMidinitial() {
        return midinitial;
    }

    public int getParm() {
        return parm;
    }

    public int getPbase() {
        return pbase;
    }

    public int getPbat() {
        return pbat;
    }

    public String getPdate() {
        return pdate;
    }

    public String getPdraft() {
        return pdraft;
    }

    public int getPheight() {
        //return pheight;
        if (pheight < 0){
            Log.d(LOG_TAG, "pheight <0");
            return  0;
        } else {
            return pheight;
        }
    }

    public int getPhit() {
        return phit;
    }

    public int getPinfield() {
        return pinfield;
    }

    public String getPlayerid() {
        return playerid;
    }

    public String getPnote() {
        return pnote;
    }

    public int getPoutfield() {
        return poutfield;
    }

    public double getPrank() {
        return prank;
    }

    public void setPrank(double prank) {
        this.prank = prank;
    }

    public int getPspeed() {
        return pspeed;
    }

    public int getPthrow() {
        return pthrow;
    }

    public int getPweight() {

        //return (pweight != null ? pweight:0);
        if (pweight < 0){
            return  0;
        } else {
            return pweight;
        }
    }


    public boolean isCompletedAssessment() {
        return completedAssessment;
    }
    public boolean hasCompletedAssessment() {
        return isCompletedAssessment();
    }

    public void setCompletedAssessment(boolean completedAssessment) {
        this.completedAssessment = completedAssessment;
    }

    public String getPnoteHit() {
        return pnoteHit;
    }

    public void setPnoteHit(String pnoteHit) {
        this.pnoteHit = pnoteHit;
    }

    public String getPnoteBat() {
        return pnoteBat;
    }

    public void setPnoteBat(String pnoteBat) {
        this.pnoteBat = pnoteBat;
    }

    public String getPnoteInfield() {
        return pnoteInfield;
    }

    public void setPnoteInfield(String pnoteInfield) {
        this.pnoteInfield = pnoteInfield;
    }

    public String getPnoteOutfield() {
        return pnoteOutfield;
    }

    public void setPnoteOutfield(String pnoteOutfield) {
        this.pnoteOutfield = pnoteOutfield;
    }

    public String getPnoteThrow() {
        return pnoteThrow;
    }

    public void setPnoteThrow(String pnoteThrow) {
        this.pnoteThrow = pnoteThrow;
    }

    public String getPnoteArm() {
        return pnoteArm;
    }

    public void setPnoteArm(String pnoteArm) {
        this.pnoteArm = pnoteArm;
    }

    public String getPnoteSpeed() {
        return pnoteSpeed;
    }

    public void setPnoteSpeed(String pnoteSpeed) {
        this.pnoteSpeed = pnoteSpeed;
    }

    public String getPnoteBase() {
        return pnoteBase;
    }

    public void setPnoteBase(String pnoteBase) {
        this.pnoteBase = pnoteBase;
    }

    public int getCustom01() {
        return custom01;
    }

    public void setCustom01(int custom01) {
        this.custom01 = custom01;
    }

    public int getCustom02() {
        return custom02;
    }

    public void setCustom02(int custom02) {
        this.custom02 = custom02;
    }

    public int getCustom03() {
        return custom03;
    }

    public void setCustom03(int custom03) {
        this.custom03 = custom03;
    }

    public int getCustom04() {
        return custom04;
    }

    public void setCustom04(int custom04) {
        this.custom04 = custom04;
    }

    public int getCustom05() {
        return custom05;
    }

    public void setCustom05(int custom05) {
        this.custom05 = custom05;
    }

    public String getCustomNote01() {
        return customNote01;
    }

    public void setCustomNote01(String customNote01) {
        this.customNote01 = customNote01;
    }

    public String getCustomNote02() {
        return customNote02;
    }

    public void setCustomNote02(String customNote02) {
        this.customNote02 = customNote02;
    }

    public String getCustomNote03() {
        return customNote03;
    }

    public void setCustomNote03(String customNote03) {
        this.customNote03 = customNote03;
    }

    public String getCustomNote04() {
        return customNote04;
    }

    public void setCustomNote04(String customNote04) {
        this.customNote04 = customNote04;
    }

    public String getCustomNote05() {
        return customNote05;
    }

    public void setCustomNote05(String customNote05) {
        this.customNote05 = customNote05;
    }

    public boolean isCatcher() {
        return catcher;
    }

    public void setCatcher(boolean catcher) {
        this.catcher = catcher;
    }

    public boolean isPitcher() {
        return pitcher;
    }

    public void setPitcher(boolean pitcher) {
        this.pitcher = pitcher;
    }
}