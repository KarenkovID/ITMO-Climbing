package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.adpters.RoutesRecyclerAdapter;
import ru.climbing.itmo.itmoclimbing.loader.LoadResult;
import ru.climbing.itmo.itmoclimbing.loader.ResultType;
import ru.climbing.itmo.itmoclimbing.loader.RoutesLoader;
import ru.climbing.itmo.itmoclimbing.model.Route;

public class RoutesFragment extends Fragment implements LoaderManager.LoaderCallbacks<LoadResult<ArrayList<Route>>>{
    private static final String TAG = RoutesFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView rvRoutes;
    private ProgressBar progressBar;
    private RoutesRecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.routes_fragment, container, false);
        rvRoutes = (RecyclerView) rootView.findViewById(R.id.rvRoutes);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        recyclerAdapter = new RoutesRecyclerAdapter(getContext());
        rvRoutes.setAdapter(recyclerAdapter);
        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
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
        if (data.resultType == ResultType.OK) {
            recyclerAdapter.setRoutesData(data.data);
            rvRoutes.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, data.resultType.toString());
            //TODO: error message + button
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<ArrayList<Route>>> loader) {

    }
}