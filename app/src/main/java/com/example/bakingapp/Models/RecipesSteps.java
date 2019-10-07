package com.example.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipesSteps extends Recipes implements Parcelable {
    private String sId;
    private String sDescription;
    private String sInstructions;
    private String sVideoUrl;

    public RecipesSteps(String sId, String sDescription, String sVideoUrl, String sInstructions) {
        this.sId = sId;
        this.sDescription = sDescription;
        this.sVideoUrl = sVideoUrl;
        this.sInstructions = sInstructions;
    }

    private RecipesSteps(Parcel in) {
        sId = in.readString();
        sDescription = in.readString();
        sVideoUrl = in.readString();
        sInstructions = in.readString();
    }

    public static final Creator<RecipesSteps> CREATOR = new Creator<RecipesSteps>() {
        @Override
        public RecipesSteps createFromParcel(Parcel in) {
            return new RecipesSteps(in);
        }

        @Override
        public RecipesSteps[] newArray(int size) {
            return new RecipesSteps[size];
        }
    };

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsDescription() {
        return sDescription;
    }

    public void setsDescription(String sDescription) {
        this.sDescription = sDescription;
    }

    public String getsVideoUrl() {
        return sVideoUrl;
    }

    public void setsVideoUrl(String sVideoUrl) {
        this.sVideoUrl = sVideoUrl;
    }

    public String getsInstructions() {
        return sInstructions;
    }

    public void setsInstructions(String sInstructions) {
        this.sInstructions = sInstructions;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sId);
        parcel.writeString(sDescription);
        parcel.writeString(sVideoUrl);
        parcel.writeString(sInstructions);
    }
}
