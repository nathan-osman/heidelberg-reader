package com.nathanosman.heidelbergreader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Display the data for a question
 */
public class QuestionDetailFragment extends Fragment {

    public static final String ARG_QUESTION_INDEX = "com.nathanosman.heidelbergreader.ARG_QUESTION_INDEX";

    private ServiceConnection mConnection;

    /**
     * Mandatory constructor
     */
    public QuestionDetailFragment() {
    }

    /**
     * Load question from the binder
     */
    private void loadQuestion(QuestionService.LocalBinder binder) {

        // TODO Check for an error

        // Retrieve the question index
        int questionIndex = getArguments().getInt(ARG_QUESTION_INDEX);

        // Ensure the index is valid
        if (questionIndex < binder.getQuestionCount()) {
            Question question = binder.getQuestion(questionIndex);

            // Show the question title
            Activity activity = getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(question.getQuestion());
            }
        }

        // We're done, unbind from the service
        getActivity().unbindService(mConnection);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attempt to retrieve the question from the service
        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                // Retrieve the binder
                final QuestionService.LocalBinder binder = (QuestionService.LocalBinder) service;

                // If the questions are loaded, continue; otherwise, wait for a broadcast
                if (binder.isLoaded()) {
                    loadQuestion(binder);
                } else {
                    BroadcastReceiver receiver = new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {
                            loadQuestion(binder);
                            getActivity().unregisterReceiver(this);
                        }
                    };
                    getActivity().registerReceiver(receiver, new IntentFilter(QuestionService.LOADED));
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        // Bind to the service to retrieve the data
        Intent intent = new Intent(getActivity(), QuestionService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question_detail, container, false);
    }
}
