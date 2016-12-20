package ru.climbing.itmo.itmoclimbing.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;

/**
 * Created by macbook on 16.12.16.
 */

public class CompetitionLoader extends AsyncTaskLoader<LoadResult<List<CompetitionsEntry>>> {

    private LoadResult<List<CompetitionsEntry>> lastResult;

    public CompetitionLoader(Context context) {
        super(context);
        // TODO cache
    }

    @Override
    public LoadResult<List<CompetitionsEntry>> loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        if (lastResult == null) {
            forceLoad();
        } else {
            deliverResult(lastResult);
        }
    }

    @Override
    public void deliverResult(LoadResult<List<CompetitionsEntry>> data) {
        lastResult = data;
        super.deliverResult(data);
    }
}
