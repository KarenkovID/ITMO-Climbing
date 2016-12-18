package ru.climbing.itmo.itmoclimbing.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class CompetitionsEntry {

    @NonNull
    public final String competitionName;

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
        this.competitionRoutes = competitionRoutes;
        this.competitors = competitors;

    }
}
