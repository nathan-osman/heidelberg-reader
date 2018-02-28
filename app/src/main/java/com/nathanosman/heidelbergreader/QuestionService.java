package com.nathanosman.heidelbergreader;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * Provide a central location for accessing question content
 */
public class QuestionService extends Service {

    public static final String LOADED = "com.nathanosman.heidelbergreader.LOADED";

    private boolean mLoaded;
    private String mErrorMessage;
    private Question[] mQuestions;

    /**
     * Interface for interacting with the service
     */
    class LocalBinder extends Binder {

        /**
         * Determine if the question data has been loaded
         */
        boolean isLoaded() {
            return mLoaded;
        }

        /**
         * Retrieve the error if one occurred
         * @return error message or null if no error
         */
        String getErrorMessage() {
            return mErrorMessage;
        }

        /**
         * Retrieve the number of questions
         */
        int getQuestionCount() {
            return mQuestions.length;
        }

        /**
         * Retrieve a question via its index
         */
        Question getQuestion(int index) {
            return mQuestions[index];
        }
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Task for loading the question data from disk
     */
    private static class LoadTask extends AsyncTask<Void, Void, LoadTask.Result> {

        private WeakReference<QuestionService> mService;

        /**
         * Result from loading questions
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

        /**
         * Create a new question loader
         */
        LoadTask(QuestionService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        protected Result doInBackground(Void... voids) {
            Gson gson = new Gson();
            try {
                JsonReader reader = new JsonReader(
                        new InputStreamReader(
                                mService.get().getAssets().open("questions.json")
                        )
                );
                Type questionsType = new TypeToken<Question[]>() {}.getType();
                return new Result((Question[]) gson.fromJson(reader, questionsType));
            } catch (IOException|JsonParseException e) {
                return new Result(e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            QuestionService service = mService.get();
            service.mLoaded = true;
            service.mErrorMessage = result.mErrorMessage;
            service.mQuestions = result.mQuestions;
            service.sendBroadcast(new Intent(LOADED));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Load the questions
        new LoadTask(this).execute();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
