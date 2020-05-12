package com.example.covid19;


public class States {
    private String loc;
    private int cases;

    public States(String loc, int cases) {
        this.loc = loc;
        this.cases = cases;
    }

    public String getLoc() {
        return loc;
    }

    public int getCases() {
        return cases;
    }
}
