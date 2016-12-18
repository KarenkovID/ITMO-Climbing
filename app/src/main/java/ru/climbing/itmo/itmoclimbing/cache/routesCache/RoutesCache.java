package ru.climbing.itmo.itmoclimbing.cache.routesCache;

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

import ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsDBHelper;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsRoutesEntry;

import static ru.climbing.itmo.itmoclimbing.cache.routesCache.RoutesCacheContract.RoutesCached.ROUTES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.routesCache.RoutesCacheContract.routsCacheColumns.ROUTE_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.routesCache.RoutesCacheContract.routsCacheColumns.ROUTE_ID;
import static ru.climbing.itmo.itmoclimbing.cache.routesCache.RoutesCacheContract.routsCacheColumns.ROUTE_LEVEL;
import static ru.climbing.itmo.itmoclimbing.cache.routesCache.RoutesCacheContract.routsCacheColumns.ROUTE_NAME;

/**
 * Created by macbook on 18.12.16.
 */

public class RoutesCache {
    @NonNull
    private final Context context;


    private void getEntry(SQLiteStatement insert, CompetitionsRoutesEntry entry) {
        insert.bindString(1, entry.routeName);
        insert.bindDouble(2, entry.routeFactor);
        insert.bindLong(3, entry.id);
    }

    @AnyThread
    public RoutesCache(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    @NonNull
    public List<CompetitionsEntry> get()
            throws FileNotFoundException {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = ROUTE_COMPONENTS;
        List<CompetitionsEntry> comptable = new ArrayList<>();
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
                    String competitionName = cursor.getString(0);
                    String competitionType = cursor.getString(1);
                    String isActive = cursor.getString(2);
                    String competitionsRoutes = cursor.getString(3);
                    String competitors = cursor.getString(4);
                    comptable.add(new CompetitionsEntry(competitionName, competitionType,
                            isActive, competitionsRoutes, competitors));
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        }
        return comptable;
    }

    @WorkerThread
    public void put(@NonNull List<CompetitionsRoutesEntry> compTable) {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        String insertion = "INSERT INTO " + RoutesCacheContract.RoutesCached.ROUTES_TABLE + " ("
                + ROUTE_NAME + ", "
                + ROUTE_LEVEL + ", "
                + ROUTE_ID + ", ";
        insertion += ") VALUES(?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (CompetitionsRoutesEntry entry : compTable) {
                getEntry(insert, entry);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static final String TAG = "ComptableCache";
}
