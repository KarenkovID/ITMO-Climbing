package ru.climbing.itmo.itmoclimbing.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import ru.climbing.itmo.itmoclimbing.callbacks.OnSelectListItemListener;
import ru.climbing.itmo.itmoclimbing.fragments.adpters.RoutesRecyclerAdapter;
import ru.climbing.itmo.itmoclimbing.loader.LoadResult;
import ru.climbing.itmo.itmoclimbing.loader.ResultType;
import ru.climbing.itmo.itmoclimbing.loader.RoutesLoader;
import ru.climbing.itmo.itmoclimbing.model.Route;

public class RoutesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<LoadResult<ArrayList<Route>>>,
        SwipeRefreshLayout.OnRefreshListener,
        OnSelectListItemListener{
    public static final String TAG = RoutesFragment.class.getSimpleName();
    private static String ROUTES_LIST_TAG = "routesList";

    public static final int LOADER_ID = 0;

    private RecyclerView rvRoutes;
    private ProgressBar progressBar;
    private RoutesRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvErrorMessage;

    private ArrayList<Route> mRoutesList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRoutesList = savedInstanceState.getParcelableArrayList(ROUTES_LIST_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_routes, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(rootView, savedInstanceState);

        rvRoutes = (RecyclerView) rootView.findViewById(R.id.rvRoutes);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tvErrorMessage = (TextView) rootView.findViewById(R.id.tvError);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // делаем повеселее
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);

        if (recyclerAdapter == null) {
            Log.d(TAG, "onViewCreated: recyclerAdapter = null");
            recyclerAdapter = new RoutesRecyclerAdapter(getContext(), this);
        }
        rvRoutes.setAdapter(recyclerAdapter);

        if (mLayoutManager == null) {
            Log.d(TAG, "onViewCreated: mLayoutManager = null");
            mLayoutManager = new LinearLayoutManager(getContext());
        } else {
            Parcelable layoutManagerState = mLayoutManager.onSaveInstanceState();
            mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.onRestoreInstanceState(layoutManagerState);
        }
        rvRoutes.setLayoutManager(mLayoutManager);

        if (mRoutesList == null) {
            Log.d(TAG, "onViewCreated: mRoutesList = null");
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            recyclerAdapter.setRoutesData(mRoutesList);
            rvRoutes.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Loader<LoadResult<ArrayList<Route>>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.GONE);
        rvRoutes.setVisibility(View.GONE);
        return new RoutesLoader(getContext(), null);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<ArrayList<Route>>> loader, LoadResult<ArrayList<Route>> data) {
        Log.d(TAG, "onLoadFinished");
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        // FIXME: 22.12.2016 Chenge result type
        if (data.resultType == ResultType.OK || data.resultType == ResultType.NO_INTERNET_LOADED_FROM_CACHE) {
            Log.d(TAG, "onLoadFinished: loading is done");
            mRoutesList = data.data;
            recyclerAdapter.setRoutesData(mRoutesList);
            tvErrorMessage.setVisibility(View.GONE);
            rvRoutes.setVisibility(View.VISIBLE);
        } else {
            //TODO: error message + button
            Log.d(TAG, "onLoadFinished: data doesn't downloaded");
            Log.d(TAG, data.resultType.toString());
            if (data.resultType == ResultType.NO_INTERNET_LOADED_FROM_CACHE) {
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
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ROUTES_LIST_TAG, mRoutesList);
    }

    @Override
    public void onClick(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        RouteDetailsFragment fragment =
                RouteDetailsFragment.newInstance(mRoutesList.get(position).id);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}