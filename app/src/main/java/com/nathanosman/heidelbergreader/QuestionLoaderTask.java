package com.nathanosman.heidelbergreader;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * Task for parsing the JSON containing the questions
 */
public class QuestionLoaderTask extends AsyncTask<Void, Void, QuestionLoaderTask.Result> {

    interface Listener {
        void onLoadError(String message);
        void onLoadSucceeded(Question[] questions);
        void onLoadFinished();
    }

    /**
     * Result from the load operation
     */
    static class Result {

        String mErrorMessage;
        Question[] mQuestions;

        Result(String errorMessage) {
            mErrorMessage = errorMessage;
        }

        Result(Question[] questions) {
            mQuestions = questions;
        }
    }

    private WeakReference<Context> mContext;
    private Listener mListener;

    /**
     * Create a new instance of the loader task
     */
    QuestionLoaderTask(Context context, Listener listener) {
        mContext = new WeakReference<>(context);
        mListener = listener;
    }

    @Override
    protected Result doInBackground(Void... voids) {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(
                    new InputStreamReader(
                            mContext.get().getAssets().open(
                                    "questions.json"
                            )
                    )
            );
            Type questionsType = new TypeToken<Question[]>() {}.getType();
            return new Result((Question[]) gson.fromJson(reader, questionsType));
        } catch (IOException | JsonParseException e) {
            return new Result(e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result.mErrorMessage != null) {
            mListener.onLoadError(result.mErrorMessage);
        } else {
            mListener.onLoadSucceeded(result.mQuestions);
        }
        mListener.onLoadFinished();
    }
}
