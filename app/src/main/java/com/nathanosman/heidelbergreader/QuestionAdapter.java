package com.nathanosman.heidelbergreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * RecyclerView adapter that displays a list of questions
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context mContext;
    private QuestionService.LocalBinder mBinder;

    QuestionAdapter(Context context) {
        mContext = context;
    }

    /**
     * Set the binder for retrieving question data
     */
    void setBinder(QuestionService.LocalBinder binder) {
        mBinder = binder;
        notifyDataSetChanged();
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
        Question question = mBinder.getQuestion(position);

        // Populate the view holder with the question data
        holder.mTitle.setText(
                mContext.getString(
                        R.string.adapter_question_title,
                        question.getNumber(),
                        question.getQuestion()
                )
        );
        holder.mSummary.setText(question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return mBinder != null ? mBinder.getQuestionCount() : 0;
    }
}
