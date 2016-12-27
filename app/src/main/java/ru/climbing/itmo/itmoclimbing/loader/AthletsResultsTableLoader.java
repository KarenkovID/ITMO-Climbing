package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.api.ItmoClimbingApi;
import ru.climbing.itmo.itmoclimbing.model.AthleteRouteResult;
import ru.climbing.itmo.itmoclimbing.utils.IOUtils;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthletsResultsTableLoader extends AsyncTaskLoader<LoadResult<ArrayList<AthleteRouteResult>>>{
    public static final String TAG = AthleteRouteResult.class.getSimpleName();

    public AthletsResultsTableLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<ArrayList<AthleteRouteResult>> loadInBackground() {
        LoadResult<InputStream> loadResult;
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            try {
                connection = ItmoClimbingApi.getAthletesRoutesResultTableRequest();
                loadResult = LoadUtils.loadToStream(connection, getContext());
            } catch (IOException e) {
                Log.e(TAG, "loadInBackground: error while do getRouteRequest", e);
                return new LoadResult<>(ResultType.ERROR, null);
            }
            ArrayList<AthleteRouteResult> resultsTable = null;
            ResultType resultType = loadResult.resultType;
            if (resultType == ResultType.OK) {
                try {
                    in = loadResult.data;
                    resultsTable = JsonDOMParser.parseResultsTable(in);
                } catch (JSONException | BadResponseException | IOException e) {
                    resultType = ResultType.ERROR;
                    Log.e(TAG, "loadInBackground: ERROR WHILE PARSE JSON", e);
                }
            }
            return new LoadResult<>(resultType, resultsTable);
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
