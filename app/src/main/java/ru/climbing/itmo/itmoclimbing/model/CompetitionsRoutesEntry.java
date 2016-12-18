package ru.climbing.itmo.itmoclimbing.model;

import android.support.annotation.NonNull;

public class CompetitionsRoutesEntry {

    @NonNull
    public final String routeName;

    @NonNull
    public double routeFactor;

    @NonNull
    public int id;

    @NonNull
    public int count;

    public CompetitionsRoutesEntry(@NonNull String routeName, @NonNull double routeFactor, @NonNull int id, @NonNull int count) {
        this.routeName = routeName;
        this.routeFactor = routeFactor;
        this.id = id;
        this.count = count;
    }
}
