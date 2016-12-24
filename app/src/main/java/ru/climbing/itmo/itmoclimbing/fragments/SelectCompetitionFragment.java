package ru.climbing.itmo.itmoclimbing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.CompetitionManagerActivity;
import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.cache.competitions_cache.CompetitionsCache;
import ru.climbing.itmo.itmoclimbing.callbacks.OnSelectListItem;
import ru.climbing.itmo.itmoclimbing.model.CompetitionsEntry;

/**
 * Created by Игорь on 16.12.2016.
 */

public class SelectCompetitionFragment extends Fragment implements
        View.OnClickListener, OnSelectListItem {
    public static final int REQUEST_COMPETITION_NAME_TAG = 1;
    public static final int REQUEST_ANOTHER_ONE = 2;

    public static final String COMPETITIONS_ARRAY_LIST_TAG = "competitionsArrayList";

    public static final String TAG = SelectCompetitionFragment.class.getSimpleName();

    private FloatingActionButton fabAddCompetition;
    private AddCompetitionDialogFragment mAddCompetitionDialog;
    private RecyclerView rvCompetitions;

    private ArrayList<CompetitionsEntry> mCompetitionsList;

    private CompetitionsRecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CompetitionsCache mCompetitionsCache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompetitionsCache = new CompetitionsCache(getContext());
        if (savedInstanceState != null) {
            Log.d(TAG, "onViewCreated: savedInstance != null");
            mCompetitionsList = savedInstanceState.getParcelableArrayList(COMPETITIONS_ARRAY_LIST_TAG);
        } else {
            mCompetitionsList = new ArrayList<CompetitionsEntry>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + TAG);
        return inflater.inflate(R.layout.fragment_select_competition, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if (mCompetitionsList == null) {

        }

        fabAddCompetition = (FloatingActionButton) view.findViewById(R.id.fabAddCompetition);
        fabAddCompetition.setOnClickListener(this);
        rvCompetitions = (RecyclerView) view.findViewById(R.id.rvCompetitions);

        if (mRecyclerAdapter == null) {
            mRecyclerAdapter = new CompetitionsRecyclerAdapter(
                    mCompetitionsList, LayoutInflater.from(getContext()), this);
        }
        Log.d(TAG, "onViewCreated: mLayoutManager == null " + (mLayoutManager == null));
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(getContext());
        } else {
            Parcelable layoutManagerState = mLayoutManager.onSaveInstanceState();
            mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.onRestoreInstanceState(layoutManagerState);
        }

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
            mAddCompetitionDialog.show(
                    getChildFragmentManager(), mAddCompetitionDialog.getClass().getName());
        }
    }

    /**
     * Вызывается из диалогового окна, после нажатия на одну из кнопок
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: result from dialog was returned");
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
            switch (requestCode) {
                case REQUEST_COMPETITION_NAME_TAG:
                    addCompetition(data.getStringExtra(AddCompetitionDialogFragment.COMPETITION_NAME_TAG));
                    break;
            }
        } else {

        }
    }

    private void addCompetition(@Nullable String competitionName) {
        Log.d(TAG, "addCompetition: String = \"" + String.valueOf(competitionName) + "\"");
        if (competitionName == null || competitionName.isEmpty()) {
            return;
        }
        Log.d(TAG, "addCompetition: " + competitionName);
        CompetitionsEntry newCompetition = new CompetitionsEntry(
                competitionName, "", true);
        // FIXME: 19.12.2016
        /////////////////////////////////////////////
        ArrayList<CompetitionsEntry> competitions = new ArrayList<CompetitionsEntry>();
        competitions.add(newCompetition);
        mCompetitionsCache.putCompetitions(competitions);
        ////////////////////////////////////////////
        mCompetitionsList.add(newCompetition);
        mRecyclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "addCompetition: competitions count " + mRecyclerAdapter.getItemCount());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(COMPETITIONS_ARRAY_LIST_TAG, mCompetitionsList);
    }

    /**
     * It call when calls from (@{@link CompetitionsRecyclerAdapter.CompetitionVH#onClick(View)})
     * holder when onItemClick
     * @param position
     */
    @Override
    public void onClick(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(CompetitionInfoFragment.TAG) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, CompetitionInfoFragment.newInstance(
                            mCompetitionsList.get(position)), CompetitionInfoFragment.TAG)
                    .addToBackStack(CompetitionManagerActivity.BACK_STACK_TAG)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    public static class CompetitionsRecyclerAdapter
            extends RecyclerView.Adapter<CompetitionsRecyclerAdapter.CompetitionVH> {
        @NonNull
        private ArrayList<CompetitionsEntry> mCompetitionData;
        @NonNull
        private LayoutInflater mLayoutInflater;
        @NonNull
        OnSelectListItem listener;

        public CompetitionsRecyclerAdapter(@NonNull ArrayList<CompetitionsEntry> data,
                                           @NonNull LayoutInflater layoutInflater,
                                           @NonNull OnSelectListItem listener) {
            this.listener = listener;
            mCompetitionData = data;
            mLayoutInflater = layoutInflater;
        }

        @Override
        public CompetitionVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CompetitionVH(mLayoutInflater.inflate(R.layout.competition_card, parent, false),
                    listener);
        }

        @Override
        public void onBindViewHolder(CompetitionVH holder, int position) {
            holder.tvCompetitionName.setText(mCompetitionData.get(position).competitionName);
        }

        @Override
        public int getItemCount() {
            return mCompetitionData.size();
        }


        public static class CompetitionVH extends RecyclerView.ViewHolder
                implements View.OnClickListener {
            private TextView tvCompetitionName;
            private OnSelectListItem listener;

            public CompetitionVH(View itemView, OnSelectListItem listener) {
                super(itemView);
                tvCompetitionName = (TextView) itemView.findViewById(R.id.tvCompetitionName);
                itemView.setOnClickListener(this);
                this.listener = listener;
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                listener.onClick(position);
            }
        }
    }
}
