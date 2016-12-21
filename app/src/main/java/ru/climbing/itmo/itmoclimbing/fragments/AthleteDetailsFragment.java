package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthleteDetailsFragment extends Fragment {
    public static final String TAG = AthletesFragment.class.getSimpleName();
    public static final String ATHLETE_ID_TAG = "athleteID";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public static AthleteDetailsFragment newInstence(int AthleteID) {
        AthleteDetailsFragment fragment = new AthleteDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ATHLETE_ID_TAG, AthleteID);
        fragment.setArguments(arguments);
        return fragment;
    }
}
