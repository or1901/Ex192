package com.example.ex192.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * Vaccine class:
 * saves data about each vaccine of a student - where it was taken and when.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 9/4/2024
 */
public class Vaccine implements Parcelable {
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

    // Parcelable implementation
    protected Vaccine(Parcel in) {
        placeTaken = in.readString();
        dateInMillis = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(placeTaken);
        parcel.writeLong(dateInMillis);
    }

    public static final Creator<Vaccine> CREATOR = new Creator<Vaccine>() {
        @Override
        public Vaccine createFromParcel(Parcel in) {
            return new Vaccine(in);
        }

        @Override
        public Vaccine[] newArray(int size) {
            return new Vaccine[size];
        }
    };
}