package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class CompetitionsEntry implements Parcelable{

    /**
     * Название соревнования
     */
    @NonNull
    public String competitionName;
    /**
     * Тип соревнования
     */
    @NonNull
    public String competitionType;
    /**
     * Идёт ли соревнование в данный момент
     */
    @NonNull
    public boolean isActive;
    /**
     * Уникальный ID при помощи которого мы будем находить список участников и список трасс
     */
    private final int competitionID;

    public CompetitionsEntry(@NonNull String competitionName,
                             @NonNull String competitionType,
                             boolean isActive) {
        this.competitionName = competitionName;
        this.competitionType = competitionType;
        this.isActive = isActive;
        // FIXME: 19.12.2016 генерация уникального ID
        this.competitionID = 0;
    }


    protected CompetitionsEntry(Parcel in) {
        competitionName = in.readString();
        competitionType = in.readString();
        isActive = in.readByte() != 0;
        competitionID = in.readInt();
    }

    public static final Creator<CompetitionsEntry> CREATOR = new Creator<CompetitionsEntry>() {
        @Override
        public CompetitionsEntry createFromParcel(Parcel in) {
            return new CompetitionsEntry(in);
        }

        @Override
        public CompetitionsEntry[] newArray(int size) {
            return new CompetitionsEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(competitionName);
        dest.writeString(competitionType);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(competitionID);
    }
}
