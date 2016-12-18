package ru.climbing.itmo.itmoclimbing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;

/**
 * Created by Игорь on 16.12.2016.
 */

public class SelectCompetitionFragment extends Fragment implements
        View.OnClickListener{
    private static final int REQUEST_COMPETITION_NAME_TAG = 1;
    private static final int REQUEST_ANOTHER_ONE = 2;

    public static final String COMPETITIONS_ARRAY_LIST_TAG = "competitionsArrayList";

    public static final String TAG = SelectCompetitionFragment.class.getSimpleName();

    private FloatingActionButton fabAddCompetition;
    private AddCompetitionDialogFragment mAddCompetitionDialog;
    private RecyclerView rvCompetitions;

    private ArrayList<CompetitionsEntry> mCompetitionsList;

    private CompetitionsRecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCompetitionsList = new ArrayList<CompetitionsEntry>();
        return inflater.inflate(R.layout.fragment_select_competition, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "onViewCreated: savedInstance != null");
            mCompetitionsList = savedInstanceState.getParcelableArrayList(COMPETITIONS_ARRAY_LIST_TAG);
        } else {
            mCompetitionsList = new ArrayList<CompetitionsEntry>();
        }
        fabAddCompetition = (FloatingActionButton) view.findViewById(R.id.fabAddCompetition);
        fabAddCompetition.setOnClickListener(this);
        rvCompetitions = (RecyclerView) view.findViewById(R.id.rvCompetitions);

        mRecyclerAdapter = new CompetitionsRecyclerAdapter(
                mCompetitionsList, LayoutInflater.from(getContext()));
        mLayoutManager = new LinearLayoutManager(getContext());

        rvCompetitions.setAdapter(mRecyclerAdapter);
        rvCompetitions.setLayoutManager(mLayoutManager);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddCompetition) {
            Log.d(TAG, "onClick: fabAddCompetition");
            if (mAddCompetitionDialog == null) {
                mAddCompetitionDialog = new AddCompetitionDialogFragment();
            }
            mAddCompetitionDialog.setTargetFragment(this, REQUEST_COMPETITION_NAME_TAG);
            mAddCompetitionDialog.show(
                    getFragmentManager(), mAddCompetitionDialog.getClass().getName());
        }
    }

    /**
     * Вызывается после закрытия диалогового окна
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: RESULT_OK");
            switch (requestCode) {
                case REQUEST_COMPETITION_NAME_TAG:
                    addCompetition(data.getStringExtra(AddCompetitionDialogFragment.COMPETITION_NAME_TAG));
                    break;
                case REQUEST_ANOTHER_ONE:
                    break;
                //обработка других requestCode
            }
        } else if (requestCode == AppCompatActivity.RESULT_CANCELED) {
            Log.d(TAG, "onActivityResult: RESULT_FIRST_USER");
            switch (requestCode){
                case REQUEST_COMPETITION_NAME_TAG:
                    addCompetition(data.getStringExtra(AddCompetitionDialogFragment.COMPETITION_NAME_TAG));
                    break;
            }
        } else {

        }
    }

    private void addCompetition(@Nullable String competitionName) {
        if (competitionName == null || competitionName.isEmpty()) {
            return;
        }
        Log.d(TAG, "addCompetition: " + competitionName);
        mCompetitionsList.add(new CompetitionsEntry(competitionName, "", true, null, null));
        mRecyclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "addCompetition: competitions count " + mRecyclerAdapter.getItemCount());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(COMPETITIONS_ARRAY_LIST_TAG, mCompetitionsList);
    }

    public static class CompetitionsRecyclerAdapter extends RecyclerView.Adapter<CompetitionsRecyclerAdapter.CompetitionVH> {
        @NonNull
        private ArrayList<CompetitionsEntry> mCompetitionData;
        @NonNull
        private LayoutInflater mLayoutInflater;

        public CompetitionsRecyclerAdapter (@NonNull ArrayList<CompetitionsEntry> data,
                                            @NonNull LayoutInflater layoutInflater) {
            mCompetitionData = data;
            mLayoutInflater = layoutInflater;
        }

        @Override
        public CompetitionVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return  new CompetitionVH(mLayoutInflater.inflate(R.layout.competition_card, parent, false));
        }

        @Override
        public void onBindViewHolder(CompetitionVH holder, int position) {
            holder.tvCompetitionName.setText(mCompetitionData.get(position).competitionName);
        }

        @Override
        public int getItemCount() {
            return mCompetitionData.size();
        }


        public static class CompetitionVH extends RecyclerView.ViewHolder {
            private TextView tvCompetitionName;

            public CompetitionVH(View itemView) {
                super(itemView);
                tvCompetitionName = (TextView) itemView.findViewById(R.id.tvCompetitionName);
            }
        } 
    }
}
