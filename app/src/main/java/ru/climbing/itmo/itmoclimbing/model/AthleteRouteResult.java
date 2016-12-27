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
    public int resultID = -1;
    /**
     * Athlete ID in Athletes table
     */
    public final int athleteID;
    /**
     * Route ID in RoutesTable
     */
    public int routeID = -1;
    /**
     * Remark of athlete result (like Redpoint, Flesh, Onsite)
     */
    @NonNull
    public final String remark;
    /**
     * Addititional coast to
     */
    public int remarkCost = -1;

    @Nullable
    public String routeName;
    @Nullable
    public String routeGrade;

    public int routeCoast;
    @Nullable
    public String athleteName;

    public AthleteRouteResult (int resultID, int athleteID, int routeID, String remark, int remarkCost) {
        this.resultID = resultID;
        this.athleteID = athleteID;
        this.routeID = routeID;
        this.remark = remark;
        this.remarkCost = remarkCost;
    }
    public AthleteRouteResult (int athleteID, int routeID, String remark, String athleteName) {
        this.athleteID = athleteID;
        this.routeID = routeID;
        this.remark = remark;
        this.athleteName = athleteName;
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
