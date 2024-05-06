package com.example.myapplication;

import java.io.Serializable;

public class Rank implements Serializable {
    private String rank;
    private String zrs;

    public Rank(String rank, String zrs) {
        this.rank = rank;
        this.zrs = zrs;
    }
    public String getRank() {
        return rank;
    }
    public String getZrs() {
        return zrs;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public void setZrs(String zrs) {
        this.zrs = zrs;
    }
}
