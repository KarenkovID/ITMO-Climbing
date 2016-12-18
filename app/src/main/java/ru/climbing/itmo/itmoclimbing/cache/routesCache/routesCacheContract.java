package ru.climbing.itmo.itmoclimbing.cache.routesCache;

import android.provider.BaseColumns;

/**
 * Created by macbook on 16.12.16.
 */

final class RoutesCacheContract {

    public interface routsCacheColumns extends BaseColumns {

        String ROUTE_NAME = "route_name";
        String ROUTE_LEVEL = "route_level";
        String ROUTE_ID = "route_id";

        String ROUTE_COMPONENTS [] = {ROUTE_NAME, ROUTE_LEVEL, ROUTE_ID};

    }

    static final class RoutesCached implements routsCacheColumns {
        static final String ROUTES_TABLE = "routes_table";
        static final String CREATE_ROUTES_TABLE = "CREATE TABLE " + ROUTES_TABLE
                + " ("
                + ROUTE_NAME + " TEXT, "
                + ROUTE_LEVEL + " REAL, "
                + ROUTE_ID + " INTEGER"
                + ")";
    }

    private RoutesCacheContract() {
    }
}
