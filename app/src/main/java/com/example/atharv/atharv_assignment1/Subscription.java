package com.example.atharv.atharv_assignment1;

/**
 * Created by Atharv on 2/4/2018.
 */

public class Subscription {
    private String name;
    private String charge;
    private String comment;
    private String startDate;

    public Subscription(String name, String charge, String startDate, String comment){ //enter params of input
       this.name = name;
       this.startDate = startDate;
       this.comment = comment;
       this.charge = charge;
    }

    public String getName(){
        return name;
    }

    public String getCharge(){
        return charge;
    }

    public String getComment(){
        return comment;
    }

    public String getStartDate(){
        return startDate;
    }
}
