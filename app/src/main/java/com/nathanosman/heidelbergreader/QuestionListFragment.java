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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Display a list of questions
 */
public class QuestionListFragment extends Fragment
        implements QuestionAdapter.Listener {

    public static final String ARG_MESSAGE = "com.nathanosman.heidelbergreader.ARG_MESSAGE";
    public static final String ARG_QUESTIONS = "com.nathanosman.heidelbergreader.ARG_QUESTIONS";

    public interface Listener {
        void onNavigate(Question question);
    }

    private Listener mListener;

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory constructor
     */
    public QuestionListFragment() {
    }

    /**
     * Get the argument bundle for the fragment, creating it if necessary
     */
    private Bundle getOrCreateArguments() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            arguments = new Bundle();
            setArguments(arguments);
        }
        return arguments;
    }

    /**
     * Show and hide UI elements as necessary based on fragment arguments
     */
    private void refreshUi() {

        Bundle arguments = getOrCreateArguments();

        // If a message is present, show it
        String message = arguments.getString(ARG_MESSAGE);
        if (message != null) {
            mTextView.setText(message);
            mTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            return;
        }

        // If questions are present, create an adapter for the RecyclerView
        Parcelable[] parcelable = getArguments().getParcelableArray(ARG_QUESTIONS);
        if (parcelable != null) {

            // Create the adapter for the questions
            mRecyclerView.setAdapter(
                    new QuestionAdapter(
                            getActivity(),
                            this,
                            Arrays.copyOf(parcelable, parcelable.length, Question[].class)
                    )
            );

            // Add dividers
            DividerItemDecoration decoration = new DividerItemDecoration(
                    getActivity(),
                    DividerItemDecoration.VERTICAL
            );
            mRecyclerView.addItemDecoration(decoration);

            // Show the list of questions
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
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

        // Inflate the layout for the fragment
        View rootView = inflater.inflate(R.layout.question_list, container, false);

        // Retrieve references to the various views
        mProgressBar = rootView.findViewById(R.id.progress);
        mTextView = rootView.findViewById(R.id.message);
        mRecyclerView = rootView.findViewById(R.id.question_list);

        // Refresh the UI in case question data is already available
        refreshUi();

        return rootView;
    }

    @Override
    public void onNavigate(Question question) {
        mListener.onNavigate(question);
    }

    /**
     * Set the message to be displayed in place of the question list
     */
    public void setMessage(String message) {
        getOrCreateArguments().putString(ARG_MESSAGE, message);
        refreshUi();
    }

    /**
     * Set the list of questions to be displayed
     */
    public void setQuestions(Question[] questions) {
        getOrCreateArguments().putParcelableArray(ARG_QUESTIONS, questions);
        refreshUi();
    }
}
