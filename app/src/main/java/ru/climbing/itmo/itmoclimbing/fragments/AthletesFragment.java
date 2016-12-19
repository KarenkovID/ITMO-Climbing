package ru.climbing.itmo.itmoclimbing.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.adpters.AthletesRecyclerAdapter;
import ru.climbing.itmo.itmoclimbing.adpters.RoutesRecyclerAdapter;
import ru.climbing.itmo.itmoclimbing.loader.AthleteListLoader;
import ru.climbing.itmo.itmoclimbing.loader.LoadResult;
import ru.climbing.itmo.itmoclimbing.loader.ResultType;
import ru.climbing.itmo.itmoclimbing.loader.RoutesLoader;
import ru.climbing.itmo.itmoclimbing.model.Athlete;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 20.12.2016.
 */

public class AthletesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<LoadResult<ArrayList<Athlete>>>,
        SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = Athlete.class.getSimpleName();
    private static String ATHLETES_LIST_TAG = "routesList";

    private RecyclerView rvAthletes;
    private ProgressBar progressBar;
    private AthletesRecyclerAdapter recyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvErrorMessage;

    private ArrayList<Athlete> mAthletesList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAthletesList = savedInstanceState.getParcelableArrayList(ATHLETES_LIST_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_athletes, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(rootView, savedInstanceState);

        rvAthletes = (RecyclerView) rootView.findViewById(R.id.rvAthletes);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tvErrorMessage = (TextView) rootView.findViewById(R.id.tvError);

        recyclerAdapter = new AthletesRecyclerAdapter(getContext());
        rvAthletes.setAdapter(recyclerAdapter);
        // TODO: 20.12.2016 нужны ли для этого отдельные поля в классе?
        rvAthletes.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mAthletesList == null) {
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerAdapter.setRoutesData(mAthletesList);
            rvAthletes.setVisibility(View.VISIBLE);
        }



        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // делаем повеселее
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);
    }

    @Override
    public Loader<LoadResult<ArrayList<Athlete>>> onCreateLoader(int id, Bundle args) {
        return new AthleteListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<ArrayList<Athlete>>> loader, LoadResult<ArrayList<Athlete>> data) {
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if (data.resultType == ResultType.OK) {
            Log.d(TAG, "onLoadFinished: loading is done");
            mAthletesList = data.data;
            recyclerAdapter.setRoutesData(mRoutesList);
            tvErrorMessage.setVisibility(View.GONE);
            rvRoutes.setVisibility(View.VISIBLE);
        } else {
            //TODO: error message + button
            Log.d(TAG, "onLoadFinished: data doesn't downloaded");
            Log.d(TAG, data.resultType.toString());
            if (data.resultType == ResultType.NO_INTERNET) {
                Log.d(TAG, "onLoadFinished: no internet");
                tvErrorMessage.setText(R.string.no_internet);
            } else {
                Log.d(TAG, "onLoadFinished: something went wrong");
                tvErrorMessage.setText(R.string.error);
            }
            rvRoutes.setVisibility(View.GONE);
            tvErrorMessage.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<ArrayList<Athlete>>> loader) {

    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: try to refresh data");
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ROUTES_LIST_TAG, mRoutesList);
    }
}
