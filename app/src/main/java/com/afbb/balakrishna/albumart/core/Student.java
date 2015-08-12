package com.afbb.balakrishna.albumart.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by balakrishna on 12/8/15.
 */
public class Student implements Parcelable {
    private String branch;
    private String name;
    private  double rank;

    public Student(String branch, String name, double rank) {
        this.branch = branch;
        this.name = name;
        this.rank = rank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.branch);
        dest.writeString(this.name);
        dest.writeDouble(this.rank);
    }


    protected Student(Parcel in) {
        this.branch = in.readString();
        this.name = in.readString();
        this.rank = in.readDouble();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
