package ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache;

import android.provider.BaseColumns;

import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_AUTHOR;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_DESCRIPTION;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_GRADE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_GRADE_COAST;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_IS_ACTIVE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_NAME;


/**
 * Created by macbook on 18.12.16.
 */

public class RoutesCacheContract {
    interface RouteCacheColumns extends BaseColumns {

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

    private RoutesCacheContract() {
    }

}
