package com.nathanosman.heidelbergreader;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * RecyclerView adapter that displays a list of questions
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private QuestionService.LocalBinder mBinder;

    /**
     * Create a new adapter
     * @param context use this context
     */
    QuestionAdapter(Context context) {

        // Listen for broadcasts from the service
        context.registerReceiver(
                new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        notifyDataSetChanged();
                    }
                },
                new IntentFilter(QuestionService.LOAD_SUCCESS)
        );

        // Bind to the service
        context.bindService(
                new Intent(context, QuestionService.class),
                new ServiceConnection() {

                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        mBinder = (QuestionService.LocalBinder) service;
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        mBinder = null;
                    }

                }, Context.BIND_AUTO_CREATE
        );
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
        holder.mTitle.setText(question.getQuestion());
        holder.mSummary.setText(question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return mBinder.getQuestionCount();
    }
}
