package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesAndAthletesCache;
import ru.climbing.itmo.itmoclimbing.callbacks.ActionBarDrawerCallback;
import ru.climbing.itmo.itmoclimbing.loader.LoadResult;
import ru.climbing.itmo.itmoclimbing.loader.ResultType;
import ru.climbing.itmo.itmoclimbing.model.AthleteRouteResult;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthleteDetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<LoadResult<AthleteRouteResult>>{
    public static final String TAG = AthletesFragment.class.getSimpleName();
    public static final String ATHLETE_ID_TAG = "athleteID";
    
    private int athleteID;
    private ArrayList<AthleteRouteResult> solvedRoutes;
    private ArrayList<Route> madeRoutes;
    private int raitingPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            athleteID = getArguments().getInt(ATHLETE_ID_TAG);
        } else {
            // TODO: 27.12.2016  
        }
        RoutesAndAthletesCache cache = new RoutesAndAthletesCache(getContext());
        try {
            solvedRoutes = cache.getAthletesSolvedRoutes(athleteID);
            Log.d(TAG, "onCreate: athlete routes loaded from cashe");
        } catch (FileNotFoundException e) {
            solvedRoutes = new ArrayList<>();
            Log.e(TAG, "onCreate: can not load athlete route from cache", e);
        }
        try {
            ((ActionBarDrawerCallback) getActivity()).setTitleAndShowBackButton(cache.getAthleteName(athleteID));
        } catch (ClassCastException e) {
            Log.e(TAG, "onCreate: parent activity doesn't support ActionBarDrawerCallback", e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static AthleteDetailsFragment newInstance(int AthleteID) {
        AthleteDetailsFragment fragment = new AthleteDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ATHLETE_ID_TAG, AthleteID);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            ((ActionBarDrawerCallback)getActivity()).showDrawerButton();
        } catch (ClassCastException e) {
            Log.e(TAG, "onCreate: parent activity doesn't support ActionBarDrawerCallback", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            ((ActionBarDrawerCallback)getActivity()).showDrawerButton();
        } catch (ClassCastException e) {
            Log.e(TAG, "onCreate: parent activity doesn't support ActionBarDrawerCallback", e);
        }
    }

    @Override
    public Loader<LoadResult<AthleteRouteResult>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<AthleteRouteResult>> loader, LoadResult<AthleteRouteResult> data) {
        // TODO: 27.12.2016
        if (data.resultType == ResultType.NO_INTERNET_LOADED_FROM_CACHE) {

        } else if (data.resultType == ResultType.NO_INTERNET) {

        } else if (data.resultType == ResultType.ERROR) {

        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<AthleteRouteResult>> loader) {

    }
}
