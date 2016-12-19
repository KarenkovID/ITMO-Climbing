package ru.climbing.itmo.itmoclimbing.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Игорь on 20.12.2016.
 */

public class Athlete implements Parcelable{
    /**
     * id спортсмена
     */
    public final int id;
    /**
     * имя спортсмена
     */
    public final String lastName;
    /**
     * фамилия спортсмена
     */
    public final String firstName;
    /**
     * балы спортсмена за пролазы трасс
     */
    public final double score;
    /**
     * позиция спортсмена в рейтинге
     */
    public final int position;

    public Athlete(int id, String lastName, String firstName, double score, int position) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.score = score;
        this.position = position;
    }

    protected Athlete(Parcel in) {
        id = in.readInt();
        lastName = in.readString();
        firstName = in.readString();
        score = in.readDouble();
        position = in.readInt();
    }

    public static final Creator<Athlete> CREATOR = new Creator<Athlete>() {
        @Override
        public Athlete createFromParcel(Parcel in) {
            return new Athlete(in);
        }

        @Override
        public Athlete[] newArray(int size) {
            return new Athlete[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeDouble(score);
        dest.writeInt(position);
    }
}
