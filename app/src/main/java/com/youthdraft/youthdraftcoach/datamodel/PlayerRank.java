package com.youthdraft.youthdraftcoach.datamodel;

/**
 * Created by marty331 on 5/10/16.
 */
public class PlayerRank implements Comparable<PlayerRank> {

    String rowid;
    String first_name;
    String last_name;
    Double ranking;
    String draftable;
    int rawscore;
    String birth;

    public PlayerRank(String rowid, String first_name, String last_name, Double ranking, String draftable, int rawscore, String birth) {
        this.rowid = rowid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ranking = ranking;
        this.draftable = draftable;
        this.rawscore = rawscore;
        this.birth = birth;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Double getRanking() {
        return ranking;
    }

    public void setRanking(Double ranking) {
        this.ranking = ranking;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getDraftable() {
        return draftable;
    }

    public void setDraftable(String draftable) {
        this.draftable = draftable;
    }

    public int getRawscore() {
        return rawscore;
    }

    public void setRawscore(int rawscore) {
        this.rawscore = rawscore;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public int compareTo(PlayerRank another) {
        double compareRank = ((PlayerRank) another).getRanking();

        //ascending order
        //return this.quantity - compareQuantity;

        //descending order
        int i = (int)(compareRank + 0.5d) - (int)(this.getRanking()+ 0.5d);

        return i;
    }
}