package com.example.ex192.Objects;

import java.util.Calendar;

/**
 * Vaccine class:
 * saves data about each vaccine of a student - where it was taken and when.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 9/4/2024
 */
public class Vaccine {
    private String placeTaken;
    private long dateInMillis;

    public Vaccine(String placeTaken, long timeInMillis) {
        this.placeTaken = placeTaken;
        this.dateInMillis = timeInMillis;
    }

    public Vaccine() {
        this.placeTaken = null;
        this.dateInMillis = Calendar.getInstance().getTimeInMillis();
    }

    public Vaccine(Vaccine otherVaccine) {
        if(otherVaccine != null) {
            this.placeTaken = otherVaccine.placeTaken;
            this.dateInMillis = otherVaccine.dateInMillis;
        }
    }

    public String getPlaceTaken() {
        return this.placeTaken;
    }

    public long getDate() {
        return this.dateInMillis;
    }

    public void setPlaceTaken(String placeTaken) {
        this.placeTaken = placeTaken;
    }

    public void setDate(long timeInMillis) {
        this.dateInMillis = timeInMillis;
    }
}