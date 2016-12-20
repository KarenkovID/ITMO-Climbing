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
        return new AthleteVH(layoutInflater.inflate(R.layout.athlete_card, parent, false));
    }

    @Override
    public void onBindViewHolder(AthleteVH holder, int position) {
        Athlete athlete = athletesData.get(position);
        holder.tvFirstName.setText(athlete.firstName);
        holder.tvLastName.setText(athlete.lastName);
        holder.tvPosition.setText(String.valueOf(athlete.position));
    }

    public void setAthletesData(ArrayList<Athlete> athletesData) {
        this.athletesData = athletesData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return athletesData != null ? athletesData.size() : 0;
    }

    public static class AthleteVH extends RecyclerView.ViewHolder {
        private final ImageView ivPhoto;
        private final TextView tvFirstName;
        private final TextView tvLastName;
        private final TextView tvPosition;

        public AthleteVH(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvFirstName = (TextView) itemView.findViewById(R.id.tvFirstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tvLastName);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
        }
    }
}