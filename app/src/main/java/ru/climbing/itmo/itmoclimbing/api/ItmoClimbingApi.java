package ru.climbing.itmo.itmoclimbing.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Игорь on 30.11.2016.
 */

public class ItmoClimbingApi {
    private static final Uri BASE_URI = Uri.parse("http://itcl.pythonanywhere.com");

    private ItmoClimbingApi() {
    }

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса
     */
    public static HttpURLConnection getRoutesRequest() throws IOException {
        // TODO
        Uri uri = BASE_URI.buildUpon()
                .appendEncodedPath("api/routes/?format=json").build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
