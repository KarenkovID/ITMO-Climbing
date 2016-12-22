package ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache;

import android.provider.BaseColumns;

import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_FIRST_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_ID;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_LAST_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_POSITION;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_SCORE;

/**
 * Created by Игорь on 22.12.2016.
 */

public final class AthletesCacheContract{
    interface AthleteCacheColumns extends BaseColumns {

        String ATHLETE_ID = "id";

        String ATHLETE_FIRST_NAME = "firstName";

        String ATHLETE_LAST_NAME = "lastName";

        String ATHLETE_SCORE = "score";

        String ATHLETE_POSITION = "position";

        String ATHLETE_COMPONENTS[] = {
                ATHLETE_ID, ATHLETE_FIRST_NAME, ATHLETE_LAST_NAME,
                ATHLETE_SCORE, ATHLETE_POSITION
        };
    }

    static final String ATHLETES_TABLE = "athletes_table";

    static final String CREATE_ATHLETES_TABLE = "CREATE TABLE " + ATHLETES_TABLE
            + " ("
            + ATHLETE_ID + " NUMERIC, "
            + ATHLETE_FIRST_NAME + " TEXT, "
            + ATHLETE_LAST_NAME + " TEXT, "
            + ATHLETE_SCORE + " REAL, "
            + ATHLETE_POSITION + " NUMERIC "
            + ")";
    private AthletesCacheContract() {
    }

}
