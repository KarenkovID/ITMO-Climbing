package ru.climbing.itmo.itmoclimbing.fragments;

/**
 * Created by macbook on 15.11.16.
 */
import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import ru.climbing.itmo.itmoclimbing.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_person_info, container, false);
    }

}
