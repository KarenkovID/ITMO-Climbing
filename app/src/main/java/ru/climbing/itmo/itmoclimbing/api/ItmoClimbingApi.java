package ru.climbing.itmo.itmoclimbing.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Игорь on 30.11.2016.
 */

public class ItmoClimbingApi {
    private static final Uri BASE_URI = Uri.parse("http://itcl.pythonanywhere.com/api");

    private ItmoClimbingApi() {
    }

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса
     */
    public static HttpURLConnection getRoutesRequest() throws IOException {
        Uri uri = BASE_URI.buildUpon()
                .appendEncodedPath("routes/?format=json").build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }

    public static HttpURLConnection getAthleteInfoListRequest() throws IOException{
        Uri uri = BASE_URI.buildUpon()
                .appendEncodedPath("athletes/?format=json").build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }

    public static HttpURLConnection getAthletesRoutesResulteTableRequest() throws IOException{
        Uri uri = BASE_URI.buildUpon()
                .appendEncodedPath("athlete_routes/?format=json").build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
