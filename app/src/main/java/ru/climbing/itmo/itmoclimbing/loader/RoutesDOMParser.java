package ru.climbing.itmo.itmoclimbing.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.model.Route;
import ru.climbing.itmo.itmoclimbing.utils.IOUtils;

/**
 * Created by Игорь on 19.11.2016.
 */

public final class RoutesDOMParser {
    public static ArrayList<Route> parseRoutes(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONArray json = new JSONArray(content);
        return parseRoutes(json);
    }

    private static ArrayList<Route> parseRoutes(JSONArray json) throws
            JSONException {
        final ArrayList<Route> resultArray = new ArrayList<Route>(json.length());
        for (int i = 0; i < json.length(); ++i) {
            JSONObject movieJson = json.getJSONObject(i);
            final String name = movieJson.getString("competitionName");
            final String author = movieJson.getString("author");
            final String description = movieJson.getString("description");
            resultArray.add(new Route(name, "8a", author, description));
        }

        return resultArray;
    }
}
