package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.api.ItmoClimbingApi;
import ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesAndAthletesCache;
import ru.climbing.itmo.itmoclimbing.model.Route;
import ru.climbing.itmo.itmoclimbing.utils.IOUtils;


/**
 * Created by Игорь on 19.11.2016.
 */

public class RoutesLoader extends AsyncTaskLoader<LoadResult<ArrayList<Route>>> {

    public static final String TAG = "RoutesLoader";

    public RoutesLoader(Context context, Bundle args) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<ArrayList<Route>> loadInBackground() {
        LoadResult<InputStream> loadResult;
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            try {
                connection = ItmoClimbingApi.getRoutesRequest();
                loadResult = LoadUtils.loadToStream(connection, getContext());
            } catch (IOException e) {
                Log.e(TAG, "loadInBackground: error while do getRouteRequest", e);
                return new LoadResult<>(ResultType.ERROR, null);
            }
            ArrayList<Route> routesList = null;
            ResultType resultType = loadResult.resultType;
            //If data was loaded from server
            if (resultType == ResultType.OK) {
                try {
                    in = loadResult.data;
                    routesList = JsonDOMParser.parseRoutes(in);
                    RoutesAndAthletesCache cache = new RoutesAndAthletesCache(getContext());
                    cache.putRoutes(routesList);
                } catch (JSONException | BadResponseException | IOException e) {
                    resultType = ResultType.ERROR;
                    Log.e(TAG, "loadInBackground: ERROR WHILE PARSE JSON", e);
                }
            } else if (resultType == ResultType.NO_INTERNET_LOADED_FROM_CACHE) {
                // FIXME: 22.12.2016 Add new type of error
                try {
                    routesList = new RoutesAndAthletesCache(getContext()).getRoutesList();
                } catch (FileNotFoundException e) {
                    resultType = ResultType.ERROR;
                }
            }
            return new LoadResult<>(resultType, routesList);
        } finally {
            if (in != null) {
                IOUtils.closeSilently(in);
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
