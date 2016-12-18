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
    public  ArrayList<CompetitionsRoutesEntry> competitionRoutes;

    @NonNull
    public ArrayList<CompetitorEntry> competitors;

    public CompetitionsEntry(@NonNull String competitionName,
                             @NonNull String competitionType,
                             boolean isActive,
                             @Nullable ArrayList<CompetitionsRoutesEntry> competitionRoutes,
                             @Nullable ArrayList<CompetitorEntry> competitors) {
        this.competitionName = competitionName;
        this.competitionType = competitionType;
        this.isActive = isActive;
        this.competitionRoutes = competitionRoutes;
        this.competitors = competitors;
    }


}
