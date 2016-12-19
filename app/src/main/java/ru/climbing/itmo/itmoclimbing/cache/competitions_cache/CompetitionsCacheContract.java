package ru.climbing.itmo.itmoclimbing.cache.competitions_cache;

import android.provider.BaseColumns;

/**
 * Created by macbook on 16.12.16.
 */

final class CompetitionsCacheContract {

    interface CompetitionsCacheColumns extends BaseColumns {

        String COMPETITION_NAME = "competition_name";
        String COMPETITION_TYPE = "competition_type";
        String IS_ACTIVE = "is_active";
        String ROUTE_NAME = "route_name";
        String FACTOR = "route_factor";
        String HOLDS_COUNT = "holds_count";
        String COMPETITOR_NAME = "competitor_name";
        String COMPETITOR_LAST_HOLD = "competitor_last_hold";
        String COMPETITOR_IS_TOP = "competitor_is_top";

        String COMPETITION_COMPONENTS[] = {COMPETITION_NAME, COMPETITION_TYPE, IS_ACTIVE};
        String ROUTES_COMPONENTS[] = {ROUTE_NAME, FACTOR, HOLDS_COUNT};
        String COMPETITORS_COMPONENTS[] = {COMPETITOR_NAME, COMPETITOR_LAST_HOLD, COMPETITOR_IS_TOP};


    }

    static final class CompetitionsCached implements CompetitionsCacheColumns {
        static final String COMPETITIONS_TABLE = "competitions_table";
        static final String CREATE_COMPETITIONS_TABLE = "CREATE TABLE " + COMPETITIONS_TABLE
                + " ("
                + COMPETITION_NAME + " TEXT, "
                + COMPETITION_TYPE + " TEXT, "
                + IS_ACTIVE + " TEXT"
                + ")";
        private static final String COMPETITIONS_ROUTES_TABLE = "competition_routes_table";
        private static final String COMPETITORS_TABLE = "competitor_table";

        static String getRoutesTableName(int competitionId) {
            return COMPETITIONS_ROUTES_TABLE + competitionId;
        }

        static String getCompetitorsTableName(int competitionId) {
            return COMPETITIONS_ROUTES_TABLE + competitionId;
        }

        static String getCommandCreateCompetitionsRoutesTable(int competitionId) {
            String tableName = getRoutesTableName(competitionId);
            return "CREATE TABLE " + tableName
                    + " ("
                    + ROUTE_NAME + " TEXT, "
                    + FACTOR + " REAL, "
                    + HOLDS_COUNT + " NUMERIC, "
                    + ")";
        }



        static String getCommandCreateCompetitorsTable(int competitionId) {
            String tableName = getCompetitorsTableName(competitionId);
            return "CREATE TABLE " + tableName
                    + " ("
                    + COMPETITOR_NAME + " TEXT, "
                    + COMPETITOR_LAST_HOLD + " INTEGER, "
                    + COMPETITOR_IS_TOP + " BOOLEAN, "
                    + ")";
        }
    }

    private CompetitionsCacheContract() {
    }
}
