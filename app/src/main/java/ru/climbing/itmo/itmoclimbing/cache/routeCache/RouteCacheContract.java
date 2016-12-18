package ru.climbing.itmo.itmoclimbing.cache.routeCache;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;


/**
 * Created by macbook on 18.12.16.
 */

public class RouteCacheContract {
    interface routeCacheColumns extends BaseColumns {

        String ROUTE_NAME = "name";

        String ROUTE_GRADE = "grade";

        String ROUTE_DESCRIPTION = "description";

        String ROUTE_AUTHOR = "author";

        String ROUTE_COMPONENTS[] = {
                ROUTE_NAME, ROUTE_GRADE, ROUTE_DESCRIPTION, ROUTE_AUTHOR
        };
    }

    static final class RoutesCached implements RouteCacheContract.routeCacheColumns {
        static final String ROUTES_TABLE = "routes_table";
        static final String CREATE_ROUTES_TABLE = "CREATE TABLE " + ROUTES_TABLE
                + " ("
                + ROUTE_NAME + " TEXT, "
                + ROUTE_GRADE + " TEXT, "
                + ROUTE_DESCRIPTION + " TEXT, "
                + ROUTE_AUTHOR + " TEXT"
                + ")";
    }

    private RouteCacheContract() {
    }

}
