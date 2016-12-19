package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;

/**
 * Created by Игорь on 18.12.2016.
 */

public class CompetitionInfoFragment extends Fragment {
    public static final String TAG = CompetitionInfoFragment.class.getSimpleName();

    public static final String COMPETITION_DATA_TAG = "competitionData";

    private CompetitionsEntry mCompetitionData;

    @Deprecated
    public CompetitionInfoFragment(){}

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
        // FIXME: 19.12.2016
//        bundle.putParcelable(COMPETITION_DATA_TAG, competitionData);
        resFragment.setArguments(bundle);
        return resFragment;
    }
}
