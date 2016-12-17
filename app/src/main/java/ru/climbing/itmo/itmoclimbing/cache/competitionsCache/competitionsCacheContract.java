package ru.climbing.itmo.itmoclimbing.cache.competitionsCache;

import android.provider.BaseColumns;

/**
 * Created by macbook on 16.12.16.
 */

final class CompetitionsCacheContract {

    interface competitionsCacheColumns extends BaseColumns {

        String COMPETITION_NAME = "competition_name";
        String COMPETITION_TYPE = "competition_type";
        String COMPETITION_TIME = "competition_time";
        String IS_ACTIVE = "is_active";

        String COMPETITION_COMPONENTS[] = {COMPETITION_NAME, COMPETITION_TYPE, COMPETITION_TIME, IS_ACTIVE};




    }

    static final class CompetitionsCached implements competitionsCacheColumns {
        static final String COMPETITIONS_TABLE = "competitions_table";
        static final String CREATE_COMPETITIONS_TABLE = "CREATE TABLE " + COMPETITIONS_TABLE
                + " ("
                + COMPETITION_NAME + " TEXT, "
                + COMPETITION_TYPE + " TEXT, "
                + COMPETITION_TIME + " INTEGER, "
                + IS_ACTIVE + " BIT" // TODO if doesn't work change on int value
                + ")";
    }

    private CompetitionsCacheContract() {
    }
}
