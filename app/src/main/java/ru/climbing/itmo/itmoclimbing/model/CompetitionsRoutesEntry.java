package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class CompetitionsRoutesEntry implements Parcelable{

    @NonNull
    public final String routeName;

    public double routeFactor;

    private int id;

    public int holdsCount;

    public CompetitionsRoutesEntry(@NonNull String routeName, double routeFactor, int holdsCount) {
        this.routeName = routeName;
        this.routeFactor = routeFactor;
        // FIXME: 19.12.2016 генерация ID
        this.id = 0;
        this.holdsCount = holdsCount;
    }

    protected CompetitionsRoutesEntry(Parcel in) {
        routeName = in.readString();
        routeFactor = in.readDouble();
        id = in.readInt();
        holdsCount = in.readInt();
    }

    public static final Creator<CompetitionsRoutesEntry> CREATOR = new Creator<CompetitionsRoutesEntry>() {
        @Override
        public CompetitionsRoutesEntry createFromParcel(Parcel in) {
            return new CompetitionsRoutesEntry(in);
        }

        @Override
        public CompetitionsRoutesEntry[] newArray(int size) {
            return new CompetitionsRoutesEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(routeName);
        dest.writeDouble(routeFactor);
        dest.writeInt(id);
        dest.writeInt(holdsCount);
    }
}
