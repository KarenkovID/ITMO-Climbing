package ru.climbing.itmo.itmoclimbing.adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.climbing.itmo.itmoclimbing.R;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 05.12.2016.
 */

public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.RouteHolder> {

    private ArrayList<Route> routesData;
    private Context context;
    private LayoutInflater layoutInflater;

    public RoutesRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RouteHolder(layoutInflater.inflate(R.layout.route_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RouteHolder holder, int position) {
        Route route = routesData.get(position);
        holder.tvGrade.setText(route.grade.grade);
        holder.tvRouteName.setText(route.name);
    }

    public void setRoutesData (ArrayList<Route> routesData) {
        this.routesData = routesData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return routesData != null ? routesData.size() : 0;
    }

    public static class RouteHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMark;
        private final TextView tvRouteName;
        private final TextView tvGrade;
        public RouteHolder(View itemView) {
            super(itemView);
            ivMark = (ImageView) itemView.findViewById(R.id.ivMark);
            tvRouteName = (TextView) itemView.findViewById(R.id.tvRouteName);
            tvGrade = (TextView) itemView.findViewById(R.id.tvGrade);
        }
    }
}
