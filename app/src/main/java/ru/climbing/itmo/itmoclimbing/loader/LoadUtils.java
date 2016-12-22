package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
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
 * Created by Игорь on 20.12.2016.
 */

public class LoadUtils {
    public static final String TAG = LoadUtils.class.getSimpleName();
    public static LoadResult<InputStream> loadToStream (HttpURLConnection connection, Context context) {
        //TODO: what is it?
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        Log.d(TAG, "start loading in background");

        ResultType resultType = ResultType.ERROR;
        InputStream in = null;

        try {
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode() + ", "
                        + connection.getResponseMessage());
            }
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(context, false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET_LOADED_FROM_CACHE;
            }
        } catch (BadResponseException e) {
            e.printStackTrace();
        }

        return new LoadResult<>(resultType, in);
    }
}
