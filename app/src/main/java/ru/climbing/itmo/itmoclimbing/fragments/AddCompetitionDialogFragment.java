package ru.climbing.itmo.itmoclimbing.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.climbing.itmo.itmoclimbing.R;

/**
 * Created by Игорь on 16.12.2016.
 */

public class AddCompetitionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.new_competition)
                .setView(R.layout.dialog_fragment_add_competition)
                .setPositiveButton(R.string.create, this)
                .setNegativeButton(R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
