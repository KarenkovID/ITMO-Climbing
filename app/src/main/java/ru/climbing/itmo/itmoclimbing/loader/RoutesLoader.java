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
    public static final String LOG_TAG = "RoutesLoader";

    public RoutesLoader(Context context, Bundle args) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<ArrayList<Route>> loadInBackground() {
        //TODO: what is it?
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        Log.d(LOG_TAG, "start loading in background");

        ResultType resultType = ResultType.ERROR;
        ArrayList<Route> resultList = null;
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = ItmoClimbingApi.getRoutesRequest();

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                resultList = RoutesDOMParser.parseRoutes(in);

                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode() + ", "
                        + connection.getResponseMessage());
            }
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (BadResponseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, resultList);
    }
}
