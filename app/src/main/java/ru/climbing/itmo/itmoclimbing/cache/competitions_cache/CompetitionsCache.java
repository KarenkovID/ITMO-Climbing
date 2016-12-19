package ru.climbing.itmo.itmoclimbing.cache.competitions_cache;

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

import ru.climbing.itmo.itmoclimbing.cache.utils.CompParse;
import ru.climbing.itmo.itmoclimbing.loader.BadResponseException;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsRoutesEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitorEntry;
;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_ROUTES;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_TYPE;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITORS;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.IS_ACTIVE;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionsCache {
    @NonNull
    private final Context context;

    private static ArrayList<CompetitionsEntry> mCompetitionsList;
    private static ArrayList<ArrayList<CompetitionsRoutesEntry>> mCompetitionsRouuteList;
    private static ArrayList<ArrayList<CompetitorEntry>> mCompetitorsList;

    private void getEntry(SQLiteStatement insert, int competitionPos) throws JSONException {
        insert.bindString(1, mCompetitionsList.get(competitionPos).competitionName);
        insert.bindString(2, mCompetitionsList.get(competitionPos).competitionType);
        insert.bindLong(3, mCompetitionsList.get(competitionPos).isActive ? 1 : 0);
        insert.bindString(4, CompParse.parseCompRoutesToString(
                mCompetitionsRouuteList.get(competitionPos)));
        insert.bindString(5, CompParse.parseCompetitorsToString(mCompetitorsList.get(competitionPos)));
    }

    @AnyThread
    public CompetitionsCache(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    @NonNull
    public List<CompetitionsEntry> get()
            throws FileNotFoundException {
        // FIXME: 19.12.2016 
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = COMPETITION_COMPONENTS;
        List<CompetitionsEntry> compTable = new ArrayList<>();


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
                    int isActive = cursor.getInt(2);
                    boolean b = (isActive == 1);
                    ArrayList<CompetitionsRoutesEntry> competitionsRoutes = CompParse.parseCompRoutesToArray(cursor.getString(3));
                    ArrayList<CompetitorEntry> competitors = CompParse.parseCompetitorsToArray(cursor.getString(4));
                    compTable.add(new CompetitionsEntry(competitionName, competitionType, b));
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        } catch (BadResponseException | JSONException e) {
            e.printStackTrace();
        }
        return compTable;
    }

    @WorkerThread
    public void put(@NonNull List<CompetitionsEntry> compTable) {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        String insertion = "INSERT INTO " + CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE + " ("
                + COMPETITION_NAME + ", "
                + COMPETITION_TYPE + ", "
                + IS_ACTIVE + ", "
                + COMPETITION_ROUTES + ", "
                + COMPETITORS;
        insertion += ") VALUES(?, ?, ?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (int i = 0; i < mCompetitionsList.size(); i++) {
                getEntry(insert, i);
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static final String TAG = "CompTableCache";
}
