package ru.climbing.itmo.itmoclimbing.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.climbing.itmo.itmoclimbing.LoginActivity;
import ru.climbing.itmo.itmoclimbing.MainActivity;
import ru.climbing.itmo.itmoclimbing.R;

public class LoginFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = LoginFragment.class.getSimpleName();
    public LoginFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonLogin = (Button) view.findViewById(R.id.button_login);
        Button buttonRegister = (Button) view.findViewById(R.id.button_register);
        Button buttonTestLaunch = (Button) view.findViewById(R.id.button_test_launch);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonTestLaunch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.button_login:
                ((LoginActivity)getActivity()).pagerAdapter.prepareFragment(LoginActivity.ScreenSlidePagerAdapter.SIGN_IN);
                break;
            case R.id.button_register:
                ((LoginActivity)getActivity()).pagerAdapter.prepareFragment(LoginActivity.ScreenSlidePagerAdapter.SIGN_UP);
                break;
            case R.id.button_test_launch:
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}
