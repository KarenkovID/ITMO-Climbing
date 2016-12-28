package ru.climbing.itmo.itmoclimbing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.climbing.itmo.itmoclimbing.fragments.AthletesFragment;
import ru.climbing.itmo.itmoclimbing.fragments.FestivalFragment;
import ru.climbing.itmo.itmoclimbing.fragments.ProfileFragment;
import ru.climbing.itmo.itmoclimbing.fragments.RoutesFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String CURRENT_FRAGMENT_TAG = "currentFragment";
    public static final String TITLE_TAG = "title";

    private CircleImageView profileImage;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar toolbar;

    private String mCurrentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + v);
                onBackPressed();
            }
        });
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.getHeaderView(0);

        profileImage = (CircleImageView) headerView.findViewById(R.id.nav_view_profile_image);
        profileImage.setOnClickListener(this);

        //Сдвигаем содержимое header, если версия API >= 21, т.к. тулбар прозрачный
        ViewCompat.setOnApplyWindowInsetsListener(headerView, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                int newTopPadding = insets.getSystemWindowInsetTop() + (int) getResources().getDimension(R.dimen.nav_header_top_padding);
//                Log.d("Navigation View", "getSystemWindowInsetTop " + insets.getSystemWindowInsetTop()
//                        + "old PaddingTop " + (int) getResources().getDimension(R.dimen.nav_header_top_padding));
                v.setPadding(v.getPaddingLeft(), newTopPadding, v.getPaddingRight(),
                        v.getPaddingBottom());
                //TODO: why does I return this
                return insets;
            }
        });

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_routs));
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            Log.d(TAG, "onPostCreate: fragments count" + fragments.size());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG, "");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = fragmentManager.findFragmentByTag(mCurrentFragmentTag);
            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager.getBackStackEntryCount() > 0) {
                childFragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: " + item.toString());
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_routs) {
            fragmentClass = RoutesFragment.class;
        } else if (id == R.id.nav_members) {
            fragmentClass = AthletesFragment.class;
        } else if (id == R.id.nav_festival) {
            fragmentClass = FestivalFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        } else if (id == R.id.competitions_manager) {
            Intent intent = new Intent(this, CompetitionManagerActivity.class);
            startActivity(intent);
            return true;
        }
        if (fragmentClass == null) {
            return false;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        //clean back stack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mCurrentFragmentTag = fragment.getClass().getSimpleName();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, mCurrentFragmentTag)
                .commit();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());


        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (v.getId()) {
            case R.id.nav_view_profile_image:
                fragmentClass = ProfileFragment.class;
                break;
            default:
                return;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выводим выбранный пункт в заголовке
        setTitle(getString(R.string.profile));


        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        outState.putString(TITLE_TAG, getSupportActionBar().getTitle().toString());
    }
}