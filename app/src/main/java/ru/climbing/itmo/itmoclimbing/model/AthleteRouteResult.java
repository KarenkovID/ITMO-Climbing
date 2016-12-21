package ru.climbing.itmo.itmoclimbing.model;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthleteRouteResult {
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
    public final String remark;
    /**
     * Addititional coast to
     */
    public final int remarkCost;

    public AthleteRouteResult (int athleteID, int routeID, String remark, int remarkCost) {
        this.athleteID = athleteID;
        this.routeID = routeID;
        this.remark = remark;
        this.remarkCost = remarkCost;
    }
}
