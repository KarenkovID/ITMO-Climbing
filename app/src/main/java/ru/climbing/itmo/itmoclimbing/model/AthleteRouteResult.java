package ru.climbing.itmo.itmoclimbing.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthleteRouteResult {
    /**
     * Result ID
     */
    public final int resultID;
    /**
     * Athlete ID in Athletes table
     */
    public final int athleteID;
    /**
     * Route ID in RoutesTable
     */
    public final int routeID;
    /**
     * Remark of athlete result (like Redpoint, Flesh, Onsite)
     */
    @NonNull
    public final String remark;
    /**
     * Addititional coast to
     */
    @NonNull
    public final int remarkCost;

    @Nullable
    public String routeName;
    @Nullable
    public String routeGrade;

    public int routeCoast;

    public AthleteRouteResult (int resultID, int athleteID, int routeID, String remark, int remarkCost) {
        this.resultID = resultID;
        this.athleteID = athleteID;
        this.routeID = routeID;
        this.remark = remark;
        this.remarkCost = remarkCost;
    }
    public AthleteRouteResult (int resultID, int athleteID, int routeID,
                               String remark, int remarkCost,
                                String routeName, String routeGrade, int routeCoast) {
        this.resultID = resultID;
        this.athleteID = athleteID;
        this.routeID = routeID;
        this.remark = remark;
        this.remarkCost = remarkCost;
        this.routeName = routeName;
        this.routeGrade = routeGrade;
        this.routeCoast = routeCoast;
    }
}
