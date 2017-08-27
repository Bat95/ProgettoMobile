package com.myrecipebook.myrecipebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sonia on 11/08/17.
 */

public class Recipe implements Parcelable {

    String name;
    String mainPic;
    int dosePerPerson;
    List<String> steps;
    List<String> ingredients;
    int difficulty;
    int duration;
    int category;
    List<String> tag;

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        mainPic = in.readString();
        dosePerPerson = in.readInt();

        if (in.readByte() == 0x01) {
            steps = new ArrayList<String>();
            in.readList(steps, String.class.getClassLoader());
        } else {
            steps = null;
        }

        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<String>();
            in.readList(ingredients, String.class.getClassLoader());
        } else {
            ingredients = null;
        }

        difficulty = in.readInt();
        duration = in.readInt();
        category = in.readInt();

        if (in.readByte() == 0x01) {
            tag = new ArrayList<String>();
            in.readList(tag, String.class.getClassLoader());
        } else {
            tag = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mainPic);
        dest.writeInt(dosePerPerson);
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }

        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        dest.writeInt(difficulty);
        dest.writeInt(duration);
        dest.writeInt(category);

        if (tag == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tag);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}