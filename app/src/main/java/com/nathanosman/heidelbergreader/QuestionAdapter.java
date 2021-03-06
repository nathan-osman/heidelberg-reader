package com.nathanosman.heidelbergreader;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * RecyclerView adapter that displays a list of questions
 */
class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    /**
     * Interface for navigation events generated by the adapter
     */
    interface Listener {
        void onNavigate(Question question);
    }

    private Context mContext;
    private Listener mListener;
    private Question[] mQuestions;

    private boolean mTwoPane;
    private int mSelectedIndex = -1;

    /**
     * Create a new question adapter
     * @param context use this context for loading the questions
     * @param listener notification for navigation events
     * @param questions list of questions for the adapter
     * @param twoPane enable behavior for two-pane mode
     */
    QuestionAdapter(Context context, Listener listener, Question[] questions, boolean twoPane) {
        mContext = context;
        mListener = listener;
        mQuestions = questions;
        mTwoPane = twoPane;

        if (mTwoPane && questions.length > 0) {
            mSelectedIndex = 0;
            mListener.onNavigate(questions[0]);
        }
    }

    /**
     * View holder for an individual question
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNumber;
        private TextView mTitle;
        private TextView mSummary;

        ViewHolder(View itemView) {
            super(itemView);

            mNumber = itemView.findViewById(R.id.question_list_number);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Retrieve the question for the specified index
        final Question question = mQuestions[position];

        // Populate the view holder with the question data
        holder.mNumber.setText(mContext.getString(R.string.list_question_number, question.getNumber()));
        holder.mTitle.setText(question.getQuestion());
        holder.mSummary.setText(question.getAnswer());

        // Set the click listener for the question
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mTwoPane) {

                    // Grab the index of the previous selection
                    int oldSelectedIndex = mSelectedIndex;

                    // Set the new selection and update the item
                    mSelectedIndex = holder.getAdapterPosition();
                    notifyItemChanged(mSelectedIndex);

                    // If there was a previous selection, update it too
                    if (oldSelectedIndex != -1) {
                        notifyItemChanged(oldSelectedIndex);
                    }
                }

                // Notify the listener that a question was selected
                mListener.onNavigate(question);
            }
        });

        // Set the background color based on whether this question is selected
        holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                        mContext,
                        position == mSelectedIndex ?
                                R.color.colorSelected :
                                android.R.color.transparent
                )
        );
    }

    @Override
    public int getItemCount() {
        return mQuestions.length;
    }
}
