package ru.climbing.itmo.itmoclimbing.callbacks;

import android.support.annotation.Nullable;

/**
 * Created by Игорь on 27.12.2016.
 */

public interface ActionBarDrawerCallback {
    void setTitleAndShowBackButton (@Nullable String title);
    void showDrawerButton ();
}
