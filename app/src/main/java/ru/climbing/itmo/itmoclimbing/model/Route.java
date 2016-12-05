package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Информация о фильме, полученная из The Route DB API
 */

public class Route implements Parcelable{

    /**
     * Path изображения постера фильма. Как из Path получить URL, описано здесь:
     *
     * https://developers.themoviedb.org/3/getting-started/languages
     *
     * В рамках ДЗ можно не выполнять отдельный запрос /configuration, а использовать
     * базовый URL для картинок: http://image.tmdb.org/t/p/ и
     */

    /**
     * Название трассы
     */
    @NonNull
    public final String name;
    /**
     * Категория трассы
     */
    @NonNull
    public final String grade;
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

    public Route(
            @NonNull String name,
            @NonNull String grade,
            @NonNull String author,
            @NonNull String description) {
        this.name = name;
        this.grade = grade;
        this.author = author;
        this.description = description;
    }


    protected Route(Parcel in) {
        name = in.readString();
        grade = in.readString();
        description = in.readString();
        author = in.readString();
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
        dest.writeString(name);
        dest.writeString(grade);
        dest.writeString(description);
        dest.writeString(author);
    }

}
