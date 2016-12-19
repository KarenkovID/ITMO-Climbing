package ru.climbing.itmo.itmoclimbing.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.climbing.itmo.itmoclimbing.model.Athlete;
import ru.climbing.itmo.itmoclimbing.model.Route;
import ru.climbing.itmo.itmoclimbing.utils.IOUtils;

/**
 * Created by Игорь on 19.11.2016.
 */

public final class JsonDOMParser {
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
            JSONObject jsonRoute = json.getJSONObject(i);
            final int id = jsonRoute.getInt("id");
            JSONObject jsonObjGrade = jsonRoute.getJSONObject("grade");
            final String grade = jsonObjGrade.getString("grade");
            final int  cost = jsonObjGrade.getInt("cost");
            final String name = jsonRoute.getString("name");
            final String description = jsonRoute.getString("description");
            final String author = jsonRoute.getString("author");
            final boolean isActive = jsonRoute.getBoolean("is_active");
            resultArray.add(new Route(name, grade, cost, author, description, isActive));
        }

        return resultArray;
    }

    public static ArrayList<Athlete> parseAthletes(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException{
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONArray json = new JSONArray(content);
        return parseAthletes(json);
    }

    private static ArrayList<Athlete> parseAthletes(JSONArray json) throws
            JSONException {
        final ArrayList<Athlete> resultArray = new ArrayList<Athlete>(json.length());
        for (int i = 0; i < json.length(); ++i) {
            JSONObject jsonAthlete = json.getJSONObject(i);
            final int id = jsonAthlete.getInt("id");
            final String firstName = jsonAthlete.getString("first_name");
            final String lastName = jsonAthlete.getString("last_name");
            final double score = jsonAthlete.getDouble("score");
            final int position = jsonAthlete.getInt("position");
            resultArray.add(new Athlete(id, lastName, firstName, score, position));
        }
        return resultArray;
    }


}