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

    private String mError;
    private Question[] mQuestions;

    /**
     * Interface for interacting with the service
     */
    class LocalBinder extends Binder {

        /**
         * Retrieve the error if one occurred
         * @return error description or null if no error
         */
        String getError() {
            return mError;
        }

        /**
         * Retrieve the number of questions
         */
        int getQuestionCount() {
            return mQuestions != null ? mQuestions.length : 0;
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

            String mError;
            Question[] mQuestions;

            Result(String error) {
                mError = error;
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
            mService.get().mError = result.mError;
            mService.get().mQuestions = result.mQuestions;

            // Send a broadcast indicating that loading was complete
            mService.get().sendBroadcast(new Intent(LOADED));
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
