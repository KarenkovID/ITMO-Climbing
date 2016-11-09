package ru.climbing.itmo.itmoclimbing.graphicPart;

/**
 * Created by Игорь on 23.10.2016.
 */

public interface Camera {
    void updateCam();
    void getRay(float[] dest, int offset, int x, int y);
}
