package com.example.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipes implements Parcelable {
    private String rName;
    private String rId;

    public Recipes() {
    }

    public Recipes(String rName, String rId) {
        this.rName = rName;
        this.rId = rId;
    }

    protected Recipes(Parcel in) {
        rName = in.readString();
        rId = in.readString();
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rName);
        parcel.writeString(rId);
    }
}
