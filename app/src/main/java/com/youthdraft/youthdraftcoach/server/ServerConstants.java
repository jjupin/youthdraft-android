package com.youthdraft.youthdraftcoach.server;

/**
 * Created by jjupin on 11/21/16.
 */

public class ServerConstants {
    public enum ServerVersionType {
        qa("qa"),
        beta("beta"),
        staging("dl"),
        production("www"),
        hotfix("hotfix");

        private String value;

        ServerVersionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // https://dl.dropboxusercontent.com/u/2832657/youthdraft/players.json

    public enum RestfulActions {
        signup("connections/mobile/signup"),
        login("connections/mobile/signin"),
        import_contacts("connections/mobile/android.json"),
        import_call_log("connections/mobile/android.json"),
        caller_id("connections/mobile/android/incoming_call?phone_number="),
        import_progress("connections/import_progress.json"),
        get_telemarketer("connections/mobile/telemarketer_info?phone_query="),
        is_telemarketer("..."),
        get_contacts("connections.json"),
        get_activities("connections/activities.json?id="),
        mark_as_read("connections/mark_as_read.json?id="),
        delete_contact("connections/connection?id="),
        people_picker("connections/get_matches.json?id="), // no longer works
        search("v9/name/search.json"), // people picker results
        select_match("connections/select_match.json"),
        get_connection_info("connections/connection?id="),
        neighborhood_info("connections/mobile/neighborhood_info?"), //lat,long,max_limit
        updates("profile/updates?id="), //connection id
        address_coordinates("connections/mobile/get_gps_data?address="), //199+S+Los+Robles+Ave+Pasadena+CA+91101

        get_players("youthdraft/players.json");

        private String value;

        RestfulActions(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static int SERVER_UPGRADE_STATUS_CODE = 426;  // Jorge chose to use this value...
    private static boolean deprecated = false;

    private ServerConstants() {
    } // this class is a singleton...

    public static final String CONNECT_SERVER = "dropboxusercontent.com/u/2832657";
    private static ServerVersionType currentType = ServerVersionType.staging;

    public static String getServerVersion() {
        return currentType.getValue();
    }

    public static String getRestfulfServer() {
        return getServerVersion() + "." + CONNECT_SERVER;
    }

    public static String getRestfulActionURL(RestfulActions action) {
        if (action == RestfulActions.search || action == RestfulActions.updates) {
            return "http://" + ServerConstants.getRestfulfServer() + "/" + action.getValue();
        }
        return "https://" + ServerConstants.getRestfulfServer() + "/" + action.getValue();
    }

    public static boolean isDeprecated() {
        return deprecated;
    }

    public static void setDeprecated(int statusCode) {
        deprecated = (statusCode == SERVER_UPGRADE_STATUS_CODE);
    }

    public static void setDeprecated(boolean b) {
        deprecated = b;
    }

}

