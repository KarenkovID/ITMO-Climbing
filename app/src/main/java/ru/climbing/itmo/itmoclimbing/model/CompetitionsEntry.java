package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class CompetitionsEntry implements Parcelable{

    @NonNull
    public String competitionName;

    @NonNull
    public String competitionType;

    @NonNull
    public boolean isActive;

    @NonNull
    public  ArrayList<String> competitionRoutes;

    @NonNull
    public ArrayList<String> competitors;

    public CompetitionsEntry(@NonNull String competitionName,
                             @NonNull String competitionType,
                             boolean isActive,
                             @Nullable ArrayList<String> competitionRoutes,
                             @Nullable ArrayList<String> competitors) {
        this.competitionName = competitionName;
        this.competitionType = competitionType;
        this.isActive = isActive;
        if (competitionRoutes == null) {
            this.competitionRoutes = new ArrayList<String>();
        } else {
            this.competitionRoutes = competitionRoutes;
        }
        if (competitors == null) {
            this.competitors = new ArrayList<String>();
        } else {
            this.competitors = competitors;
        }

    }

    protected CompetitionsEntry(Parcel in) {
        competitionName = in.readString();
        competitionType = in.readString();
        isActive = in.readByte() != 0;
        competitionRoutes = in.createStringArrayList();
        competitors = in.createStringArrayList();
    }

    public static final Creator<CompetitionsEntry> CREATOR = new Creator<CompetitionsEntry>() {
        @Override
        public CompetitionsEntry createFromParcel(Parcel in) {
            return new CompetitionsEntry(in);
        }

        @Override
        public CompetitionsEntry[] newArray(int size) {
            return new CompetitionsEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(competitionName);
        dest.writeString(competitionType);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeStringList(competitionRoutes);
        dest.writeStringList(competitors);
    }
}
