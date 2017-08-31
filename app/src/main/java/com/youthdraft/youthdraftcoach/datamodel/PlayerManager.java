package com.youthdraft.youthdraftcoach.datamodel;

import android.text.TextUtils;
import android.util.Log;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.utility.DataUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jjupin on 12/2/16.
 */

public class PlayerManager {

    private static List<PlayerInfo> players = new ArrayList<PlayerInfo>();
    private static Map<String, Integer> playerDBPositionMap = new HashMap<String, Integer>();
    private static Map<String, PlayerInfo> playersMap = new HashMap<String, PlayerInfo>();
    private static Map<String, Map<String, PlayerScores>> playersScoreMap = new HashMap<String, Map<String, PlayerScores>>();
    private static Map<PlayerInfo, PlayerScores> playerInfoToCurrentSwapScoreMap = new HashMap<PlayerInfo, PlayerScores>();
    private static Map<String, List<String>> timeslotMap = new HashMap<String, List<String>>();
    private static Map<String, String> timeslotToSecondsMap = new HashMap<String, String>();

    private static List<PlayerInfo> myTeamPlayers = new ArrayList<PlayerInfo>();
    public static String currentTimeslot = "";

    public static void clearCurrentData() {
        players = new ArrayList<PlayerInfo>();
        playersMap = new HashMap<String, PlayerInfo>();
        playerDBPositionMap = new HashMap<String, Integer>();
        playersScoreMap = new HashMap<String, Map<String, PlayerScores>>();
        playerInfoToCurrentSwapScoreMap = new HashMap<PlayerInfo, PlayerScores>();
        timeslotMap = new HashMap<String, List<String>>();
        timeslotToSecondsMap = new HashMap<String, String>();
    }
    public static void clearTimeSlots() {
        timeslotMap = new HashMap<String, List<String>>();
        timeslotToSecondsMap = new HashMap<String, String>();
    }

    public static void addPlayer(PlayerInfo player, int position, String coachid) {

        if (playerDBPositionMap.containsKey(player.getLeagueid()) == false) {
            players.add(player);
            playersMap.put(player.getLeagueid(), player);
            playerDBPositionMap.put(player.getLeagueid(), position);
        } else {
            PlayerInfo oldPlayer = playersMap.get(player.getLeagueid());
            DataUtils.copyPlayerInfoIntoAnotherPlayerInfo(oldPlayer, player);
        }

        String timeslot = player.getTryout_date()+" \n "+player.getTryout_time();
        Date convertedToDate = getDateForTimeSlot(player.getTryout_date(), player.getTryout_time());
        if (convertedToDate == null) {
            Log.e("Hey Now!", "What's the matter with your name now");
        }
        if (timeslotToSecondsMap.containsKey(convertedToDate.getTime()+"") == false) {
            timeslotMap.put(timeslot, new ArrayList<String>());
            timeslotToSecondsMap.put(convertedToDate.getTime()+"", timeslot);
        }
        List<String> members = timeslotMap.get(timeslot);
        members.add(player.getLeagueid());
        timeslotMap.put(timeslot, members);  // might want to sort them first - but get to that later....

        //
        // Now, update the playerInfo to have the scores from that particular coach (if the score exists)...
        //

        if (playersMap.containsKey(player.getLeagueid())) {
            Map<String, PlayerScores> scores = playersScoreMap.get(player.getLeagueid());
            if (scores != null && scores.containsKey(coachid)) {
                PlayerScores score = scores.get(coachid);
                swapInPlayerScoresForPlayerInfo(player, score);
                playerInfoToCurrentSwapScoreMap.put(player, score);
            }
        }
    }

    public static PlayerInfo getPlayerForLeagueId(String leagueId) {
        if (playersMap.containsKey(leagueId))
            return playersMap.get(leagueId);

        return null;
    }

    public static Map<String, List<String>> recomputeTimeslots() {
        clearTimeSlots();
        List<PlayerInfo> players = getAllPlayers();
        for (PlayerInfo player : players) {
            if (player.hasCompletedAssessment() == false) {
                String timeslot = player.getTryout_date()+" \n "+player.getTryout_time();
                Date convertedToDate = getDateForTimeSlot(player.getTryout_date(), player.getTryout_time());
                if (timeslotToSecondsMap.containsKey(convertedToDate.getTime()+"") == false) {
                    timeslotMap.put(timeslot, new ArrayList<String>());
                    timeslotToSecondsMap.put(convertedToDate.getTime()+"", timeslot);
                }
                List<String> members = timeslotMap.get(timeslot);
                members.add(player.getLeagueid());
                timeslotMap.put(timeslot, members);

            }
        }

        return timeslotMap;
    }

