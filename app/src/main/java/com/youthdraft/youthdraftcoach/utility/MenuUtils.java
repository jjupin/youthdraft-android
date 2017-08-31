package com.youthdraft.youthdraftcoach.utility;

import android.util.Log;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;

/**
 * Created by jjupin on 1/3/17.
 */

public class MenuUtils {

    public static String returnStringForPlayerSorting(int resid) {
        switch (resid) {
            case R.id.player_alphabetical_first:
                return "Alphabetical by First Name";  // need to put these into the String.xml file...
//
//            case R.id.player_alphabetical_last:
//                return "Alphabetical by Last Name";

            case R.id.player_bib_ascend:
                return "BIB Number (Ascending)";

            case R.id.player_bib_descend:
                return "BIB Number (Descending)";

            case R.id.player_hitting_mechanics:
                return "Hitting Mechanics";

            case R.id.player_bat_speed:
                return "Bat Speed";

            case R.id.player_infield_mechanics:
                return "Infield Mechanics";

            case R.id.player_outfield_mechanics:
                return "Outfield Mechanics";

            case R.id.player_throwing_mechanics:
                return "Throwing Mechanics";

            case R.id.player_fielding:
                return "Fielding";

            case R.id.player_speed:
                return "Speed";

            case R.id.player_base_running:
                return "Base Running";

//            case R.id.player_advanced:
//                return "Alphabetical by Last Name";
//
//            case R.id.player_ranking:
//                return "Ranking";

            default:
                return "Alphabetically (Last Name)";
        }
    }

    public static String returnStringForPlayerAssessmentSelection(int resid) {
        switch (resid) {
            case R.id.player_bib_ascend:
                return "BIB Number (Ascending)";

            case R.id.player_bib_descend:
                return "BIB Number (Descending)";

            case R.id.player_hitting_mechanics:
                return "Hitting Mechanics";

            case R.id.player_bat_speed:
                return "Bat Speed";

            case R.id.player_infield_mechanics:
                return "Infield Mechanics";

            case R.id.player_outfield_mechanics:
                return "Outfield Mechanics";

            case R.id.player_throwing_mechanics:
                return "Throwing Mechanics";

            case R.id.player_fielding:
                return "Fielding";

            case R.id.player_speed:
                return "Speed";

            case R.id.player_base_running:
                return "Base Running";

//            case R.id.player_advanced:
//                return "Alphabetical by Last Name";
//
//            case R.id.player_ranking:
//                return "Ranking";

            default:
                return "Hitting Mechanics";
        }

    }
}
