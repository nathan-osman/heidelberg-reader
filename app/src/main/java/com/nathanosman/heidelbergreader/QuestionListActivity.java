package com.nathanosman.heidelbergreader;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

/**
 * Display a list of questions
 *
 * This activity shows a list of questions and launches another activity to display a question on
 * smaller screens. For larger devices, the detail fragment is shown in this activity. This
 * activity binds to the question service as long as it is active.
 */
public class QuestionListActivity extends AppCompatActivity {

    private QuestionAdapter mAdapter = new QuestionAdapter();
    private QuestionService.LocalBinder mBinder;

    /**
     * Conditionally assign the binder to the adapter
     */
    private void assignBinder() {
        if (mBinder != null && mBinder.isLoaded()) {
            String errorMessage = mBinder.getErrorMessage();
            if (errorMessage != null) {
                mAdapter.setBinder(mBinder);
            } else {
                // TODO: show error message
            }
        } else {
            mAdapter.setBinder(null);
        }
    }

    // Bind the adapter to the service while the activity is running
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (QuestionService.LocalBinder) service;
            assignBinder();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
            assignBinder();
        }
    };

    // Listen for broadcasts from the service while the activity is running
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            assignBinder();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        // Find the toolbar and set its title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = findViewById(R.id.question_list);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for broadcasts
        IntentFilter filter = new IntentFilter(QuestionService.LOADED);
        registerReceiver(mReceiver, filter);

        // Bind to the service
        Intent intent = new Intent(this, QuestionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unbind from the service
        unbindService(mConnection);

        // Stop listening for broadcasts
        unregisterReceiver(mReceiver);
    }
}
