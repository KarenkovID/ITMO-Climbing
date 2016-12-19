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
    private static final String LAST_HOLD_TAG = "lastHold";
    private static final String IS_TOP_TAG = "isTop";
    private static final String ROUTE_COUNT_TAG = "count";
    private static final String COMPETITOR_NAME_TAG = "competitorName";
    private static final String COMPETITORS_ROUTE_DATA_TAG = "competitorsRouteData";
    private static final String ROUTES_ENTRY_TAG = "routesEntry";
    private static final String START_POSITION_TAG = "startPosition";
    private static final String RESULT_TAG = "result";

    public static String parseCompRoutesToString(ArrayList<CompetitionsRoutesEntry> arr) 
            throws JSONException {
        JSONArray jsonArrayRes = new JSONArray();
        
        for (int i = 0; i < arr.size(); i++) {
            JSONObject jsonObjectRoute = new JSONObject();

            jsonObjectRoute.put(ROUTE_NAME_TAG, arr.get(i).routeName);
            jsonObjectRoute.put(ROUTE_FACTOR_TAG, arr.get(i).routeFactor);
            jsonObjectRoute.put(ROUTE_COUNT_TAG, arr.get(i).holdsCount);
            jsonArrayRes.put(jsonObjectRoute);
        }

        return jsonArrayRes.toString();
    }

    public static String parseCompetitorsToString(ArrayList<CompetitorEntry> arr) throws JSONException {
        JSONArray jsonArrayRes = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject jsonObjectCompetitor = new JSONObject();
            JSONArray jsonArrayCompetitorData = new JSONArray();

            for (int j = 0; j < arr.get(i).competitorsRouteResultData.size(); j++) {
                CompetitorEntry.RouteResultData currentRouteData = arr.get(i).competitorsRouteResultData.get(j);
                JSONObject jsonObjectRouteData = new JSONObject();
                jsonObjectRouteData.put(LAST_HOLD_TAG, currentRouteData.lastHold);
                jsonObjectRouteData.put(IS_TOP_TAG, currentRouteData.isTop);
                jsonArrayCompetitorData.put(jsonObjectRouteData);
            }

            jsonObjectCompetitor.put(COMPETITOR_NAME_TAG, arr.get(i).competitorName);
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

            JSONObject jsonRoute = json.getJSONObject(i);

            final String name = jsonRoute.getString(ROUTE_NAME_TAG);
            final double factor = jsonRoute.getDouble(ROUTE_FACTOR_TAG);
            final int countOfHolds = jsonRoute.getInt(ROUTE_COUNT_TAG);

            resultArray.add(new CompetitionsRoutesEntry(name, factor, countOfHolds));
        }
        return resultArray;
    }

    // FIXME: 19.12.2016 
    private static ArrayList<CompetitorEntry> parseCompetitorsToArray(JSONArray json) throws
            JSONException, BadResponseException {
        final ArrayList<CompetitorEntry> resultArray = new ArrayList<>(json.length());

        for (int i = 0; i < json.length(); ++i) {
            JSONObject jsonCompetitorEntry = json.getJSONObject(i);

            final String competitorName = jsonCompetitorEntry.getString(COMPETITOR_NAME_TAG);
            final JSONArray jsonCompetitorDataArray = jsonCompetitorEntry.getJSONArray(COMPETITORS_ROUTE_DATA_TAG);

            final ArrayList<CompetitorEntry.RouteResultData> competitorDataArray
                    = new ArrayList<>(jsonCompetitorDataArray.length());
            for (int j = 0; j < jsonCompetitorDataArray.length(); j++) {
                JSONObject jsonRouteData = jsonCompetitorDataArray.getJSONObject(j);

                final int lastHold = jsonRouteData.getInt(RESULT_TAG);
                final boolean isTop = jsonRouteData.getBoolean(IS_TOP_TAG);
                competitorDataArray.add(new CompetitorEntry.RouteResultData(lastHold, isTop));
            }

            resultArray.add(new CompetitorEntry(competitorName, i));
        }

        return resultArray;
    }
}
