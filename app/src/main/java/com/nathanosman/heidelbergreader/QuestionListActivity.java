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

    // Bind the adapter to the service while the activity is running
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAdapter.setBinder((QuestionService.LocalBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAdapter.setBinder(null);
        }
    };

    // Listen for broadcasts from the service while the activity is running
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.notifyDataSetChanged();
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
        registerReceiver(mReceiver, new IntentFilter(QuestionService.LOAD_SUCCESS));

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