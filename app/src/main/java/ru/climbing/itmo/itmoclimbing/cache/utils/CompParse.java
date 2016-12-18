package ru.climbing.itmo.itmoclimbing.cache.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.loader.BadResponseException;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsRoutesEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitorEntry;

/**
 * Created by macbook on 18.12.16.
 */

public class CompParse {

    public static String parseCompRoutesToString(ArrayList<CompetitionsRoutesEntry> arr) {
        JSONArray obj = new JSONArray(arr);
        return arr.toString();
    }

    public static String parseCompetitorsToString(ArrayList<CompetitorEntry> arr) {
        JSONArray obj = new JSONArray(arr);
        return arr.toString();
    }


    public static ArrayList<CompetitionsRoutesEntry> parseCompRoutesToArray(String content) throws
            JSONException,
            BadResponseException {
        final JSONArray json = new JSONArray(content);
        return parseCompRoutesToArray(json);
    }

    public static ArrayList<CompetitorEntry> parseCompetitorsToArray(String content) throws
            JSONException,
            BadResponseException {
        final JSONArray json = new JSONArray(content);
        return parseCompetitorsToArray(json);
    }

    private static ArrayList<CompetitionsRoutesEntry> parseCompRoutesToArray(JSONArray json) throws
            JSONException {
        final ArrayList<CompetitionsRoutesEntry> resultArray = new ArrayList<>(json.length());
        for (int i = 0; i < json.length(); ++i) {
            JSONObject movieJson = json.getJSONObject(i);
            final String name = movieJson.getString("routeName");
            final double author = movieJson.getDouble("routeFactor");
            final int description = movieJson.getInt("id");
            resultArray.add(new CompetitionsRoutesEntry(name, author, description));
        }
        return resultArray;
    }


    private static ArrayList<CompetitorEntry> parseCompetitorsToArray(JSONArray json) throws
            JSONException, BadResponseException {
        final ArrayList<CompetitorEntry> resultArray = new ArrayList<>(json.length());
        for (int i = 0; i < json.length(); ++i) {
            JSONObject movieJson = json.getJSONObject(i);
            final String name = movieJson.getString("competitorName");
            final String author = movieJson.getString("competitorData");
            final JSONArray json2 = new JSONArray(author);
            final ArrayList<CompetitorEntry.CompetitorsRouteData> resultArray2 = new ArrayList<>(json2.length());
            for (int j = 0; j < json2.length(); j++) {
                JSONObject movieJson2 = json2.getJSONObject(j);
                final int startPosition = movieJson2.getInt("startPosition");
                final int result = movieJson2.getInt("result");
                final ArrayList<CompetitionsRoutesEntry> cre = parseCompRoutesToArray(movieJson2.getString("routesEntry"));
                resultArray2.add(new CompetitorEntry.CompetitorsRouteData(cre.get(0), startPosition, result));
            }
            resultArray.add(new CompetitorEntry(name, resultArray2));
        }
        return resultArray;
    }
}
