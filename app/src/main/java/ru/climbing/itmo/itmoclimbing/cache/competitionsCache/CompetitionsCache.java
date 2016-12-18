//package ru.climbing.itmo.itmoclimbing.cache.competitionsCache;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteStatement;
//import android.support.annotation.AnyThread;
//import android.support.annotation.NonNull;
//import android.support.annotation.WorkerThread;
//import android.util.Log;
//import android.util.TimeUtils;
//
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//
//import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;
//import solid.collectors.ToJoinedString;
//import solid.stream.Stream;
//
//import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.CompetitionsCached.COMPETITIONS_TABLE;
//import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.competitionsCacheColumns.COMPETITION_COMPONENTS;
//
///**
// * Created by macbook on 16.12.16.
// */
//
//public class CompetitionsCache {
//    @NonNull
//    private final Context context;
//
//
//    private void getEntry(SQLiteStatement insert, CompetitionsEntry entry, Calendar date) {
//        insert.bindString(1, entry.competitionName);
//        insert.bindString(2, entry.competitionType);
//        insert.bindLong(3, entry.competitionTime.getTimeInMillis());
//        insert.bindString(4, entry.isActive);
//    }
//
//    @AnyThread
//    public CompetitionsCache(@NonNull Context context) {
//        this.context = context.getApplicationContext();
//    }
//
//    @WorkerThread
//    @NonNull
//    public List<CompetitionsEntry> get()
//            throws FileNotFoundException {
//        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getReadableDatabase();
//        String[] projection = COMPETITION_COMPONENTS;
//        List<CompetitionsEntry> comptable = new ArrayList<>();
//
//
//        try (Cursor cursor = db.query(
//                COMPETITIONS_TABLE,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null)) {
////            if (cursor != null && cursor.moveToFirst()) {
//                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
//                    String competitionName = cursor.getString(0);
//                    String competitionType = cursor.getString(1);
//                    Calendar competitionTime = getCurrentTime(cursor.getLong(2));
//                    String isActive = cursor.getString(3);
//                    comptable.add(new CompetitionsEntry(competitionName, competitionType, competitionTime, isActive));
//                }
//            } else {
//                throw new FileNotFoundException("!!!");
//            }
//        } catch (SQLiteException e) {
//            Log.wtf(TAG, "Query error: ", e);
//            throw new FileNotFoundException("...");
//        }
//        return comptable;
//    }
//
//    @WorkerThread
//    public void put(@NonNull Calendar dateMsk,
//                    @NonNull List<CompetitionsEntry> comptable) {
//        SQLiteDatabase db = CompetitionsDBHelper.getInstance(context).getWritableDatabase();
//        db.beginTransaction();
//
//        Stream<String> componentStream = Stream.stream(COMPETITION_COMPONENTS);
//
//        String columnNames = componentStream.collect(ToJoinedString.toJoinedString(", "));
//        String questions = componentStream.map(value -> "?")
//                .collect(ToJoinedString.toJoinedString(", "));
//        String insertion = String.format("INSERT INTO %s (%s) VALUES (%s)",
//                COMPETITIONS_TABLE, columnNames, questions);
//
//        try (SQLiteStatement insert = db.compileStatement(insertion)) {
//            for (CompetitionsEntry entry : comptable) {
//                getEntry(insert, entry, dateMsk);
//                insert.executeInsert();
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    private Calendar getCurrentTime(long time) {
//        Calendar calendar = Calendar.getInstance(TimeUtils.getMskTimeZone());
//        calendar.setTime(new Date(time));
//        return calendar;
//    }
//
//    private long getDate(Calendar date) {
//        return date.get(Calendar.DAY_OF_YEAR) + date.get(Calendar.YEAR) * 500;
//    }
//
//    private static final String TAG = "ComptableCache";
//}
