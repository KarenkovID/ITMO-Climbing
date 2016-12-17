package ru.climbing.itmo.itmoclimbing.model;

import android.support.annotation.NonNull;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionsRoutesEntry {

    @NonNull
    public final String routeName;

    @NonNull
    public final String routeLevel;

    @NonNull
    public final int id;

    public CompetitionsRoutesEntry(@NonNull String routeName, @NonNull String routeLevel, @NonNull int id) {
        this.routeName = routeName;
        this.routeLevel = routeLevel;
        this.id = id;
    }
}
