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
    private static final String COMPETITOR_LAST_HOLD_TAG = "competitor_last_hold";
    private static final String COMPETITOR_IS_TOP_TAG = "competitor_is_top";
    private static final String COMPETITORS_ROUTE_DATA_TAG = "competitorsRouteData";
    private static final String ROUTES_ENTRY_TAG = "routesEntry";
    private static final String START_POSITION_TAG = "startPosition";
    private static final String RESULT_TAG = "result";


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

    public static String parseRoutesDataToString(ArrayList<CompetitorEntry.RouteResultData> arr) throws JSONException {
        JSONArray jsonArrayRes = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject jsonObjectCompetitor = new JSONObject();
            jsonObjectCompetitor.put(COMPETITOR_LAST_HOLD_TAG, arr.get(i).lastHold);
            jsonObjectCompetitor.put(COMPETITOR_IS_TOP_TAG, arr.get(i).isTop);
            jsonArrayRes.put(jsonObjectCompetitor);
        }
        return jsonArrayRes.toString();
    }

    public static ArrayList<CompetitorEntry.RouteResultData> parseRouteResultDataToArray(String content) throws
            JSONException,
            BadResponseException {
        final JSONArray json = new JSONArray(content);
        return parseRouteResultDataToArray(json);
    }

    private static ArrayList<CompetitorEntry.RouteResultData> parseRouteResultDataToArray(JSONArray json) throws
            JSONException, BadResponseException {
        final ArrayList<CompetitorEntry.RouteResultData> resultArray = new ArrayList<>(json.length());

        for (int i = 0; i < json.length(); ++i) {
            JSONObject jsonCompetitorEntry = json.getJSONObject(i);
            final int last_hold = jsonCompetitorEntry.getInt(COMPETITOR_LAST_HOLD_TAG);
            final boolean is_top = jsonCompetitorEntry.getBoolean(COMPETITOR_IS_TOP_TAG);
            resultArray.add(new CompetitorEntry.RouteResultData(last_hold, is_top));
        }
        return resultArray;

    }

    public static ArrayList<CompetitorEntry> parseCompetitorsToArray(String content) throws
            JSONException,
            BadResponseException {
        final JSONArray json = new JSONArray(content);
        return parseCompetitorsToArray(json);
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
