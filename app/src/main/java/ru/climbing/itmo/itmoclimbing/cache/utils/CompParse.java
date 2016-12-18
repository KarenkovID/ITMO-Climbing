package ru.climbing.itmo.itmoclimbing.cache.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.loader.BadResponseException;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsRoutesEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitorEntry;

public class CompParse {

    private static final String ROUTE_NAME_TAG = "routeName";
    private static final String ROUTE_FACTOR_TAG = "routeFactor";
    private static final String ROUTE_ID_TAG = "id";
    private static final String ROUTE_COUNT_TAG = "count";
    private static final String COMPETITOR_NAME_TAG = "competitorName";
    private static final String COMPETITORS_ROUTE_DATA_TAG = "competitorsRouteData";
    private static final String ROUTES_ENTRY_TAG = "routesEntry";
    private static final String START_POSITION_TAG = "startPosition";
    private static final String RESULT_TAG = "result";

    public static String parseCompRoutesToString(ArrayList<CompetitionsRoutesEntry> arr) throws JSONException {
        JSONArray jsonArrayRes = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject jsonObjectRoute = new JSONObject();
            jsonObjectRoute.put(ROUTE_NAME_TAG, arr.get(i).routeName);
            jsonObjectRoute.put(ROUTE_FACTOR_TAG, arr.get(i).routeFactor);
            jsonObjectRoute.put(ROUTE_ID_TAG, arr.get(i).id);
            jsonObjectRoute.put(ROUTE_COUNT_TAG, arr.get(i).count);
            jsonArrayRes.put(jsonObjectRoute);
        }
        return jsonArrayRes.toString();
    }

    public static String parseCompetitorsToString(ArrayList<CompetitorEntry> arr) throws JSONException {
        JSONArray jsonArrayRes = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject jsonObjectCompetitor = new JSONObject();
            jsonObjectCompetitor.put(COMPETITOR_NAME_TAG, arr.get(i).competitorName);
            int sz = arr.get(i).competitorsRouteData.size();
            JSONArray jsonArrayCompetitorData = new JSONArray();
            for (int j = 0; j < sz; j++) {
                JSONObject jsonObjectCompetitorData = new JSONObject();
                jsonObjectCompetitorData.put(RESULT_TAG, arr.get(i).competitorsRouteData.get(j).getResult());
                jsonObjectCompetitorData.put(START_POSITION_TAG, arr.get(i).competitorsRouteData.get(j).getStartPosition());
                ArrayList<CompetitionsRoutesEntry> temp = new ArrayList<>();
                temp.add(arr.get(i).competitorsRouteData.get(j).getRoutesEntry());
                jsonObjectCompetitorData.put(ROUTES_ENTRY_TAG, parseCompRoutesToString(temp));
                jsonArrayCompetitorData.put(jsonObjectCompetitorData);
            }
            jsonObjectCompetitor.put(COMPETITORS_ROUTE_DATA_TAG, jsonArrayCompetitorData);
            jsonArrayRes.put(jsonObjectCompetitor);
        }
        return jsonArrayRes.toString();
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
            final String name = movieJson.getString(ROUTE_NAME_TAG);
            final double author = movieJson.getDouble(ROUTE_FACTOR_TAG);
            final int description = movieJson.getInt(ROUTE_ID_TAG);
            final int count = movieJson.getInt(ROUTE_COUNT_TAG);
            resultArray.add(new CompetitionsRoutesEntry(name, author, description, count));
        }
        return resultArray;
    }


    private static ArrayList<CompetitorEntry> parseCompetitorsToArray(JSONArray json) throws
            JSONException, BadResponseException {
        final ArrayList<CompetitorEntry> resultArray = new ArrayList<>(json.length());
        for (int i = 0; i < json.length(); ++i) {
            JSONObject movieJson = json.getJSONObject(i);
            final String name = movieJson.getString(COMPETITOR_NAME_TAG);
            final String author = movieJson.getString(COMPETITORS_ROUTE_DATA_TAG);
            final JSONArray json2 = new JSONArray(author);
            final ArrayList<CompetitorEntry.CompetitorsRouteData> resultArray2 = new ArrayList<>(json2.length());
            for (int j = 0; j < json2.length(); j++) {
                JSONObject movieJson2 = json2.getJSONObject(j);
                final int startPosition = movieJson2.getInt(START_POSITION_TAG);
                final int result = movieJson2.getInt(RESULT_TAG);
                final ArrayList<CompetitionsRoutesEntry> cre = parseCompRoutesToArray(movieJson2.getString(ROUTES_ENTRY_TAG));
                resultArray2.add(new CompetitorEntry.CompetitorsRouteData(cre.get(0), startPosition, result));
            }
            resultArray.add(new CompetitorEntry(name, resultArray2));
        }
        return resultArray;
    }
}
