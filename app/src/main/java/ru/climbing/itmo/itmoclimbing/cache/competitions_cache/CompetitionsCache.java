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
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITORS_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITOR_IS_TOP;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITOR_LAST_HOLD;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITOR_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.FACTOR;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.HOLDS_COUNT;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.ROUTES_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.ROUTE_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_COMPONENTS;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_NAME;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.COMPETITION_TYPE;
import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCacheColumns.IS_ACTIVE;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionsCache {
    @NonNull
    private final Context context;

//    private static ArrayList<CompetitionsEntry> mCompetitionsList;
//    private static ArrayList<ArrayList<CompetitionsRoutesEntry>> mCompetitionsRouuteList;
//    private static ArrayList<ArrayList<CompetitorEntry>> mCompetitorsList;

    private void fillCompetitionStatement(SQLiteStatement insert,
                          CompetitionsEntry competition) throws JSONException {
        insert.bindString(1, competition.competitionName);
        insert.bindString(2, competition.competitionType);
        insert.bindLong(3, competition.isActive ? 1 : 0);
    }
    private void fillRouteStatement(SQLiteStatement insert,
                          CompetitionsRoutesEntry route) throws JSONException {
        insert.bindString(1, route.routeName);
        insert.bindDouble(2, route.routeFactor);
        insert.bindLong(3, route.holdsCount);
    }
    private void fillCompetitorStatement(SQLiteStatement insert,
                                    CompetitorEntry competitor) throws JSONException {
        insert.bindString(1, competitor.competitorName);
        insert.bindString(2, CompParse.parseRoutesDataToString(competitor.competitorsRouteResultData));

    }
    @AnyThread
    public CompetitionsCache(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    @NonNull
    public ArrayList<CompetitionsEntry> getCompetitionsList()
            throws FileNotFoundException {
        // FIXME: 19.12.2016 
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        String[] projection = COMPETITION_COMPONENTS;
        ArrayList<CompetitionsEntry> compTable = new ArrayList<>();


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
                    compTable.add(new CompetitionsEntry(competitionName, competitionType, b));
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
    public void putCompetitions(@NonNull List<CompetitionsEntry> competitionsList) {
        Log.d(TAG, "putCompetitions: ");
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        String insertion = "INSERT INTO " + CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE + " ("
                + COMPETITION_NAME + ", "
                + COMPETITION_TYPE + ", "
                + IS_ACTIVE;
        insertion += ") VALUES(?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            for (int i = 0; i < competitionsList.size(); i++) {
                fillCompetitionStatement(insert, competitionsList.get(i));
                insert.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void putRoutes (@NonNull CompetitionsRoutesEntry competitionsRoutesEntry, int competitionID) {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        String tableName = CompetitionsCacheContract.CompetitionsCached
                .getRoutesTableName(competitionID);
        db.execSQL(CompetitionsCacheContract.CompetitionsCached
                .getCommandCreateCompetitionsRoutesTable(competitionID));

        db.beginTransaction();
        String insertion = "INSERT INTO " + tableName + " ("
                + ROUTE_NAME + ", "
                + FACTOR + ", "
                + HOLDS_COUNT;
        insertion += ") VALUES(?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            fillRouteStatement(insert, competitionsRoutesEntry);
            insert.executeInsert();

            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<CompetitionsRoutesEntry> getRoutesList (int competitionID)
            throws FileNotFoundException {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        db.execSQL(CompetitionsCacheContract.CompetitionsCached
                .getCommandCreateCompetitionsRoutesTable(competitionID));
        String tableName = CompetitionsCacheContract.CompetitionsCached
                .getRoutesTableName(competitionID);
        String[] projection = ROUTES_COMPONENTS;
        ArrayList<CompetitionsRoutesEntry> routesTable = new ArrayList<>();
        try (Cursor cursor = db.query(
                tableName,
                projection,
                null,
                null,
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    String routeName = cursor.getString(0);
                    int routeFactor = cursor.getInt(1);
                    int routeHoldsCount = cursor.getInt(2);
                    routesTable.add(new CompetitionsRoutesEntry(routeName, routeFactor, routeHoldsCount));
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        }
        return routesTable;
    }

    public void putCompetitors (@NonNull CompetitorEntry competitorEntry, int competitionID) {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        String tableName = CompetitionsCacheContract.CompetitionsCached
                .getCompetitorsTableName(competitionID);
        db.execSQL(CompetitionsCacheContract.CompetitionsCached
                .getCommandCreateCompetitorsTable(competitionID));

        db.beginTransaction();
        String insertion = "INSERT INTO " + tableName + " ("
                + COMPETITOR_NAME + ", "
                + COMPETITOR_LAST_HOLD + ", "
                + COMPETITOR_IS_TOP;
        insertion += ") VALUES(?, ?, ?)";

        try (SQLiteStatement insert = db.compileStatement(insertion)) {
            fillCompetitorStatement(insert, competitorEntry);
            insert.executeInsert();

            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    public ArrayList<CompetitorEntry> getCompetitorList (int competitionID)
            throws FileNotFoundException {
        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
        db.execSQL(CompetitionsCacheContract.CompetitionsCached
                .getCommandCreateCompetitorsTable(competitionID));
        String tableName = CompetitionsCacheContract.CompetitionsCached
                .getCompetitorsTableName(competitionID);
        String[] projection = COMPETITORS_COMPONENTS;
        ArrayList<CompetitorEntry> competitorTable = new ArrayList<>();
        try (Cursor cursor = db.query(
                tableName,
                projection,
                null,
                null,
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    String competitorName = cursor.getString(0);
                    ArrayList<CompetitorEntry.RouteResultData> competitorsRouteResultData
                            = CompParse.parseRouteResultDataToArray(cursor.getString(1));
                    int routeHoldsCount = cursor.getInt(2);
                    competitorTable.add(new CompetitorEntry(competitorName, competitorsRouteResultData.size()));
                }
            } else {
                throw new FileNotFoundException("!!!");
            }
        } catch (SQLiteException e) {
            Log.wtf(TAG, "Query error: ", e);
            throw new FileNotFoundException("...");
        } catch (BadResponseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return competitorTable;
    }



    private static final String TAG = "CompTableCache";

}
