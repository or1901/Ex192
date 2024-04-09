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
    private Calendar date;

    public Vaccine(String placeTaken, Calendar date) {
        this.placeTaken = placeTaken;
        this.date = date;
    }

    public Vaccine() {
        placeTaken = "";
        date = Calendar.getInstance();
    }

    public String getPlaceTaken() {
        return this.placeTaken;
    }

    public Calendar getDate() {
        return this.date;
    }

    public void setPlaceTaken(String placeTaken) {
        this.placeTaken = placeTaken;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
