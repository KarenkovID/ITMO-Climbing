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
import ru.climbing.itmo.itmoclimbing.model.Route;

import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.ATHLETES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.AthleteCacheColumns.ATHLETE_COMPONENTS;
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
}
