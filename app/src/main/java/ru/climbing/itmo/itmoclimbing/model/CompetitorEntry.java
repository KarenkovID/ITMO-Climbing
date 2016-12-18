package ru.climbing.itmo.itmoclimbing.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitorEntry {

    public static class CompetitorsRouteData{

        private CompetitionsRoutesEntry routesEntry;
        private int startPosition;
        private int result;

        public CompetitorsRouteData(CompetitionsRoutesEntry routesEntry,
                                    int startPosition, int result) {
            this.routesEntry = routesEntry;
            this.startPosition = startPosition;
            this.result = result;
        }

        public CompetitionsRoutesEntry getRoutesEntry() {
            return routesEntry;
        }

        public void setRoutesEntry(CompetitionsRoutesEntry routesEntry) {
            this.routesEntry = routesEntry;
        }

        public int getStartPosition() {
            return startPosition;
        }

        public void setStartPosition(int startPosition) {
            this.startPosition = startPosition;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }

    @NonNull
    public final String competitorName;

    @NonNull
    public ArrayList<CompetitorsRouteData> competitorsRouteData;

    public CompetitorEntry(@NonNull String competitorName,
                           @NonNull ArrayList<CompetitorsRouteData> competitorsRouteData) {
        this.competitorName = competitorName;
        this.competitorsRouteData = competitorsRouteData;
    }
}
