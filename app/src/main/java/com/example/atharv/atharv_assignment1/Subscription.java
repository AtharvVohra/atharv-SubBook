package com.example.atharv.atharv_assignment1;

/** Subscription Class  - contains base schema for subscription object**/


public class Subscription {
    // Basic qualities of a subscription object

    private String name;
    private String charge;
    private String comment;
    private String startDate;

    public Subscription(String name, String charge, String startDate, String comment){
        // Constructor Method

       this.name = name;
       this.startDate = startDate;
       this.comment = comment;
       this.charge = charge;
    }

    // Getter functions for class objects

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
