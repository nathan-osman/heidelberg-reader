package com.nathanosman.heidelbergreader;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Task for searching in questions for content
 */
public class SearchTask extends AsyncTask<Void, Void, SearchTask.Result> {

    interface Listener {
        void onSearchCancelled();
        void onSearchResults(Question[] questions);
    }

    /**
     * Parameters for the search operation
     */
    static class Parameters {
        Question[] mQuestions;
        String mQuery;

        Parameters(Question[] questions, String query) {
            mQuestions = questions;
            mQuery = query;
        }
    }

    /**
     * Result from the search operation
     */
    static class Result {

        boolean mWasCancelled;
        Question[] mQuestions;

        Result() {
            mWasCancelled = true;
        }

        Result(Question[] questions) {
            mQuestions = questions;
        }
    }

    private Listener mListener;
    private Parameters mParameters;

    SearchTask(Listener listener, Parameters parameters) {
        mListener = listener;
        mParameters = parameters;
    }

    @Override
    protected Result doInBackground(Void... voids) {
        String query = mParameters.mQuery;
        List<Question> matchedQuestions = new ArrayList<>();
        for (Question question : mParameters.mQuestions) {
            if (isCancelled()) {
                return new Result();
            }
            if (question.getQuestion().contains(query) ||
                    question.getAnswer().contains(query)) {
                matchedQuestions.add(question);
            }
        }
        return new Result(
                matchedQuestions.toArray(new Question[matchedQuestions.size()])
        );
    }

    @Override
    protected void onCancelled() {
        mListener.onSearchCancelled();
    }

    @Override
    protected void onPostExecute(Result result) {
        mListener.onSearchResults(result.mQuestions);
    }
}
