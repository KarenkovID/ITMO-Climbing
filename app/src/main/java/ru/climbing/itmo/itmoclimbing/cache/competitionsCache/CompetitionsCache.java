package ru.climbing.itmo.itmoclimbing.cache.competitionsCache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.TimeUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsRoutesEntry;


import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.competitionsCacheColumns.COMPETITION_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.competitionsCacheColumns.COMPETITION_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.competitionsCacheColumns.COMPETITION_TYPE;
import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.competitionsCacheColumns.IS_ACTIVE;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionsCache {
    @NonNull
    private final Context context;


    private void getEntry(SQLiteStatement insert, CompetitionsEntry entry) {
        insert.bindString(1, entry.competitionName);
        insert.bindString(2, entry.competitionType);
        insert.bindString(3, entry.isActive);
    }

    @AnyThread
    public CompetitionsCache(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    @NonNull
    public List<CompetitionsEntry> get()
            throws FileNotFoundException {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = COMPETITION_COMPONENTS;
        List<CompetitionsEntry> comptable = new ArrayList<>();


        try (Cursor cursor = db.query(
                COMPETITIONS_TABLE,
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
                    ArrayList<String> competitionsRoutes = (cursor.getString(3));
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
    public void put(@NonNull List<CompetitionsEntry> compTable) {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        String insertion = "INSERT INTO " + CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE + " ("
                + COMPETITION_NAME + ", "
                + COMPETITION_TYPE + ", "
                + IS_ACTIVE + ", ";
        insertion += ") VALUES(?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (CompetitionsEntry entry : compTable) {
                getEntry(insert, entry);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static final String TAG = "CompTableCache";
}
