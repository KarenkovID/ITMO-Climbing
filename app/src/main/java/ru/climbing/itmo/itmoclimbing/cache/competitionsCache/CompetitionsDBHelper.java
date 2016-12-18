package ru.climbing.itmo.itmoclimbing.cache.competitionsCache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.climbing.itmo.itmoclimbing.cache.utils.DBCorruptionHandler;

import static ru.climbing.itmo.itmoclimbing.cache.competitionsCache.CompetitionsCacheContract.CompetitionsCached.CREATE_COMPETITIONS_TABLE;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionsDBHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "competitions.db";
    private static volatile CompetitionsDBHelper instance;
    private Context context;


    public CompetitionsDBHelper(Context context) {
        super(context, DB_FILE_NAME, null,0, // TODO fix version
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
        db.execSQL(CREATE_COMPETITIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
