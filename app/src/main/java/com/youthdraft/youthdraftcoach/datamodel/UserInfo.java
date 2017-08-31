package com.youthdraft.youthdraftcoach.datamodel;

/**
 * Created by marty331 on 1/25/16.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {

    private String email;
    private boolean isCoach;
    private String league;
    private String provider;
    private String sport;
    private String token;
    private String season;
    private String year;

    public UserInfo(){

    }


    public UserInfo(String email, boolean isCoach, String league, String provider, String sport, String token, String season, String year){
        this.email = email;
        this.isCoach = isCoach;
        this.league = league;
        this.provider = provider;
        this.sport = sport;
        this.token = token;
        this.season = season;
        this.year = year;
    }


    public boolean isCoach() {
        return isCoach;
    }

    public String getEmail() {
        return email;
    }

    public String getLeague() {
        return league;
    }

    public String getProvider() {
        return provider;
    }

    public String getSport() {
        return sport;
    }

    public String getToken() {
        return token;
    }

    public String getSeason() {
        return season;
    }

    public String getYear() {
        return year;
    }
}
