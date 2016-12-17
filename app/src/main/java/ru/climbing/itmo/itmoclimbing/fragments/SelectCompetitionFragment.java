package ru.climbing.itmo.itmoclimbing.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.callbacks.NoticeDialogListener;

/**
 * Created by Игорь on 16.12.2016.
 */

public class SelectCompetitionFragment extends Fragment implements
        View.OnClickListener,
        NoticeDialogListener<String> {
    public static final String TAG = SelectCompetitionFragment.class.getSimpleName();

    private FloatingActionButton fabAddCompetition;
    private DialogFragment mAddCompetitionDialog;

    private ArrayList mCompetitionsData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competitions_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        fabAddCompetition = (FloatingActionButton) view.findViewById(R.id.fabAddCompetition);
        fabAddCompetition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddCompetition) {
            Log.d(TAG, "onClick: fabAddCompetition");
            if (mAddCompetitionDialog == null) {
                mAddCompetitionDialog = new AddCompetitionDialogFragment();
            }
            mAddCompetitionDialog.show(getFragmentManager(), null);
        }
    }

    /**
     * Вызывается в процессе создания нового соревнования
     *
     * @param competitionName содержит название соревование, которое нужно создать
     */
    @Override
    public void onDialogPositiveClick(String competitionName) {
        addCompetition(competitionName);
    }

    private void addCompetition(String competitionName) {
        //TODO
    }

}
