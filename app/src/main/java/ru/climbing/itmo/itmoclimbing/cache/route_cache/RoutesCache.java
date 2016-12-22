package ru.climbing.itmo.itmoclimbing.cache.route_cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsDBHelper;
import ru.climbing.itmo.itmoclimbing.model.Route;

import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.RoutesCached.ROUTES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_AUTHOR;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_DESCRIPTION;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_GRADE;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_GRADE_COAST;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_IS_ACTIVE;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RoutesCacheContract.routeCacheColumns.ROUTE_NAME;

/**
 * Created by macbook on 18.12.16.
 */

public class RoutesCache {
    @NonNull
    private final Context context;


    private void fillStatement(SQLiteStatement insert, Route route) throws JSONException {
        insert.bindString(1, route.name);
        insert.bindString(2, route.author);
        insert.bindString(3, route.grade.grade);
        insert.bindLong(4, route.grade.cost);
        insert.bindString(5, route.description);
        insert.bindLong(6, route.isActive ? 1 : 0);
    }

    @AnyThread
    public RoutesCache(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    @NonNull
    public ArrayList<Route> get()
            throws FileNotFoundException {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
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
                    String routeName = cursor.getString(0);
                    String routeAuthor = cursor.getString(1);
                    String routeGrade = cursor.getString(2);
                    int routeGrateCoast = cursor.getInt(3);
                    String routeDescription = cursor.getString(4);
                    boolean routeIsActive = 1 == cursor.getInt(5);
                    compTable.add(new Route(routeName, routeGrade, routeGrateCoast, routeAuthor, routeDescription, routeIsActive));
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
    public void put(@NonNull List<Route> compTable) {
        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.execSQL("delete from "+ ROUTES_TABLE);
        String insertion = "INSERT INTO " + ROUTES_TABLE + " ("
                + ROUTE_NAME + ", "
                + ROUTE_AUTHOR + ", "
                + ROUTE_GRADE + ", "
                + ROUTE_GRADE_COAST + ", "
                + ROUTE_DESCRIPTION + ", "
                + ROUTE_IS_ACTIVE;
        insertion += ") VALUES(?, ?, ?, ?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (Route entry : compTable) {
                fillStatement(insert, entry);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    private static final String TAG = "RouteTableCache";
}
