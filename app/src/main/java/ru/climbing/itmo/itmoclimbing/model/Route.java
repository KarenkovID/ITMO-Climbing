package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Информация о трассе, полученная из API
 */

public class Route implements Parcelable {


    public static class Grade implements Parcelable {
        public final String grade;
        public final int cost;

        public Grade(String grade, int cost) {
            this.grade = grade;
            this.cost = cost;
        }

        protected Grade(Parcel in) {
            grade = in.readString();
            cost = in.readInt();
        }

        public static final Creator<Grade> CREATOR = new Creator<Grade>() {
            @Override
            public Grade createFromParcel(Parcel in) {
                return new Grade(in);
            }

            @Override
            public Grade[] newArray(int size) {
                return new Grade[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(grade);
            dest.writeInt(cost);
        }
    }

    public final int id;

    /**
     * Название трассы
     */
    @NonNull
    public final String name;
    /**
     * Категория трассы
     */
    @NonNull
    public final Grade grade;
    /**
     * Описание трассы
     */
    @NonNull
    public final String description;
    /**
     * Автор трассы
     */
    @NonNull
    public final String author;

    /**
     * Накручена ли сейчас данная трасса
     */
    @NonNull
    public final boolean isActive;

    public Route(
            int id,
            @NonNull String name,
            @NonNull String grade,
            int cost,
            @NonNull String author,
            @NonNull String description,
            boolean isActive) {
        this.id = id;
        this.name = name;
        this.grade = new Grade(grade, cost);
        this.author = author;
        this.description = description;
        this.isActive = isActive;
    }

    protected Route(Parcel in) {
        id = in.readInt();
        name = in.readString();
        grade = in.readParcelable(Grade.class.getClassLoader());
        description = in.readString();
        author = in.readString();
        isActive = in.readByte() != 0;
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(grade, flags);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }
}
