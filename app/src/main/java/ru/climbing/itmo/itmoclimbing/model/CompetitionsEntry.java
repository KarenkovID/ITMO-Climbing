package ru.climbing.itmo.itmoclimbing.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionsEntry {

    @NonNull
    public final String competitionName;

    @NonNull
    public final String competitionType;

    @NonNull
    public final Calendar competitionTime;

    @NonNull
    public final String isActive;

    @NonNull
    ArrayList<CompetitionsRoutesEntry> competitionsRoutes;

    @NonNull
    ArrayList<CompetitorEntry> competitors;

    public CompetitionsEntry(@NonNull String competitionName,
                             @NonNull String competitionType,
                             @NonNull Calendar competitionTime,
                             @NonNull String isActive,
                             ArrayList<CompetitionsRoutesEntry> competitionsRoutes,
                             ArrayList<CompetitorEntry> competitors) {
        this.competitionName = competitionName;
        this.competitionType = competitionType;
        this.competitionTime = competitionTime;
        this.isActive = isActive;
        this.competitionsRoutes = competitionsRoutes;
        this.competitors = competitors;

    }


}
