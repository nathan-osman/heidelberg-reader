package com.nathanosman.heidelbergreader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Display a text entry for searching question and answer content
 */
public class SearchDialogFragment extends AppCompatDialogFragment {

    interface Listener {
        void onSearch(String query);
    }

    Listener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SearchDialogFragment");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        final View rootView = inflater.inflate(R.layout.dialog_search, null);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.search_dialog_title)
                .setView(rootView)
                .setPositiveButton(R.string.search_dialog_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        mListener.onSearch(((EditText) rootView.findViewById(R.id.query)).getText().toString());
                    }
                })
                .setNegativeButton(R.string.search_dialog_cancel, null)
                .create();
    }
}
