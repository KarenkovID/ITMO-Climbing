package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Данный клас предназначен для хранения
 */

public class CompetitorEntry implements Parcelable{

    protected CompetitorEntry(Parcel in) {
        competitorName = in.readString();
    }

    public static final Creator<CompetitorEntry> CREATOR = new Creator<CompetitorEntry>() {
        @Override
        public CompetitorEntry createFromParcel(Parcel in) {
            return new CompetitorEntry(in);
        }

        @Override
        public CompetitorEntry[] newArray(int size) {
            return new CompetitorEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(competitorName);
    }

    public static class RouteResultData{
        public int lastHold;
        public boolean isTop;

        public RouteResultData() {}



        public RouteResultData(int lastHold, boolean isTop) {
            setResult(lastHold, isTop);
        }
        public void setResult (int lastHold, boolean isTop) {
            this.lastHold = lastHold;
            this.isTop = isTop;
        }
    }

    @NonNull
    public final String competitorName;

    @NonNull
    public ArrayList<RouteResultData> competitorsRouteResultData;

    public CompetitorEntry(@NonNull String competitorName,
                           int routesCount) {
        this.competitorName = competitorName;
        competitorsRouteResultData = new ArrayList<>(routesCount);
        for (int i = 0; i < competitorsRouteResultData.size(); ++i) {
            competitorsRouteResultData.add(new RouteResultData());
        }
    }
}
