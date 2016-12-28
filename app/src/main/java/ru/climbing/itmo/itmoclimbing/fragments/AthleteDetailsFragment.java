package ru.climbing.itmo.itmoclimbing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.zip.Inflater;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesAndAthletesCache;
import ru.climbing.itmo.itmoclimbing.model.AthleteRouteResult;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 21.12.2016.
 */

public class AthleteDetailsFragment extends Fragment {
    public static final String TAG = AthleteDetailsFragment.class.getSimpleName();
    public static final String ATHLETE_ID_TAG = "athleteID";

    private RecyclerView rvSolvedRoutes;
    private RecyclerView rvMadeRoutes;

    private int athleteID;
    private ArrayList<AthleteRouteResult> solvedRoutes;
    private ArrayList<Route> madeRoutes;
    private int raitingPosition;
    private RoutesAndAthletesCache cache;
    private Toolbar mToolbar;

    private SolvedRoutesRecyclerAdapter mSolvedRoutesAdapter;
    private MadeRoutesRecyclerAdapter mMadeRoutesAdapter;

    private RecyclerView.LayoutManager mSolvedRoutesManager;
    private RecyclerView.LayoutManager mMadeRoutesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        athleteID = getArguments().getInt(ATHLETE_ID_TAG);

        cache = new RoutesAndAthletesCache(getContext());
        try {
            solvedRoutes = cache.getAthletesSolvedRoutes(athleteID);
            madeRoutes = cache.getAthletesMadeRoutes(athleteID);
            Log.d(TAG, "onCreate: athlete routes loaded from cache");
        } catch (FileNotFoundException e) {
            solvedRoutes = new ArrayList<>();
            Log.e(TAG, "onCreate: can not load athlete route from cache", e);
        }
        if (solvedRoutes.size() == 0) {
            solvedRoutes.add(new AthleteRouteResult(-1, -1, -1, "", -1, getString(R.string.nothing_here), "", -1));
        }
        if (madeRoutes.size() == 0) {
            madeRoutes.add(new Route(-1, getString(R.string.nothing_here), "", -1, "", "", false));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_athlete_detail_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        if (mSolvedRoutesAdapter == null) {
            mSolvedRoutesAdapter = new SolvedRoutesRecyclerAdapter(LayoutInflater.from(getContext()), solvedRoutes);
        }
        mSolvedRoutesManager = new GridLayoutManager(getContext(), 2);

        rvSolvedRoutes = (RecyclerView) view.findViewById(R.id.rvSolvedRoutes);
        rvSolvedRoutes.setAdapter(mSolvedRoutesAdapter);
        rvSolvedRoutes.setLayoutManager(mSolvedRoutesManager);

        if (mMadeRoutesAdapter == null) {
            mMadeRoutesAdapter = new MadeRoutesRecyclerAdapter(LayoutInflater.from(getContext()), madeRoutes);
        }
        mMadeRoutesManager = new GridLayoutManager(getContext(), 2);

        rvMadeRoutes = (RecyclerView) view.findViewById(R.id.rvProducedRoutes);
        rvMadeRoutes.setAdapter(mMadeRoutesAdapter);
        rvMadeRoutes.setLayoutManager(mMadeRoutesManager);

        mToolbar = (Toolbar) view.findViewById(R.id.tbAthleteName);
        try {
            mToolbar.setTitle(cache.getAthleteName(athleteID));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "onViewCreated: set toolbar title", e);
        }
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

    private static class SolvedRoutesRecyclerAdapter extends RecyclerView.Adapter<SolvedRoutesRecyclerAdapter.RouteVH> {
        private ArrayList<AthleteRouteResult> data;
        private LayoutInflater inflater;

        public SolvedRoutesRecyclerAdapter(LayoutInflater inflater, ArrayList<AthleteRouteResult> data) {
            this.data = data;
            this.inflater = inflater;
        }

        @Override
        public RouteVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RouteVH(inflater.inflate(R.layout.route_list_item, null));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(RouteVH holder, int position) {
            holder.tvRouteName.setText(data.get(position).routeName);
            holder.tvRouteGrade.setText(data.get(position).routeGrade);
        }

        public static class RouteVH extends RecyclerView.ViewHolder {
            public final TextView tvRouteName;
            public final TextView tvRouteGrade;

            public RouteVH(View itemView) {
                super(itemView);
                tvRouteName = (TextView) itemView.findViewById(R.id.tvRouteName);
                tvRouteGrade = (TextView) itemView.findViewById(R.id.tvRouteGrade);
            }
        }
    }
    private static class MadeRoutesRecyclerAdapter extends RecyclerView.Adapter<MadeRoutesRecyclerAdapter.RouteVH> {
        private ArrayList<Route> data;
        private LayoutInflater inflater;

        public MadeRoutesRecyclerAdapter(LayoutInflater inflater, ArrayList<Route> data) {
            this.data = data;
            this.inflater = inflater;
        }

        @Override
        public RouteVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RouteVH(inflater.inflate(R.layout.route_list_item, null));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(RouteVH holder, int position) {
            holder.tvRouteName.setText(data.get(position).name);
            holder.tvRouteGrade.setText(data.get(position).grade.grade);
        }

        public static class RouteVH extends RecyclerView.ViewHolder {
            public final TextView tvRouteName;
            public final TextView tvRouteGrade;

            public RouteVH(View itemView) {
                super(itemView);
                tvRouteName = (TextView) itemView.findViewById(R.id.tvRouteName);
                tvRouteGrade = (TextView) itemView.findViewById(R.id.tvRouteGrade);
            }
        }
    }
}
