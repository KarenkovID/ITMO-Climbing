package ru.climbing.itmo.itmoclimbing.cache.routesCache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.climbing.itmo.itmoclimbing.cache.DBCorruptionHandler;
import static ru.climbing.itmo.itmoclimbing.cache.routesCache.RoutesCacheContract.RoutesCached.ROUTES_TABLE;


/**
 * Created by macbook on 16.12.16.
 */

public class RoutesDBHelper extends SQLiteOpenHelper {
    private static final String DB_FILE_NAME = "competitions.db";
    private static volatile RoutesDBHelper instance;
    private Context context;


    public RoutesDBHelper(Context context) {
        super(context, DB_FILE_NAME, null,0, // TODO fix version
                new DBCorruptionHandler(context, DB_FILE_NAME));
    }

    public static RoutesDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (RoutesDBHelper.class) {
                if (instance == null) {
                    instance = new RoutesDBHelper(context);
                }
            }
        }
        return instance;
    }
    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ROUTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
