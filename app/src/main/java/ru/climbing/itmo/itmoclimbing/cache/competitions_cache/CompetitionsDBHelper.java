package ru.climbing.itmo.itmoclimbing.cache.competitions_cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.climbing.itmo.itmoclimbing.cache.utils.DBCorruptionHandler;

import static ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCacheContract.CompetitionsCached.CREATE_COMPETITIONS_TABLE;

/**
 * Данная база данных предназначена для храние информации о соревнованиях
 */

public class CompetitionsDBHelper extends SQLiteOpenHelper {

    public static final String TAG = CompetitionsDBHelper.class.getSimpleName();

    private static final String DB_FILE_NAME = "competitions.db";
    private static volatile CompetitionsDBHelper instance;


    public CompetitionsDBHelper(Context context) {
        super(context, DB_FILE_NAME, null, 1, // TODO fix version
                new DBCorruptionHandler(context, DB_FILE_NAME));
    }

    public static CompetitionsDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (CompetitionsDBHelper.class) {
                if (instance == null) {
                    instance = new CompetitionsDBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: sql db");
        db.execSQL(CREATE_COMPETITIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
