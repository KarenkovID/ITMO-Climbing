package ru.climbing.itmo.itmoclimbing;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.stetho.Stetho;

import ru.climbing.itmo.itmoclimbing.fragments.SelectCompetitionFragment;

/**
 * Менеджер соревнований, при помощи которого можно вести учёт во внутресекционных прикидках
 */
public class CompetitionManagerActivity extends AppCompatActivity {

    public static final String TAG = CompetitionManagerActivity.class.getSimpleName();

    public static final String BACK_STACK_TAG = TAG + "backStack";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Stetho.initializeWithDefaults(this);
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_manager);

        Class fragmentClass =  SelectCompetitionFragment.class;
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Если фрагмент не добавлен.
        //Нужно проверять, т.к. мы предыдущее состояние может быть восстановлено
        if (fragmentManager.findFragmentByTag(SelectCompetitionFragment.TAG) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new SelectCompetitionFragment(), SelectCompetitionFragment.TAG)
                    .commit();
        }
    }
}
