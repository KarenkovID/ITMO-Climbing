package ru.climbing.itmo.itmoclimbing.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.climbing.itmo.itmoclimbing.LoginActivity;
import ru.climbing.itmo.itmoclimbing.R;

@SuppressLint("ValidFragment")
public class LoginFragment extends Fragment implements View.OnClickListener{

    LoginActivity.ScreenSlidePagerAdapter adapter;

    public LoginFragment(LoginActivity.ScreenSlidePagerAdapter adapter) {
        this.adapter = adapter;

    }

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

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                adapter.prepareFragment(adapter.SIGN_IN);
                break;
            case R.id.button_register:
                adapter.prepareFragment(adapter.SIGN_UP);
                break;
        }
    }
}
