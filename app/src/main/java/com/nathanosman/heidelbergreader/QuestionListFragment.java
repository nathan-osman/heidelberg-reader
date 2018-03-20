package com.nathanosman.heidelbergreader;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

/**
 * Display a list of questions
 */
public class QuestionListFragment extends Fragment
        implements QuestionAdapter.Listener {

    public static final String ARG_QUESTIONS = "com.nathanosman.heidelbergreader.ARG_QUESTIONS";

    interface Listener {
        void onNavigate(Question question);
    }

    private Listener mListener;

    /**
     * Mandatory constructor
     */
    public QuestionListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement QuestionListFragment.Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If a list of questions was not provided, return null
        if (!getArguments().containsKey(ARG_QUESTIONS)) {
            return null;
        }

        // Retrieve the list of questions so that they can be passed to the adapter
        Parcelable[] parcelable = getArguments().getParcelableArray(ARG_QUESTIONS);
        assert parcelable != null;
        Question[] questions = Arrays.copyOf(parcelable, parcelable.length, Question[].class);

        // Create a new adapter
        QuestionAdapter adapter = new QuestionAdapter(
                getActivity(),
                this,
                questions
        );

        // Inflate the layout
        View rootView = inflater.inflate(R.layout.question_list, container, false);

        // Find the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.question_list);
        recyclerView.setAdapter(adapter);

        // Add dividers
        DividerItemDecoration decoration = new DividerItemDecoration(
                getActivity(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(decoration);

        return rootView;
    }

    @Override
    public void onNavigate(Question question) {
        mListener.onNavigate(question);
    }
}
