package ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsDBHelper;
import ru.climbing.itmo.itmoclimbing.model.Athlete;
import ru.climbing.itmo.itmoclimbing.model.AthleteRouteResult;
import ru.climbing.itmo.itmoclimbing.model.Route;

import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.ATHLETES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_FIRST_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_LAST_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.RESULTS_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.ATHLETE_ID;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesRoutesResultsCacheContract.ResultColumns.RESULT_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.ROUTES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_AUTHOR;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_DESCRIPTION;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_GRADE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_GRADE_COAST;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_ID;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_IS_ACTIVE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.RouteCacheColumns.ROUTE_NAME;

public class RoutesAndAthletesCache {
    public static final String TAG = RoutesAndAthletesCache.class.getSimpleName();

    @NonNull
    private final Context context;

    private void fillStatement(SQLiteStatement insert, Route route) {
        insert.bindLong(1, route.id);
        insert.bindString(2, route.name);
        insert.bindString(3, route.author);
        insert.bindString(4, route.grade.grade);
        insert.bindLong(5, route.grade.cost);
        insert.bindString(6, route.description);
        insert.bindLong(7, route.isActive ? 1 : 0);
    }

    private void fillStatement(SQLiteStatement insert, Athlete athlete){
        insert.bindLong(1, athlete.id);
        insert.bindString(2, athlete.firstName);
        insert.bindString(3, athlete.lastName);
        insert.bindDouble(4, athlete.score);
        insert.bindLong(5, athlete.position);
    }

    private void fillStatement(SQLiteStatement insert, AthleteRouteResult result) {
        insert.bindLong(1, result.resultID);
        insert.bindLong(2, result.athleteID);
        insert.bindLong(3, result.routeID);
        insert.bindString(4, result.remark);
        insert.bindLong(5, result.remarkCost);
    }

    @AnyThread
    public RoutesAndAthletesCache(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    @NonNull
    public ArrayList<Route> getRoutesList()
            throws FileNotFoundException {
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = ROUTE_COMPONENTS;
        ArrayList<Route> compTable = new ArrayList<>();


        try (Cursor cursor = db.query(
                ROUTES_TABLE,
                projection,
                null,
                null,
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String routeName = cursor.getString(1);
                    String routeAuthor = cursor.getString(2);
                    String routeGrade = cursor.getString(3);
                    int routeGrateCoast = cursor.getInt(4);
                    String routeDescription = cursor.getString(5);
                    boolean routeIsActive = 1 == cursor.getInt(6);
                    compTable.add(new Route(id, routeName, routeGrade, routeGrateCoast, routeAuthor, routeDescription, routeIsActive));
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        }
        return compTable;
    }

    @WorkerThread
    public void putRoutes(@NonNull List<Route> compTable) {
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from "+ ROUTES_TABLE);
        String insertion = "INSERT INTO " + ROUTES_TABLE + " ("
                + ROUTE_ID + ", "
                + ROUTE_NAME + ", "
                + ROUTE_AUTHOR + ", "
                + ROUTE_GRADE + ", "
                + ROUTE_GRADE_COAST + ", "
                + ROUTE_DESCRIPTION + ", "
                + ROUTE_IS_ACTIVE;
        insertion += ") VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (Route entry : compTable) {
                fillStatement(insert, entry);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
        }
    }

    @WorkerThread
    @NonNull
    public ArrayList<Athlete> getAthletesList()
            throws FileNotFoundException {
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = ATHLETE_COMPONENTS;
        ArrayList<Athlete> athletesList = new ArrayList<Athlete>();

        try (Cursor cursor = db.query(
                ATHLETES_TABLE,
                projection,
                null,
                null,
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    int athleteID = cursor.getInt(0);
                    String firstName = cursor.getString(1);
                    String lastName = cursor.getString(2);
                    double score = cursor.getDouble(3);
                    int  position = cursor.getInt(4);
                    athletesList.add(new Athlete(athleteID, firstName, lastName, score, position));
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        }
        return athletesList;
    }

    @WorkerThread
    public void putAthletes(@NonNull List<Athlete> compTable) {
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from "+ ATHLETES_TABLE);
        String insertion = "INSERT INTO " + ATHLETES_TABLE + " ("
                + ATHLETE_COMPONENTS[0] + ", "
                + ATHLETE_COMPONENTS[1] + ", "
                + ATHLETE_COMPONENTS[2] + ", "
                + ATHLETE_COMPONENTS[3] + ", "
                + ATHLETE_COMPONENTS[4];
        insertion += ") VALUES(?, ?, ?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (Athlete entry : compTable) {
                fillStatement(insert, entry);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
        }
    }

    public void putAthletesRoutesResultsTable (@NonNull List<AthleteRouteResult> resultsList) {
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from "+ RESULTS_TABLE);
        String insertion = "INSERT INTO " + RESULTS_TABLE + " ("
                + RESULT_COMPONENTS[0] + ", "
                + RESULT_COMPONENTS[1] + ", "
                + RESULT_COMPONENTS[2] + ", "
                + RESULT_COMPONENTS[3] + ", "
                + RESULT_COMPONENTS[4];
        insertion += ") VALUES(?, ?, ?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (AthleteRouteResult entry : resultsList) {
                fillStatement(insert, entry);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
        }
    }


