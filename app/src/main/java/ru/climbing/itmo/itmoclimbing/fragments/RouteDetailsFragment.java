package ru.climbing.itmo.itmoclimbing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.cache.routes_and_athletes_cache.RoutesAndAthletesCache;
import ru.climbing.itmo.itmoclimbing.model.Athlete;
import ru.climbing.itmo.itmoclimbing.model.AthleteRouteResult;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 27.12.2016.
 */

public class RouteDetailsFragment extends Fragment {
    public static final String TAG = RouteDetailsFragment.class.getSimpleName();
    public static final String Route_ID_TAG = "routeID";

    private RecyclerView rvLastTops;

    private int routeID;
    private ArrayList<AthleteRouteResult> athletesSolvedRoute;

    private RoutesAndAthletesCache cache;
    private LastTopsRecyclerAdapter mLastTopsAdapter;
    private RecyclerView.LayoutManager mLastTopsLayoutManager;
    private Toolbar mToolbar;

    private TextView tvRouteAuthor;
    private TextView tvGrade;
    private TextView tvGradeCost;
    private TextView tvDescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeID = getArguments().getInt(Route_ID_TAG);

        cache = new RoutesAndAthletesCache(getContext());
        try {
            athletesSolvedRoute = cache.getAthletesForRoute(routeID);
            Log.d(TAG, "onCreate: athletes  loaded from cashe");
        } catch (FileNotFoundException e) {
            athletesSolvedRoute = new ArrayList<>();
            Log.e(TAG, "onCreate: can not load athletes from cache", e);
        }
        if (athletesSolvedRoute.size() == 0) {
            athletesSolvedRoute.add(new AthleteRouteResult(-1, -1,"",  getString(R.string.nothing_here)));
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mLastTopsAdapter == null) {
            mLastTopsAdapter = new LastTopsRecyclerAdapter(LayoutInflater.from(getContext()), athletesSolvedRoute);
        }
        mLastTopsLayoutManager = new GridLayoutManager(getContext(), 1);

        rvLastTops = (RecyclerView) view.findViewById(R.id.rvLastRoutes);
        rvLastTops.setAdapter(mLastTopsAdapter);
        rvLastTops.setLayoutManager(mLastTopsLayoutManager);

        mToolbar = (Toolbar) view.findViewById(R.id.tbRouteName);
        tvRouteAuthor = (TextView) view.findViewById(R.id.tvRouteAuthor);
        tvGrade = (TextView) view.findViewById(R.id.tvGrade);
        tvGradeCost = (TextView) view.findViewById(R.id.tvGradeCost);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        try {
            Route route = cache.getRoute(routeID);
            tvRouteAuthor.setText(route.author);
            tvGrade.setText(route.grade.grade);
            tvGradeCost.setText(String.valueOf(route.grade.cost));
            tvDescription.setText(route.description);
            mToolbar.setTitle(cache.getRouteName(routeID));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "onViewCreated: set toolbar title", e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private static class LastTopsRecyclerAdapter extends RecyclerView.Adapter<LastTopsRecyclerAdapter.AthleteVH> {
        private ArrayList<AthleteRouteResult> data;
        private LayoutInflater inflater;

        public LastTopsRecyclerAdapter(LayoutInflater inflater, ArrayList<AthleteRouteResult> data) {
            this.data = data;
            this.inflater = inflater;
        }

        @Override
        public AthleteVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AthleteVH(inflater.inflate(R.layout.athlete_list_item, null));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(AthleteVH holder, int position) {
            holder.tvAthleteName.setText(data.get(position).athleteName);
            holder.tvResultRemark.setText(data.get(position).remark);
        }

        public static class AthleteVH extends RecyclerView.ViewHolder {
            public final TextView tvAthleteName;
            public final TextView tvResultRemark;

            public AthleteVH(View itemView) {
                super(itemView);
                tvAthleteName = (TextView) itemView.findViewById(R.id.tvAthleteName);
                tvResultRemark = (TextView) itemView.findViewById(R.id.tvResultRemark);
            }
        }
    }

    public static RouteDetailsFragment newInstance(int routeID) {
        RouteDetailsFragment fragment = new RouteDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(Route_ID_TAG, routeID);
        fragment.setArguments(arguments);
        return fragment;
    }
}
