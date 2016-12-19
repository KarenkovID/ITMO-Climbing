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
        String COMPETITION_ROUTES = "competition_routes";
        String COMPETITORS = "competitors";

        String COMPETITION_COMPONENTS[] = {COMPETITION_NAME, COMPETITION_TYPE, IS_ACTIVE, COMPETITION_ROUTES, COMPETITORS};


    }

    static final class CompetitionsCached implements CompetitionsCacheColumns {
        static final String COMPETITIONS_TABLE = "competitions_table";
        static final String CREATE_COMPETITIONS_TABLE = "CREATE TABLE " + COMPETITIONS_TABLE
                + " ("
                + COMPETITION_NAME + " TEXT, "
                + COMPETITION_TYPE + " TEXT, "
                + IS_ACTIVE + " TEXT, "
                + COMPETITION_ROUTES + " TEXT, "
                + COMPETITORS + " TEXT"
                + ")";
    }

    private CompetitionsCacheContract() {
    }
}
