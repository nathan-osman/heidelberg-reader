package com.nathanosman.heidelbergreader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * RecyclerView adapter that displays a list of questions
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    /**
     * Data for an individual question
     */
    private class Question {

        private int mNumber;
        private String mQuestion;
        private String mAnswer;
        private String[] mReferences;

        int getNumber() {
            return mNumber;
        }

        String getQuestion() {
            return mQuestion;
        }

        String getAnswer() {
            return mAnswer;
        }

        String[] getReferences() {
            return mReferences;
        }
    }

    private Context mContext;
    private Question[] mQuestions;

    /**
     * Task for loading the questions from the JSON file
     */
    private class LoadTask extends AsyncTask<Void, Void, Question[]> {

        @Override
        protected Question[] doInBackground(Void... voids) {
            Gson gson = new Gson();
            try {
                JsonReader reader = new JsonReader(
                        new InputStreamReader(
                                mContext.getAssets().open("questions.json")
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
            mQuestions = questions;
            notifyDataSetChanged();
        }
    }

    QuestionAdapter(Context context) {
        mContext = context;

        // Immediately load the questions
        new LoadTask().execute();
    }

    /**
     * Retrieve a question from the list
     * @param index question index
     * @return question data
     */
    Question getQuestion(int index) {
        return mQuestions[index];
    }

    /**
     * View holder for an individual question
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mSummary;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.question_list_title);
            mSummary = itemView.findViewById(R.id.question_list_summary);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.question_list_content,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Retrieve the question for the specified index
        Question question = mQuestions[position];

        // Populate the view holder with the question data
        holder.mTitle.setText(question.getQuestion());
        holder.mSummary.setText(question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return mQuestions.length;
    }
}
