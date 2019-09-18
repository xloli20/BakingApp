package com.example.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipesIngredients extends Recipes implements Parcelable {
    private String rIngredient;
    private String iQuantity;
    private String iMeasure;

    public RecipesIngredients(String rIngredient, String iQuantity, String iMeasure) {
        super();
        this.rIngredient = rIngredient;
        this.iQuantity = iQuantity;
        this.iMeasure = iMeasure;
    }

    protected RecipesIngredients(Parcel in) {
        rIngredient = in.readString();
        iQuantity = in.readString();
        iMeasure = in.readString();
    }

    public static final Creator<RecipesIngredients> CREATOR = new Creator<RecipesIngredients>() {
        @Override
        public RecipesIngredients createFromParcel(Parcel in) {
            return new RecipesIngredients(in);
        }

        @Override
        public RecipesIngredients[] newArray(int size) {
            return new RecipesIngredients[size];
        }
    };

    public String getrIngredient() {
        return rIngredient;
    }

    public void setrIngredient(String rIngredient) {
        this.rIngredient = rIngredient;
    }

    public String getiQuantity() {
        return iQuantity;
    }

    public void setiQuantity(String iQuantity) {
        this.iQuantity = iQuantity;
    }

    public String getiMeasure() {
        return iMeasure;
    }

    public void setiMeasure(String iMeasure) {
        this.iMeasure = iMeasure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rIngredient);
        parcel.writeString(iQuantity);
        parcel.writeString(iMeasure);
    }
}
