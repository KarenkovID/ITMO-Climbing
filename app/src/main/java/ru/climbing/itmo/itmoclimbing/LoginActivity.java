package ru.climbing.itmo.itmoclimbing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private ViewPager viewPager;

    public ScreenSlidePagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.pagerContainer, new LoginFragment())
//                    .commit();
//        }

        viewPager = (ViewPager) findViewById(R.id.pagerContainer);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageTransformer(false, new ParallaxPageTransformer());
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public static final int SIGN_IN = 1, SIGN_UP = 2;
        private SignInFragment signInFragment = new SignInFragment();
        private SignUpFragment signUpFragment = new SignUpFragment();
        private LoginFragment loginFragment = new LoginFragment(this);
        private Fragment curSecondFragment;
        private int curSecondPageType = 0;

        private int numPages = 1;

        public ViewPager pager;


        public ScreenSlidePagerAdapter(FragmentManager fm, ViewPager pager) {
            super(fm);
            this.pager = pager;
        }
        //TODO: do it more efficient
        @Override
        public int getItemPosition(Object object) {
            if (object == loginFragment) {
                return POSITION_UNCHANGED;
            }
            return POSITION_NONE;
        }

        public void prepareFragment(int type) {
            if (numPages == 1) {
                numPages++;
            }
            //If requested page == current page
            if (type != curSecondPageType) {
                curSecondPageType = type;
                if (curSecondPageType == SIGN_IN) {
                    curSecondFragment = signInFragment;
                } else {
                    curSecondFragment = signUpFragment;
                }
                notifyDataSetChanged();
            }
            pager.setCurrentItem(1, true);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return curSecondFragment;
            }
            return loginFragment;
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }
    public class ParallaxPageTransformer implements ViewPager.PageTransformer {
        

        public void transformPage(View view, float position) {

            int pageWidth = view.getWidth();


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(1);

            } else if (position <= 1) { // [-1,1]

                ImageView background= (ImageView)view.findViewById(R.id.backgroundImage);
                background.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(1);
            }


        }
    }
}
