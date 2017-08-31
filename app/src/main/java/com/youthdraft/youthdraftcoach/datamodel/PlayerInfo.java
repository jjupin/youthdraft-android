package com.youthdraft.youthdraftcoach.datamodel;

/**
 * Created by marty331 on 1/26/16.
 */
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.youthdraft.youthdraftcoach.R;

import java.util.Comparator;

import static com.youthdraft.youthdraftcoach.datamodel.PlayerInfo.ComparatorField.BAT_SPEED;
import static com.youthdraft.youthdraftcoach.datamodel.PlayerInfo.ComparatorField.HEIGHT;
import static com.youthdraft.youthdraftcoach.datamodel.PlayerInfo.ComparatorField.HITTING_MECHANICS;
import static com.youthdraft.youthdraftcoach.datamodel.PlayerInfo.ComparatorField.INFIELD_MECHANICS;
import static com.youthdraft.youthdraftcoach.datamodel.PlayerInfo.ComparatorField.WEIGHT;

/**
 * Created by marty331 on 11/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerInfo implements Comparable<PlayerInfo> {

    private String birthday;
    private String playerid;
    private String firstname;
    private String middlename;
    private String lastname;
    private String tryout_date;
    private String tryout_time;
    private int jersey;
    private String priorlevel;
    private String priorteam;
    private String draftedTeam;
    private int leagueage;
    private String leagueid;
    private String bib;
    private String gender;
    private String sport;

    private boolean coachsKid = false;
    private boolean reschedule = false;
    private boolean drafted = false;

    @JsonIgnore
    private int height;
    @JsonIgnore
    private int weight;
    @JsonIgnore
    private int hitting;
    @JsonIgnore
    private int bat;
    @JsonIgnore
    private int infield;
    @JsonIgnore
    private int outfield;
    @JsonIgnore
    private int throwing;
    @JsonIgnore
    private int arm;
    @JsonIgnore
    private int speed;
    @JsonIgnore
    private int base;
    @JsonIgnore
    private double prank;

    @JsonIgnore
    private String noteHitting;
    @JsonIgnore
    private String noteBat;
    @JsonIgnore
    private String noteInfield;
    @JsonIgnore
    private String noteOutfield;
    @JsonIgnore
    private String noteThrowing;
    @JsonIgnore
    private String noteArm;
    @JsonIgnore
    private String noteSpeed;
    @JsonIgnore
    private String noteBase;

    @JsonIgnore
    private int compositeScore;

    @JsonIgnore
    private boolean pitcher = false;
    @JsonIgnore
    private boolean catcher = false;

    @JsonIgnore
    private boolean completedAssessment = false;  // based on the current coach's viewpoint - not necessarily from all the coaches...

    @JsonIgnore
    private int custom01;
    @JsonIgnore
    private int custom02;
    @JsonIgnore
    private int custom03;
    @JsonIgnore
    private int custom04;
    @JsonIgnore
    private int custom05;

    @JsonIgnore
    private String customNote01;
    @JsonIgnore
    private String customNote02;
    @JsonIgnore
    private String customNote03;
    @JsonIgnore
    private String customNote04;
    @JsonIgnore
    private String customNote05;

    public PlayerInfo() {
        // empty default constructor, necessary for Firebase to be able to deserialize player info objects...
    }

    public PlayerInfo(String birthday, String firstname, String gender, String lastname, String leagueid, String middlename, String playerid, String priorlevel, String priorteam, String tryout_date, String tryout_time) {
        this.birthday = birthday;
        this.firstname = firstname;
        this.gender = gender;
        this.lastname = lastname;
        this.leagueid = leagueid;
        this.middlename = middlename;
        this.playerid = playerid;
        this.priorlevel = priorlevel;
        this.priorteam = priorteam;

        this.tryout_date = tryout_date;
        this.tryout_time = tryout_time;

    }



    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getJersey() {
        return jersey;
    }

    public void setJersey(int jersey) {
        this.jersey = jersey;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getLeagueage() {
        return leagueage;
    }

    public void setLeagueage(int leagueage) {
        this.leagueage = leagueage;
    }

    public String getLeagueid() {
        return leagueid;
    }

    public void setLeagueid(String leagueid) {
        this.leagueid = leagueid;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getPriorlevel() {
        return priorlevel;
    }

    public void setPriorlevel(String priorlevel) {
        this.priorlevel = priorlevel;
    }

    public String getPriorteam() {
        return priorteam;
    }

    public void setPriorteam(String priorteam) {
        this.priorteam = priorteam;
    }

    public String getDraftedTeam() {
        return draftedTeam;
    }

    public void setDraftedTeam(String draftedTeam) {
        this.draftedTeam = draftedTeam;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTryout_date() {
        return tryout_date;
    }

    public void setTryout_date(String tryout_date) {
        this.tryout_date = tryout_date;
    }

    public String getTryout_time() {
        return tryout_time;
    }

    public void setTryout_time(String tryout_time) {
        this.tryout_time = tryout_time;
    }

    public double getPrank() {
        return prank;
    }

    public void setPrank(double prank) {
        if (prank <0){
            this.prank = 0.0;
        } else {
            this.prank = prank;
        }
    }

    public int getArm() {
        return arm;
    }

    public void setArm(int arm) {
        if (arm <0){
            this.arm = 0;
        } else {
            this.arm = arm;
        }

    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        if (base <0){
            this.base = 0;
        } else {
            this.base = base;
        }
    }

    public int getBat() {
        return bat;
    }

    public void setBat(int bat) {
        if (bat <0){
            this.bat = 0;
        } else {
            this.bat = bat;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height <0){
            this.height = 0;
        } else {
            this.height = height;
        }
    }

    public int getHitting() {
        return hitting;
    }

    public void setHitting(int hitting) {
        if (hitting <0){
            this.hitting = 0;
        } else {
            this.hitting = hitting;
        }
    }

    public int getInfield() {
        return infield;
    }

    public void setInfield(int infield) {
        if (infield <0){
            this.infield = 0;
        } else {
            this.infield = infield;
        }
    }

    public int getOutfield() {
        return outfield;
    }

    public void setOutfield(int outfield) {
        if (outfield <0){
            this.outfield = 0;
        } else {
            this.outfield = outfield;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed <0){
            this.speed = 0;
        } else {
            this.speed = speed;
        }
    }

    public int getThrowing() {
        return throwing;
    }

    public void setThrowing(int throwing) {
        if (throwing <0){
            this.throwing = 0;
        } else {
            this.throwing = throwing;
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight <0){
            this.weight = 0;
        } else {
            this.weight = weight;
        }

    }

    public String getNoteHitting() {
        return noteHitting;
    }

    public void setNoteHitting(String noteHitting) {
        this.noteHitting = noteHitting;
    }

    public String getNoteBat() {
        return noteBat;
    }

    public void setNoteBat(String noteBat) {
        this.noteBat = noteBat;
    }

    public String getNoteInfield() {
        return noteInfield;
    }

    public void setNoteInfield(String noteInfield) {
        this.noteInfield = noteInfield;
    }

    public String getNoteOutfield() {
        return noteOutfield;
    }

    public void setNoteOutfield(String noteOutfield) {
        this.noteOutfield = noteOutfield;
    }

    public String getNoteThrowing() {
        return noteThrowing;
    }

    public void setNoteThrowing(String noteThrowing) {
        this.noteThrowing = noteThrowing;
    }

    public String getNoteArm() {
        return noteArm;
    }

    public void setNoteArm(String noteArm) {
        this.noteArm = noteArm;
    }

    public String getNoteSpeed() {
        return noteSpeed;
    }

    public void setNoteSpeed(String noteSpeed) {
        this.noteSpeed = noteSpeed;
    }

    public String getNoteBase() {
        return noteBase;
    }

    public void setNoteBase(String noteBase) {
        this.noteBase = noteBase;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public String getFullname() {
        return firstname +" "+lastname;
    }
    public String getDateValues() {
        return tryout_date+" "+tryout_time;
    }

    public int getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(int compositeScore) {
        this.compositeScore = compositeScore;
    }

    public boolean isReschedule() {
        return reschedule;
    }

    public void setReschedule(boolean reschedule) {
        this.reschedule = reschedule;
    }

    public boolean isDrafted() {
        return drafted;
    }

    public void setDrafted(boolean drafted) {
        this.drafted = drafted;
    }

    public boolean hasCompletedAssessment() {
//        if (completedAssessment == false) {
//            return allAssessmentValuesFilledIn();
//        }
        return completedAssessment;
    }
    public void setCompletedAssessment(boolean completed) {
        this.completedAssessment = completed;
    }


    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public boolean isPitcher() {
        return pitcher;
    }

    public void setPitcher(boolean pitcher) {
        this.pitcher = pitcher;
    }

    public boolean isCatcher() {
        return catcher;
    }

    public void setCatcher(boolean catcher) {
        this.catcher = catcher;
    }

    public boolean isCoachsKid() {
        return coachsKid;
    }

    public void setCoachsKid(boolean coachsKid) {
        this.coachsKid = coachsKid;
    }

    public int getCustom01() {
        return custom01;
    }

    public void setCustom01(int custom01) {
        this.custom01 = custom01;
    }

    public String getCustomNote05() {
        return customNote05;
    }

    public void setCustomNote05(String customNote05) {
        this.customNote05 = customNote05;
    }

    public String getCustomNote04() {
        return customNote04;
    }

    public void setCustomNote04(String customNote04) {
        this.customNote04 = customNote04;
    }

    public String getCustomNote03() {
        return customNote03;
    }

    public void setCustomNote03(String customNote03) {
        this.customNote03 = customNote03;
    }

    public String getCustomNote02() {
        return customNote02;
    }

    public void setCustomNote02(String customNote02) {
        this.customNote02 = customNote02;
    }

    public String getCustomNote01() {
        return customNote01;
    }

    public void setCustomNote01(String customNote01) {
        this.customNote01 = customNote01;
    }

    public int getCustom05() {
        return custom05;
    }

    public void setCustom05(int custom05) {
        this.custom05 = custom05;
    }

    public int getCustom04() {
        return custom04;
    }

    public void setCustom04(int custom04) {
        this.custom04 = custom04;
    }

    public int getCustom03() {
        return custom03;
    }

    public void setCustom03(int custom03) {
        this.custom03 = custom03;
    }

    public int getCustom02() {
        return custom02;
    }

    public void setCustom02(int custom02) {
        this.custom02 = custom02;
    }

    public void updateCompositeScore() {
        int cScore = 0;
        //compositeScore += getHeight();
        //compositeScore += getWeight();
        cScore += getHitting();
        cScore += getBat();
        cScore += getInfield();
        cScore += getOutfield();
        cScore += getThrowing();
        cScore += getArm();
        cScore += getSpeed();
        cScore += getBase();

        setCompositeScore(cScore);
    }

    private static final int MIN_VALUE = 0;
    private boolean allAssessmentValuesFilledIn() {
        if ( getHitting() == MIN_VALUE) {
            return false;
        }
        if (getBat() == MIN_VALUE) {
            return false;
        }
        if (getInfield() == MIN_VALUE) {
            return false;
        }
        if (getOutfield() == MIN_VALUE) {
            return false;
        }
        if (getThrowing() == MIN_VALUE) {
            return false;
        }
        if (getArm() == MIN_VALUE) {
            return false;
        }
        if (getSpeed() == MIN_VALUE) {
            return false;
        }
        if (getBase() == MIN_VALUE) {
            return false;
        }

        return true;
    }

    public static enum ComparatorField {
        ALPHABETICAL_FIRST,
        ALPHABETICAL_LAST,
        BIB_ASCEND,
        BIB_DESCEND,
        HEIGHT,
        WEIGHT,
        HITTING_MECHANICS,
        BAT_SPEED,
        INFIELD_MECHANICS,
        OUTFIELD_MECHANICS,
        THROWING_MECHANICS,
        FIELDING,
        SPEED,
        BASE_RUNNING,
        RANKING,
        ADVANCED
    }

    private static ComparatorField currentSortField = ComparatorField.ALPHABETICAL_LAST;

    public static void setSortComparatorField(ComparatorField field) {
        currentSortField = field;
    }
    public static ComparatorField getSortComparatorField() {
        return currentSortField;
    }

    private static ComparatorField currentAssessField = HITTING_MECHANICS;

    public static void setAssessComparatorField(ComparatorField field) {
        currentAssessField = field;
    }
    public static ComparatorField getAssessComparatorField() {
        return currentAssessField;
    }

    private static String currentTimeslotSort = null;
    public static String getCurrentTimeslotSort() {
        return currentTimeslotSort;
    }
    public static void setCurrentTimeslotSort(String s) {
        currentTimeslotSort = s;
    }


    public static int getColumnForField(ComparatorField field) {

        if (field == null) {
            return 100;
        }

        switch (field) {
            //case HEIGHT:
            //    return 0;
            //case WEIGHT:
            //    return 1;
            case HITTING_MECHANICS:
                return 0;
            case BAT_SPEED:
                return 1;
            case INFIELD_MECHANICS:
                return 2;
            case OUTFIELD_MECHANICS:
                return 3;
            case THROWING_MECHANICS:
                return 4;
            case FIELDING:
                return 5;
            case SPEED:
                return 6;
            case BASE_RUNNING:
                return 7;
            default:
                return 100;
        }
    }

    public static void updateSortComparatorFieldBasedOnMenuSelection(int resid) {
        switch (resid) {
            case R.id.player_alphabetical_first:
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.ALPHABETICAL_FIRST);
                break;

//            case R.id.player_alphabetical_last:
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.ALPHABETICAL_LAST);
//                break;

            case R.id.player_bib_ascend:
                PlayerInfo.setSortComparatorField(ComparatorField.BIB_ASCEND);
                break;
            case R.id.player_bib_descend:
                PlayerInfo.setSortComparatorField(ComparatorField.BIB_DESCEND);
                break;

            case R.id.player_hitting_mechanics:
                PlayerInfo.setSortComparatorField(HITTING_MECHANICS);
                break;

            case R.id.player_bat_speed:
                PlayerInfo.setSortComparatorField(BAT_SPEED);
                break;

            case R.id.player_infield_mechanics:
                PlayerInfo.setSortComparatorField(INFIELD_MECHANICS);
                break;

            case R.id.player_outfield_mechanics:
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.OUTFIELD_MECHANICS);
                break;

            case R.id.player_throwing_mechanics:
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.THROWING_MECHANICS);
                break;

            case R.id.player_fielding:
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.FIELDING);
                break;

            case R.id.player_speed:
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.SPEED);
                break;

            case R.id.player_base_running:
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.BASE_RUNNING);
                break;

//            case R.id.player_advanced:
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.ADVANCED);
//                break;
//
//            case R.id.player_ranking:
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.RANKING);
//                break;

            default:
               PlayerInfo.setSortComparatorField(ComparatorField.ALPHABETICAL_LAST);
        }
    }

    public static void updateAssessComparatorFieldBasedOnMenuSelection(int resid) {
        switch (resid) {

            case R.id.player_hitting_mechanics:
                PlayerInfo.setAssessComparatorField(HITTING_MECHANICS);
                break;

            case R.id.player_bat_speed:
                PlayerInfo.setAssessComparatorField(BAT_SPEED);
                break;

            case R.id.player_infield_mechanics:
                PlayerInfo.setAssessComparatorField(INFIELD_MECHANICS);
                break;

            case R.id.player_outfield_mechanics:
                PlayerInfo.setAssessComparatorField(PlayerInfo.ComparatorField.OUTFIELD_MECHANICS);
                break;

            case R.id.player_throwing_mechanics:
                PlayerInfo.setAssessComparatorField(PlayerInfo.ComparatorField.THROWING_MECHANICS);
                break;

            case R.id.player_fielding:
                PlayerInfo.setAssessComparatorField(PlayerInfo.ComparatorField.FIELDING);
                break;

            case R.id.player_speed:
                PlayerInfo.setAssessComparatorField(PlayerInfo.ComparatorField.SPEED);
                break;

            case R.id.player_base_running:
                PlayerInfo.setAssessComparatorField(PlayerInfo.ComparatorField.BASE_RUNNING);
                break;

            default:
                PlayerInfo.setAssessComparatorField(HITTING_MECHANICS);
        }
    }

    public int getValueForField(ComparatorField field) {
        if (field == null) {
            return getCompositeScore();
        }

        switch (field) {
            case HEIGHT:
                return getHeight();
            case WEIGHT:
                return getWeight();
            case HITTING_MECHANICS:
                return getHitting();
            case BAT_SPEED:
                return getBat();
            case INFIELD_MECHANICS:
                return getInfield();
            case OUTFIELD_MECHANICS:
                return getOutfield();
            case THROWING_MECHANICS:
                return getThrowing();
            case FIELDING:
                return getArm();
            case SPEED:
                return getSpeed();
            case BASE_RUNNING:
                return getBase();
            case BIB_ASCEND:
            case BIB_DESCEND:
                return Integer.parseInt(TextUtils.isEmpty(getBib()) ? "0" : getBib());
            default:
                return getCompositeScore();
        }
    }

    public String getNoteForField(ComparatorField field) {
        if (field == null) {
            return "";
        }

        switch (field) {
            case HITTING_MECHANICS:
                return getNoteHitting();

            case BAT_SPEED:
                return getNoteBat();

            case INFIELD_MECHANICS:
                return getNoteInfield();

            case OUTFIELD_MECHANICS:
                return getNoteOutfield();

            case THROWING_MECHANICS:
                return getNoteThrowing();

            case FIELDING:
                return getNoteArm();

            case SPEED:
                return getNoteSpeed();

            case BASE_RUNNING:
                return getNoteBase();

            default:
                return "";
        }
    }

    public static String getTitleForField(ComparatorField field) {

        if (field == null) {
            return "Composite \n Score";
        }

        String s = "Composite \n Score";

        switch (field) {
            case HEIGHT:
                s = "Player \n Height";
                break;
            case WEIGHT:
                s = "Player \n Weight";
                break;
            case HITTING_MECHANICS:
                s = "Hitting \n Mechanics";
                break;
            case BAT_SPEED:
                s = "Bat \n Speed";
                break;
            case INFIELD_MECHANICS:
                s = "Infield \n Mechanics";
                break;
            case OUTFIELD_MECHANICS:
                s = "Outfield \n Mechanics";
                break;
            case THROWING_MECHANICS:
                s = "Throwing \n Mechanics";
                break;
            case FIELDING:
                s = "Player \n Fielding";
                break;
            case SPEED:
                s = "Player \n Speed";
                break;
            case BASE_RUNNING:
                s = "Base \n Running";
                break;
            default:
                s ="Composite \n Score";
        }

        return s;
    }

    public static String[] getFieldTitles() {
        String[] s = {
                "Player Height", "Player Weight", "Hitting Mechanics",
                "Bat Speed", "Infield Mechanics", "Outfield Mechanics",
                "Throwing Mechanics", "Player Fielding", "Player Speed",
                "Base Running", "Composite Score"
        };

                return s;
    }

    public int compareTo(PlayerInfo comparePlayer) {

        if (currentSortField == ComparatorField.ALPHABETICAL_FIRST) {
            return this.getFirstname().compareTo(comparePlayer.getFirstname());
        } else {
            return this.getLastname().compareTo(comparePlayer.getLastname());
        }
    }

    public static Comparator<PlayerInfo> PlayerSpeedComparator
            = new Comparator<PlayerInfo>() {

        public int compare(PlayerInfo player1, PlayerInfo player2) {

            int val1 = 0;
            int val2 = 0;

            switch (PlayerInfo.currentSortField) {
                case  HEIGHT:
                    val1 = player1.getHeight();
                    val2 = player2.getHeight();
                    break;

                case WEIGHT:
                    val1 = player1.getWeight();
                    val2 = player2.getWeight();
                    break;

                case HITTING_MECHANICS:
                    val1 = player1.getHitting();
                    val2 = player2.getHitting();
                    break;

                case BAT_SPEED:
                    val1 = player1.getBat();
                    val2 = player2.getBat();
                    break;

                case INFIELD_MECHANICS:
                    val1 = player1.getInfield();
                    val2 = player2.getInfield();
                    break;

                case OUTFIELD_MECHANICS:
                    val1 = player1.getOutfield();
                    val2 = player2.getOutfield();
                    break;

                case THROWING_MECHANICS:
                    val1 = player1.getThrowing();
                    val2 = player2.getThrowing();
                    break;

                case FIELDING:
                    val1 = player1.getArm();
                    val2 = player2.getArm();
                    break;

                case SPEED:
                    val1 = player1.getSpeed();
                    val2 = player2.getSpeed();
                    break;

                case BASE_RUNNING:
                    val1 = player1.getBase();
                    val2 = player2.getBase();
                    break;
                case RANKING:
                    val1 = player1.getCompositeScore();
                    val2 = player2.getCompositeScore();
                    break;
                case BIB_DESCEND:
                    val1 = Integer.parseInt(TextUtils.isEmpty(player1.getBib()) ? "0" : player1.getBib());
                    val2 = Integer.parseInt(TextUtils.isEmpty(player2.getBib()) ? "0" : player2.getBib());
                    break;
                case BIB_ASCEND:
                    val2 = Integer.parseInt(TextUtils.isEmpty(player1.getBib()) ? "0" : player1.getBib());
                    val1 = Integer.parseInt(TextUtils.isEmpty(player2.getBib()) ? "0" : player2.getBib());
                    break;

                default:

                    val1 = 0;
                    val2 = 0;
            }

            //ascending order
            int compareValue = Integer.valueOf(val2).compareTo(Integer.valueOf(val1));
            if (compareValue != 0) {
                return compareValue;
            } else if (PlayerInfo.currentSortField == ComparatorField.RANKING) {
                return Integer.valueOf(player2.getCompositeScore()).compareTo(Integer.valueOf(player1.getCompositeScore()));
            } else if (PlayerInfo.currentSortField == ComparatorField.ALPHABETICAL_FIRST) {
                return player1.getFirstname().compareTo(player2.getFirstname());
            } else {
                return player1.getLastname().compareTo(player2.getLastname());
            }

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
}