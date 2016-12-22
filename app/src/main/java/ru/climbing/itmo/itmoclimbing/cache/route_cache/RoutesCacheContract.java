package ru.climbing.itmo.itmoclimbing.cache.route_cache;

import android.provider.BaseColumns;


/**
 * Created by macbook on 18.12.16.
 */

public class RoutesCacheContract {
    interface routeCacheColumns extends BaseColumns {

        String ROUTE_NAME = "name";

        String ROUTE_AUTHOR = "author";

        String ROUTE_GRADE = "grade";

        String ROUTE_GRADE_COAST = "gradeCoast";

        String ROUTE_DESCRIPTION = "description";

        String ROUTE_IS_ACTIVE = "isActive";

        String ROUTE_COMPONENTS[] = {
                ROUTE_NAME, ROUTE_AUTHOR, ROUTE_GRADE,
                ROUTE_GRADE_COAST, ROUTE_DESCRIPTION, ROUTE_IS_ACTIVE
        };
    }

    static final class RoutesCached implements RoutesCacheContract.routeCacheColumns {
        static final String ROUTES_TABLE = "routes_table";
        static final String CREATE_ROUTES_TABLE = "CREATE TABLE " + ROUTES_TABLE
                + " ("
                + ROUTE_NAME + " TEXT, "
                + ROUTE_AUTHOR + " TEXT, "
                + ROUTE_GRADE + " TEXT, "
                + ROUTE_GRADE_COAST + " NUMERIC, "
                + ROUTE_DESCRIPTION + " TEXT, "
                + ROUTE_IS_ACTIVE + " NUMERIC"
                + ")";
    }

    private RoutesCacheContract() {
    }

}
