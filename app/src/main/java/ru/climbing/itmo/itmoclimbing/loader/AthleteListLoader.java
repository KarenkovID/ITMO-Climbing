package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.model.Athlete;

/**
 * Created by Игорь on 20.12.2016.
 */

public class AthleteListLoader extends AsyncTaskLoader<LoadResult<ArrayList<Athlete>>> {
    public AthleteListLoader(Context context) {
        super(context);
    }

    @Override
    public LoadResult<ArrayList<Athlete>> loadInBackground() {
        return null;
    }
}
