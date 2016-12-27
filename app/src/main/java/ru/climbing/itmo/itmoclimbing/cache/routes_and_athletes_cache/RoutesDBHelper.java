package ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.climbing.itmo.itmoclimbing.cache.utils.DBCorruptionHandler;
import ru.climbing.itmo.itmoclimbing.model.Athlete;

import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.AthletesCacheContract.CREATE_ATHLETES_TABLE;
import static ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesCacheContract.CREATE_ROUTES_TABLE;

public class RoutesDBHelper extends SQLiteOpenHelper {

    public static final String TAG = RoutesDBHelper.class.getSimpleName();

    private static final String DB_FILE_NAME = "itmo_climbing.db";
    private static volatile RoutesDBHelper instance;


    public RoutesDBHelper(Context context) {
        super(context, DB_FILE_NAME, null, 1, // TODO fix version
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
        Log.d(TAG, "onCreate: " + "CREATE_ROUTES_TABLE");
        db.execSQL(CREATE_ROUTES_TABLE);
        db.execSQL(CREATE_ATHLETES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