    /**
     * @param athleteID is id of requested athlete
     * @return list contains results for this athlete
     * @throws FileNotFoundException
     */
    public ArrayList<AthleteRouteResult> getAthletesSolvedRoutes (int athleteID)
            throws FileNotFoundException{
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = new String[RESULT_COMPONENTS.length + 3];
        for (int i = 0; i < RESULT_COMPONENTS.length; i++) {
            projection[i] = "a." + RESULT_COMPONENTS[i];
        }
        projection[RESULT_COMPONENTS.length] = "b." + RoutesCacheContract.RouteCacheColumns.ROUTE_NAME;
        projection[RESULT_COMPONENTS.length + 1] ="b." + RoutesCacheContract.RouteCacheColumns.ROUTE_GRADE;
        projection[RESULT_COMPONENTS.length + 2] = "b." + RoutesCacheContract.RouteCacheColumns.ROUTE_GRADE_COAST;
        ArrayList<AthleteRouteResult> resultsList = new ArrayList<AthleteRouteResult>();

        String MY_QUERY = "SELECT";
        for (int i = 0; i < projection.length; i++) {
            MY_QUERY += " " + projection[i];
            if (i != projection.length - 1) {
                MY_QUERY += ",";
            }
        }

        /*
        SELECT a.id, a.athlete_id, a.route_id, a.remark, a.cost, b.name, b.grade, b.gradeCoast FROM results_table a INNER JOIN routes_table b ON a.route_id=b.id WHERE a.athlete_id=4
         */

        MY_QUERY += " FROM " + RESULTS_TABLE + " a INNER JOIN "+ ROUTES_TABLE + " b ON "+
                "a." + AthletesRoutesResultsCacheContract.ResultColumns.ROUTE_ID + "=" +
                "b." + RoutesCacheContract.RouteCacheColumns.ROUTE_ID + " WHERE " + "a." + ATHLETE_ID + "=" + athleteID;
        Log.d(TAG, "getAthletesSolvedRoutes: " + MY_QUERY);
        Log.d(TAG, "getAthletesSolvedRoutes: athlete id " + athleteID);
        try (Cursor cursor = db.rawQuery(MY_QUERY, null)/*db.query(
                RESULTS_TABLE +", " + ROUTES_TABLE ,
                projection,
                ATHLETE_ID + "=? AND " + RESULTS_TABLE + "."
                    + AthletesRoutesResultsCacheContract.ResultColumns.ROUTE_ID + "="
                    + ROUTES_TABLE + "." + ROUTE_ID,
                new String[] {String.valueOf(athleteID)},
                null,
                null,
                null)*/) {
            if (cursor != null && cursor.moveToFirst()) {
                Log.d(TAG, "getAthletesSolvedRoutes: cursor is not empty");
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    int resultID = cursor.getInt(0);
//                    int athleteID = cursor.getInt(1);
                    int routeID = cursor.getInt(2);
                    String resultRemark = cursor.getString(3);
                    int remarkCoast = cursor.getInt(4);
                    String routeName = cursor.getString(5);
                    String routeGrade = cursor.getString(6);
                    int routeCoast = cursor.getInt(7);
                    resultsList.add(new AthleteRouteResult(
                            resultID, athleteID, routeID, resultRemark, remarkCoast, routeName,
                            routeGrade, routeCoast));
                }
            } else {
//                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        }
        return resultsList;
    }

    public String getAthleteName(int athleteID) throws FileNotFoundException{
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = new String[]{ATHLETE_FIRST_NAME, ATHLETE_LAST_NAME};

        String res = null;
        try (Cursor cursor = db.query(
                ATHLETES_TABLE ,
                projection,
                AthletesCacheContract.AthleteCacheColumns.ATHLETE_ID+ "=?",
                new String[] {String.valueOf(athleteID)},
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    String firstName = cursor.getString(0);
                    String lastName = cursor.getString(1);
                    res = firstName + lastName;
                    break;
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        }
        return res == "null" ? ":c" : res;
    }
}
