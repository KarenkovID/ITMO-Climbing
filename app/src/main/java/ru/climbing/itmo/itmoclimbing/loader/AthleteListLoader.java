package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.api.ItmoClimbingApi;
import ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesAndAthletesCache;
import ru.climbing.itmo.itmoclimbing.model.Athlete;
import ru.climbing.itmo.itmoclimbing.model.Route;
import ru.climbing.itmo.itmoclimbing.utils.IOUtils;

/**
 * Created by Игорь on 20.12.2016.
 */

public class AthleteListLoader extends AsyncTaskLoader<LoadResult<ArrayList<Athlete>>> {
    public static final String TAG = Athlete.class.getSimpleName();

    public AthleteListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<ArrayList<Athlete>> loadInBackground() {
        LoadResult<InputStream> loadResult;
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            try {
                connection = ItmoClimbingApi.getAthleteInfoListRequest();
                loadResult = LoadUtils.loadToStream(connection, getContext());
            } catch (IOException e) {
                Log.e(TAG, "loadInBackground: error while do getRouteRequest", e);
                return new LoadResult<>(ResultType.ERROR, null);
            }
            ArrayList<Athlete> athletesList = null;
            ResultType resultType = loadResult.resultType;
            if (resultType == ResultType.OK) {
                try {
                    in = loadResult.data;
                    athletesList = JsonDOMParser.parseAthletes(in);
                    RoutesAndAthletesCache cache = new RoutesAndAthletesCache(getContext());
                    cache.putAthletes(athletesList);
                } catch (JSONException | BadResponseException | IOException e) {
                    resultType = ResultType.ERROR;
                    Log.e(TAG, "loadInBackground: EROR WHILE PARSE JSON", e);
                }
            }
            return new LoadResult<>(resultType, athletesList);
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
