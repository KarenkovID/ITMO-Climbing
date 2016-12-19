package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.api.ItmoClimbingApi;
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
            if (resultType == ResultType.OK) {
                try {
                    in = loadResult.data;
                    routesList = JsonDOMParser.parseRoutes(in);
                } catch (JSONException | BadResponseException | IOException e) {
                    resultType = ResultType.ERROR;
                    Log.e(TAG, "loadInBackground: EROR WHILE PARSE JSON", e);
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
