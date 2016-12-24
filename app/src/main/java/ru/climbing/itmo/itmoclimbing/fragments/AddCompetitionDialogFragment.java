package ru.climbing.itmo.itmoclimbing.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.climbing.itmo.itmoclimbing.R;

/**
 * Created by Игорь on 16.12.2016.
 */

public class AddCompetitionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    public static final String COMPETITION_NAME_TAG = "competitionName";

    private EditText etCompetitionName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_add_competition, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.new_competition)
                .setView(rootView)
                .setPositiveButton(R.string.create, this)
                .setNegativeButton(R.string.cancel, this);
        etCompetitionName = (EditText) rootView.findViewById(R.id.etCompetitionName);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Intent intent = new Intent();
            intent.putExtra(COMPETITION_NAME_TAG, etCompetitionName.getText().toString());
            getParentFragment().onActivityResult(SelectCompetitionFragment.
                    REQUEST_COMPETITION_NAME_TAG, AppCompatActivity.RESULT_OK, intent);
        }
    }
}
