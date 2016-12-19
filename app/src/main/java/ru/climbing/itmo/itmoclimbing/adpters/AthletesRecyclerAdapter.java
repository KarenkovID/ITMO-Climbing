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
import ru.climbing.itmo.itmoclimbing.model.Athlete;
import ru.climbing.itmo.itmoclimbing.model.Route;

/**
 * Created by Игорь on 20.12.2016.
 */

public class AthletesRecyclerAdapter extends RecyclerView.Adapter<AthletesRecyclerAdapter.AthleteVH> {

    private ArrayList<Athlete> athletesData;
    private Context context;
    private LayoutInflater layoutInflater;

    public AthletesRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AthleteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteVH(layoutInflater.inflate(R.layout.route_card, parent, false));
    }

    @Override
    public void onBindViewHolder(AthleteVH holder, int position) {
        Athlete athlete = athletesData.get(position);
        holder.tvGrade.setText(route.grade.grade);
        holder.tvRouteName.setText(route.name);
    }

    public void setAthletesData(ArrayList<Athlete> routesData) {
        this.routesData = routesData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return routesData != null ? routesData.size() : 0;
    }

    public static class AthleteVH extends RecyclerView.ViewHolder {
        // FIXME: 20.12.2016
        private final ImageView ivMark;
        private final TextView tvRouteName;
        private final TextView tvGrade;

        public AthleteVH(View itemView) {
            super(itemView);
            ivMark = (ImageView) itemView.findViewById(R.id.ivMark);
            tvRouteName = (TextView) itemView.findViewById(R.id.tvRouteName);
            tvGrade = (TextView) itemView.findViewById(R.id.tvGrade);
        }
    }
}