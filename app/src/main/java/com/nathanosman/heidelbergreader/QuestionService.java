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

    public static final String LOAD_ERROR = "com.nathanosman.heidelbergreader.LOAD_ERROR";
    public static final String LOAD_SUCCESS = "com.nathanosman.heidelbergreader.LOAD_SUCCESS";

    private Question[] mQuestions;

    /**
     * Interface for interacting with the service
     */
    class LocalBinder extends Binder {

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
    private static class LoadTask extends AsyncTask<Void, Void, Question[]> {

        private WeakReference<QuestionService> mService;

        /**
         * Create a new question loader
         */
        LoadTask(QuestionService service) {
            mService = new WeakReference<>(service);
        }

        // TODO: pass error message along during broadcast

        @Override
        protected Question[] doInBackground(Void... voids) {
            Gson gson = new Gson();
            try {
                JsonReader reader = new JsonReader(
                        new InputStreamReader(
                                mService.get().getAssets().open("questions.json")
                        )
                );
                Type questionsType = new TypeToken<Question[]>() {}.getType();
                return gson.fromJson(reader, questionsType);
            } catch (IOException|JsonParseException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Question[] questions) {
            mService.get().mQuestions = questions;

            // Create a broadcast indicating that data was loaded (or not)
            Intent intent = new Intent();
            intent.setAction(questions != null ? LOAD_SUCCESS : LOAD_ERROR);

            // Send the broadcast
            mService.get().sendBroadcast(intent);
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