    public static int getPlayerDBPosiiton(PlayerInfo player) {
        if (playerDBPositionMap.containsKey(player.getLeagueid())) {
            return playerDBPositionMap.get(player.getLeagueid());
        }

        return -1;
    }

    public static int getHighestDBPosition() {
        int highest = 0;
        for (int val : playerDBPositionMap.values()) {
            if (val > highest) {
                highest = val;
            }
        }

        return highest;
    }

    public static Date getDateForTimeSlot(String datepart, String timepart) {
        try{
            if (datepart.indexOf("/") != 2) {
                datepart = "0"+datepart;
            }
            if (timepart.indexOf(":") != 2) {
                timepart = "0"+timepart;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa"); // here set the pattern as you date in string was containing like date/month/year
            Date d = sdf.parse(datepart + " " + timepart.toLowerCase());

            return d;
        } catch (Exception ex){
            Log.e("PlayerManager", "parse exception: " + ex.toString());
        }

        return null;
    }

    public static void addPlayerScore(PlayerScores newScore, String coachid) {
        Map<String, PlayerScores> scores = new HashMap<String,PlayerScores>();
        if (playersScoreMap.containsKey(newScore.getPlayerid())) {
            scores = playersScoreMap.get(newScore.getPlayerid());
        }

        scores.put(coachid, newScore);

        playersScoreMap.put(newScore.getPlayerid(), scores);

        //
        // doing a bit of a cheat here to put the latest scores into the playerInfo
        // object - so as to keep track of the immediate one...
        //

        if (playersMap.containsKey(newScore.getPlayerid())) {
            PlayerInfo info = playersMap.get(newScore.getPlayerid());
            swapInPlayerScoresForPlayerInfo(info, newScore);
            playerInfoToCurrentSwapScoreMap.put(info, newScore);

        }
    }

    public static Map<String, PlayerScores> getPlayerScoresForCoachId(String coachid) {
        if (playersScoreMap.containsKey(coachid)) {
            return playersScoreMap.get(coachid);
        }

        return null;
    }

    public static PlayerScores getCurrentSwapScoresForPlayer(PlayerInfo info) {
        if (playerInfoToCurrentSwapScoreMap.containsKey(info) == false) {
            return new PlayerScores();
        } else {
            return playerInfoToCurrentSwapScoreMap.get(info);
        }
    }

    public static void swapInPlayerScoresForPlayerInfo(PlayerInfo playerInfo, PlayerScores scores) {
        playerInfo.setArm(scores.getParm());
        playerInfo.setBase(scores.getPbase());
        playerInfo.setBat(scores.getPbat());
        playerInfo.setHeight(scores.getPheight());
        playerInfo.setWeight(scores.getPweight());
        playerInfo.setHitting(scores.getPhit());
        playerInfo.setInfield(scores.getPinfield());
        playerInfo.setOutfield(scores.getPoutfield());
        playerInfo.setSpeed(scores.getPspeed());
        playerInfo.setThrowing(scores.getPthrow());
        playerInfo.setPrank(scores.getPrank());

        playerInfo.setNoteArm(scores.getPnoteArm());
        playerInfo.setNoteBase(scores.getPnoteBase());
        playerInfo.setNoteBat(scores.getPnoteBat());
        playerInfo.setNoteHitting(scores.getPnoteHit());
        playerInfo.setNoteInfield(scores.getPnoteInfield());
        playerInfo.setNoteOutfield(scores.getPnoteOutfield());
        playerInfo.setNoteSpeed(scores.getPnoteSpeed());
        playerInfo.setNoteThrowing(scores.getPnoteThrow());

        playerInfo.setPitcher(scores.isPitcher());
        playerInfo.setCatcher(scores.isCatcher());

        playerInfo.setCustom01(scores.getCustom01());
        playerInfo.setCustom02(scores.getCustom02());
        playerInfo.setCustom03(scores.getCustom03());
        playerInfo.setCustom04(scores.getCustom04());
        playerInfo.setCustom05(scores.getCustom05());

        playerInfo.setCustomNote01(scores.getCustomNote01());
        playerInfo.setCustomNote02(scores.getCustomNote02());
        playerInfo.setCustomNote03(scores.getCustomNote03());
        playerInfo.setCustomNote04(scores.getCustomNote04());
        playerInfo.setCustomNote05(scores.getCustomNote05());

        playerInfo.setCompletedAssessment(scores.hasCompletedAssessment());


        playerInfo.updateCompositeScore();
    }

    public static void addPlayerToMyTeam(PlayerInfo player) {
        myTeamPlayers.add(player);
    }

    public static List<PlayerInfo> getAllPlayers() {
        return players;
    }

    public static List<PlayerInfo> getPlayersExceptForRescheduled() {
        List<PlayerInfo> notRescheduled = new ArrayList<PlayerInfo>();
        for (PlayerInfo player : players) {
            if (player.isReschedule() == false) {
                notRescheduled.add(player);
            }
        }

        return notRescheduled;
    }

    public static List<PlayerInfo> getPlayersExceptForRescheduledAndAssessed() {
        List<PlayerInfo> needAssessment = new ArrayList<PlayerInfo>();
        for (PlayerInfo player : players) {
            if (player.isReschedule() == false && player.hasCompletedAssessment() == false) {
                needAssessment.add(player);
            }
        }

        return needAssessment;
    }

    public static List<PlayerInfo> getDraftedPlayers() {
        List<PlayerInfo> drafted = new ArrayList<PlayerInfo>();
        for (PlayerInfo player : players) {
            if (player.isDrafted()) {
                drafted.add(player);
            }
        }

        return drafted;
    }

    public static List<PlayerInfo> getPlayersToReschedule() {
        List<PlayerInfo> rescheduled = new ArrayList<PlayerInfo>();
        for (PlayerInfo player : players) {
            if (player.isReschedule()) {
                rescheduled.add(player);
            }
        }

        return rescheduled;
    }

    public static List<PlayerInfo> getAssessedPlayers(String coachID) {
        List<PlayerInfo> assessed = new ArrayList<PlayerInfo>();
        for (PlayerInfo player : players) {
            if (player.hasCompletedAssessment()) { // assume he's been assessed...
                assessed.add(player);
            }
        }

        return assessed;
    }

    public static List<PlayerInfo> getMyTeamPlayers() {
        return myTeamPlayers;
    }

    public static List<PlayerInfo> getPlayersForTimeslot(String timeslot) {
        currentTimeslot = timeslot;
        List<String> found = null;
        List<String> remove = new ArrayList<String>();

        if (timeslot.toLowerCase().indexOf("all") > -1) {
            return getPlayersExceptForRescheduled();
        } else {
            if (timeslotToSecondsMap.containsKey(timeslot)) {
                found = timeslotMap.get(timeslotToSecondsMap.get(timeslot));

            } else if (timeslotMap.containsKey(timeslot)) {
                found = timeslotMap.get(timeslot);
            }

            if (found != null) {
                List<PlayerInfo> players = new ArrayList<PlayerInfo>();

                for (String leagueid : found) {
                    PlayerInfo player = playersMap.get(leagueid);
                    if (player.hasCompletedAssessment() || player.isReschedule()) {
                        remove.add(leagueid);
                    } else {
                        players.add(player);
                    }
                }
                return players;
            }
        }

        return null;
    }

    public static boolean bibNumberExists(String bib) {
        if (bib == null || bib.length() == 0) {
            return false;
        }

        String lower = bib.toLowerCase();
        List<PlayerInfo> players = getAllPlayers();
        for (PlayerInfo player : players) {
            if (TextUtils.isEmpty(player.getBib()) == false && player.getBib().toLowerCase().contentEquals(lower)) {
                return true;
            }
        }

        return false;
    }

    public static boolean leagueIdExistsForPlayer(String playerId) {
        if (playerId == null || playerId.length() == 0) {
            return false;
        }

        String lower = playerId.toLowerCase();
        List<PlayerInfo> players = getAllPlayers();
        for (PlayerInfo player : players) {
            if (player.getLeagueid() != null && player.getLeagueid().toLowerCase().contentEquals(lower)) {
                return true;
            }
        }

        return false;
    }

    public static Map<String, PlayerInfo> getPlayersMap() {
        return playersMap;
    }
    public static Map<String, Map<String, PlayerScores>> getPlayerScores() {
        return playersScoreMap;
    }

    public static Map<String, String> getTimeslotToSecondsMap() { return timeslotToSecondsMap; }
    public static Map<String, List<String>> getTimeslotMap() {
        return timeslotMap;
    }


}
