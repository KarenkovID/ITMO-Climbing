package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesAndAthletesCache;
import ru.climbing.itmo.itmoclimbing.model.AthleteRouteResult;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthleteDetailsFragment extends Fragment{
    public static final String TAG = AthleteDetailsFragment.class.getSimpleName();
    public static final String ATHLETE_ID_TAG = "athleteID";
    
    private int athleteID;
    private ArrayList<AthleteRouteResult> solvedRoutes;
    private ArrayList<Route> madeRoutes;
    private int raitingPosition;
    private RoutesAndAthletesCache cache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            athleteID = getArguments().getInt(ATHLETE_ID_TAG);
        } else {
            // TODO: 27.12.2016  
        }
        cache = new RoutesAndAthletesCache(getContext());
        try {
            solvedRoutes = cache.getAthletesSolvedRoutes(athleteID);
            Log.d(TAG, "onCreate: athlete routes loaded from cashe");
        } catch (FileNotFoundException e) {
            solvedRoutes = new ArrayList<>();
            Log.e(TAG, "onCreate: can not load athlete route from cache", e);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_athlete_detail_info, container, false);
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
}
