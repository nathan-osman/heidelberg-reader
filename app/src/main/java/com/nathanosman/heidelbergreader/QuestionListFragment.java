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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Display a list of questions
 *
 * The fragment accepts four optional arguments: one providing the message displayed until the
 * question list is supplied, another indicating if the operation can be cancelled, and one each
 * for the header's title and subtitle.
 *
 * The remaining two arguments are supplied by using the setMessage() and setQuestions() methods.
 */
public class QuestionListFragment extends Fragment
        implements QuestionAdapter.Listener {

    public static final String ARG_TWO_PANE = "com.nathanosman.heidelbergreader.ARG_TWO_PANE";

    public static final String ARG_PROGRESS_TEXT = "com.nathanosman.heidelbergreader.ARG_PROGRESS_TEXT";
    public static final String ARG_PROGRESS_CANCEL = "com.nathanosman.heidelbergreader.ARG_PROGRESS_CANCEL";

    public static final String ARG_HEADER_TITLE = "com.nathanosman.heidelbergreader.ARG_HEADER_TITLE";
    public static final String ARG_HEADER_SUBTITLE = "com.nathanosman.heidelbergreader.ARG_HEADER_SUBTITLE";

    public static final String ARG_MESSAGE = "com.nathanosman.heidelbergreader.ARG_MESSAGE";
    public static final String ARG_QUESTIONS = "com.nathanosman.heidelbergreader.ARG_QUESTIONS";

    public interface Listener {
        void onClose();
        void onCancel();
        void onNavigate(Question question);
    }

    private Listener mListener;

    private LinearLayout mProgressLayout;
    private LinearLayout mHeaderLayout;
    private TextView mMessage;
    private RecyclerView mQuestions;

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
            mMessage.setText(message);
            mMessage.setVisibility(View.VISIBLE);
            mProgressLayout.setVisibility(View.GONE);
            return;
        }

        // If questions are present, create an adapter for the RecyclerView
        Parcelable[] parcelable = arguments.getParcelableArray(ARG_QUESTIONS);
        if (parcelable != null) {

            // Create the adapter for the questions
            mQuestions.setAdapter(
                    new QuestionAdapter(
                            getActivity(),
                            this,
                            Arrays.copyOf(parcelable, parcelable.length, Question[].class),
                            arguments.getBoolean(ARG_TWO_PANE)
                    )
            );

            // Add dividers
            DividerItemDecoration decoration = new DividerItemDecoration(
                    getActivity(),
                    DividerItemDecoration.VERTICAL
            );
            mQuestions.addItemDecoration(decoration);

            // Show the list of questions
            mQuestions.setVisibility(View.VISIBLE);
            mProgressLayout.setVisibility(View.GONE);

            // Show the header if provided
            if (mHeaderLayout != null) {
                mHeaderLayout.setVisibility(View.VISIBLE);
            }
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
        mProgressLayout = rootView.findViewById(R.id.progress_container);
        mMessage = rootView.findViewById(R.id.message);
        mQuestions = rootView.findViewById(R.id.question_list);

        Bundle arguments = getOrCreateArguments();

        // Override the progress text (if provided)
        String progressText = arguments.getString(ARG_PROGRESS_TEXT);
        if (progressText != null) {
            ((TextView) rootView.findViewById(R.id.progress_text)).setText(progressText);
        }

        // Enable the cancel button if the operation can be cancelled
        if (arguments.getBoolean(ARG_PROGRESS_CANCEL)) {
            Button button = rootView.findViewById(R.id.progress_cancel);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mListener.onCancel();
                }
            });
            button.setVisibility(View.VISIBLE);
        }

        // Initialize the header (if title and subtitle were provided)
        String headerTitle = arguments.getString(ARG_HEADER_TITLE);
        String headerSubtitle = arguments.getString(ARG_HEADER_SUBTITLE);
        if (headerTitle != null && headerSubtitle != null) {

            // Retrieve a reference to the header
            mHeaderLayout = rootView.findViewById(R.id.header_container);

            // Set the title and subtitle for the header
            ((TextView) rootView.findViewById(R.id.header_title)).setText(headerTitle);
            ((TextView) rootView.findViewById(R.id.header_subtitle)).setText(headerSubtitle);

            // Invoke the listener's onClose() method when the close button is tapped
            rootView.findViewById(R.id.header_close).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mListener.onClose();
                }
            });
        }

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
