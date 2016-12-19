package ru.climbing.itmo.itmoclimbing.model;

/**
 * Created by Игорь on 20.12.2016.
 */

public class Athlete {
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
}
