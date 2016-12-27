package ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache;

import android.provider.BaseColumns;

import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.ATHLETE_ID;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.REMARK_COAST;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.RESULT_ID;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.RESULT_REMARK;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.ROUTE_ID;

/**
 * Created by Игорь on 27.12.2016.
 */

public final class AthletesRoutesResultsCacheContract {
    interface ResultColumns extends BaseColumns {
        String RESULT_ID = "id";
        String ATHLETE_ID = "athlete_id";
        String ROUTE_ID = "route_id";
        String RESULT_REMARK = "remark";
        String REMARK_COAST = "cost";
        String RESULT_COMPONENTS[] = {
                RESULT_ID, ATHLETE_ID, ROUTE_ID, RESULT_REMARK,
                REMARK_COAST
        };
    }

    static final String RESULTS_TABLE = "results_table";

    static final String CREATE_RESULTS_TABLE = "CREATE TABLE " + RESULTS_TABLE
            + " ("
            + RESULT_ID + " NUMERIC, "
            + ATHLETE_ID + " NUMERIC, "
            + ROUTE_ID + " NUMERIC, "
            + RESULT_REMARK + " TEXT, "
            + REMARK_COAST + " NUMERIC "
            + ")";
    private AthletesRoutesResultsCacheContract() {
    }
}
