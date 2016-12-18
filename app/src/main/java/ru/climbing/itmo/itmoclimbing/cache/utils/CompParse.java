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

            int sz = arr.get(i).competitorsRouteData.size();
            JSONArray jsonArrayCompetitorData = new JSONArray();

            for (int j = 0; j < sz; j++) {

                JSONObject jsonObjectCompetitorData = new JSONObject();

                ArrayList<CompetitionsRoutesEntry> temp = new ArrayList<>();
                temp.add(arr.get(i).competitorsRouteData.get(j).getRoutesEntry());

                jsonObjectCompetitorData.put(ROUTES_ENTRY_TAG, parseCompRoutesToString(temp));
                jsonObjectCompetitorData.put(START_POSITION_TAG, arr.get(i).competitorsRouteData
                        .get(j).getStartPosition());
                jsonObjectCompetitorData.put(RESULT_TAG, arr.get(i).competitorsRouteData
                        .get(j).getResult());

                jsonArrayCompetitorData.put(jsonObjectCompetitorData);
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

            JSONObject jsonCompetitorEntry = json.getJSONObject(i);

            final String competitorName = jsonCompetitorEntry.getString(COMPETITOR_NAME_TAG);
            final String competitorData = jsonCompetitorEntry.getString(COMPETITORS_ROUTE_DATA_TAG);

            final JSONArray jsonCompetitorDataArray = new JSONArray(competitorData);
            final ArrayList<CompetitorEntry.CompetitorsRouteData> competitorDataArray
                    = new ArrayList<>(jsonCompetitorDataArray.length());

            for (int j = 0; j < jsonCompetitorDataArray.length(); j++) {

                JSONObject jsonCompetitorDataObject = jsonCompetitorDataArray.getJSONObject(j);

                final int startPosition = jsonCompetitorDataObject.getInt(START_POSITION_TAG);
                final int result = jsonCompetitorDataObject.getInt(RESULT_TAG);
                final ArrayList<CompetitionsRoutesEntry> competitionsRoutesEntries =
                        parseCompRoutesToArray(jsonCompetitorDataObject.getString(ROUTES_ENTRY_TAG));

                competitorDataArray.add(new CompetitorEntry.CompetitorsRouteData(
                        competitionsRoutesEntries.get(0),
                        startPosition, result));
            }

            resultArray.add(new CompetitorEntry(competitorName, competitorDataArray));
        }

        return resultArray;
    }
}
