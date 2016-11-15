package ru.climbing.itmo.itmoclimbing;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.climbing.itmo.itmoclimbing.R;


/**
 * Created by macbook on 15.11.16.
 */


public class Routs extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.routs, container, false);
    }
}