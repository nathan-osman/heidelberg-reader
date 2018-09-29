package com.nathanosman.heidelbergreader.database;

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
 * Catechism maintains a list of Lord's Days in the catechism
 *
 * This class provides facilities for asynchronously loading the questions from the JSON data file.
 */
public class Catechism {

    /**
     * Interface for being notified that loading completed
     */
    interface Listener {
        void onLoadFailed(String message);
        void onLoadSucceeded();
    }

    private static class Loader extends AsyncTask<Void, Void, Loader.Result> {

        interface Listener {
            void onCompleted(Result result);
        }

        class Result {
            String errorMessage;
            Day[] days;
        }

        private WeakReference<Context> mContext;
        private Listener mListener;

        Loader(Context context, Listener listener) {
            mContext = new WeakReference<>(context);
            mListener = listener;
        }

        @Override
        protected Result doInBackground(Void... voids) {
            Result result = new Result();
            Gson gson = new Gson();
            try {
                JsonReader reader = new JsonReader(
                        new InputStreamReader(
                                mContext.get().getAssets().open(
                                        "questions.json"
                                )
                        )
                );
                Type dayType = new TypeToken<Day[]>() {}.getType();
                result.days = gson.fromJson(reader, dayType);
            } catch (IOException | JsonParseException e) {
                result.errorMessage = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            mListener.onCompleted(result);
        }
    }

    private Day[] mDays;

    /**
     * Asynchronously load the questions for the catechism
     */
    public void load(Context context, final Listener listener) {
        new Loader(context, new Loader.Listener() {

            @Override
            public void onCompleted(Loader.Result result) {
                if (result.errorMessage != null) {
                    listener.onLoadFailed(result.errorMessage);
                } else {
                    mDays = result.days;
                    listener.onLoadSucceeded();
                }
            }
        }).execute();
    }

    /**
     * Retrieve the list of days (with questions)
     * @return a list of days or null if loading failed
     */
    public Day[] getDays() {
        return mDays;
    }
}
