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
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RouteCacheContract.RoutesCached.ROUTES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RouteCacheContract.routeCacheColumns.ROUTE_AUTHOR;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RouteCacheContract.routeCacheColumns.ROUTE_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RouteCacheContract.routeCacheColumns.ROUTE_DESCRIPTION;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RouteCacheContract.routeCacheColumns.ROUTE_GRADE;
import static ru.climbing.itmo.itmoclimbing.cache.route_cache.RouteCacheContract.routeCacheColumns.ROUTE_NAME;

/**
 * Created by macbook on 18.12.16.
 */

import ru.climbing.itmo.itmoclimbing.model.Route;

public class RoutesCache {
//    @NonNull
//    private final Context context;
//
//
//    private void getEntry(SQLiteStatement insert, Route entry) throws JSONException {
//        insert.bindString(1, entry.name);
//        insert.bindString(2, entry.grade);
//        insert.bindString(3, entry.description);
//        insert.bindString(4, entry.author);
//    }
//
//    @AnyThread
//    public RoutesCache(@NonNull Context context) {
//        this.context = context.getApplicationContext();
//    }
//
//    @WorkerThread
//    @NonNull
//    public List<Route> get()
//            throws FileNotFoundException {
//        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
//        String[] projection = ROUTE_COMPONENTS;
//        List<Route> compTable = new ArrayList<>();
//
//
//        try (Cursor cursor = db.query(
//                ROUTES_TABLE,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null)) {
//            if (cursor != null && cursor.moveToFirst()) {
//                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
//                    String routeName = cursor.getString(0);
//                    String routeGrade = cursor.getString(1);
//                    String routeDescription = cursor.getString(2);
//                    String routeAuthor = cursor.getString(3);
//                    compTable.add(new Route(routeName, routeGrade, routeDescription, routeAuthor));
//                }
//            } else {
//                throw new FileNotFoundException("!!!");
//            }
//        } catch (SQLiteException e) {
//            Log.wtf(TAG, "Query error: ", e);
//            throw new FileNotFoundException("...");
//        }
//        return compTable;
//    }
//
//    @WorkerThread
//    public void put(@NonNull List<Route> compTable) {
//        SQLiteDatabase db = RoutesDBHelper.getInstance(context).getWritableDatabase();
//        db.beginTransaction();
//        String insertion = "INSERT INTO " + RouteCacheContract.RoutesCached.ROUTES_TABLE + " ("
//                + ROUTE_NAME + ", "
//                + ROUTE_GRADE + ", "
//                + ROUTE_DESCRIPTION + ", "
//                + ROUTE_AUTHOR;
//        insertion += ") VALUES(?, ?, ?, ?)";
//
//        try (SQLiteStatement insert = db.compileStatement(insertion)) {
//            for (Route entry : compTable) {
//                getEntry(insert, entry);
//                insert.executeInsert();
//            }
//            db.setTransactionSuccessful();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//
//    private static final String TAG = "RouteTableCache";
}
