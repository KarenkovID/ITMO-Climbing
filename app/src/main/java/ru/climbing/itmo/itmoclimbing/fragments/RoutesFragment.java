package ru.climbing.itmo.itmoclimbing.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import ru.climbing.itmo.itmoclimbing.adpters.RoutesRecyclerAdapter;
import ru.climbing.itmo.itmoclimbing.loader.LoadResult;
import ru.climbing.itmo.itmoclimbing.loader.ResultType;
import ru.climbing.itmo.itmoclimbing.loader.RoutesLoader;
import ru.climbing.itmo.itmoclimbing.model.Route;

public class RoutesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<LoadResult<ArrayList<Route>>>,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = RoutesFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView rvRoutes;
    private ProgressBar progressBar;
    private RoutesRecyclerAdapter recyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvErrorMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.routes_fragment, container, false);
        rvRoutes = (RecyclerView) rootView.findViewById(R.id.rvRoutes);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tvErrorMessage = (TextView) rootView.findViewById(R.id.tvError);
        recyclerAdapter = new RoutesRecyclerAdapter(getContext());
        rvRoutes.setAdapter(recyclerAdapter);
        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // делаем повеселее
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<LoadResult<ArrayList<Route>>> onCreateLoader(int id, Bundle args) {
        return new RoutesLoader(getContext(), null);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<ArrayList<Route>>> loader, LoadResult<ArrayList<Route>> data) {
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if (data.resultType == ResultType.OK) {
            Log.d(TAG, "onLoadFinished: loading is done");
            recyclerAdapter.setRoutesData(data.data);
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
    public void onLoaderReset(Loader<LoadResult<ArrayList<Route>>> loader) {

    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: try to refresh data");
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }
}