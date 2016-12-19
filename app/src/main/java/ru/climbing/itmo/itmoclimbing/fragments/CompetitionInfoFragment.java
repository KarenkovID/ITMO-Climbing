package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsRoutesEntry;
import ru.climbing.itmo.itmoclimbing.model.CompetitorEntry;

/**
 * Created by Игорь on 18.12.2016.
 */

public class CompetitionInfoFragment extends Fragment {
    public static final String TAG = CompetitionInfoFragment.class.getSimpleName();

    public static final String COMPETITION_DATA_TAG = "competitionData";

    private CompetitionsEntry mCompetitionData;
    private ArrayList<CompetitionsRoutesEntry> mCompetitionRoutesData;
    private ArrayList<CompetitorEntry> mCompetitorsRoutesData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mCompetitionData = arguments.getParcelable(COMPETITION_DATA_TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competition_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null || savedInstanceState.getParcelable(COMPETITION_DATA_TAG) == null) {
            mCompetitionData = getArguments().getParcelable(COMPETITION_DATA_TAG);
        }
    }

    public static CompetitionInfoFragment newInstance(CompetitionsEntry competitionData) {
        CompetitionInfoFragment resFragment = new CompetitionInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COMPETITION_DATA_TAG, competitionData);
        resFragment.setArguments(bundle);
        return resFragment;
    }
}
