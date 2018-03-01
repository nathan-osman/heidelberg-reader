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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Display a list of questions
 *
 * This activity shows a list of questions and launches another activity to display a question on
 * smaller screens. For larger devices, the detail fragment is shown in this activity. This
 * activity binds to the question service as long as it is active.
 */
public class QuestionListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    private QuestionAdapter mAdapter;
    private QuestionService.LocalBinder mBinder;

    /**
     * Conditionally assign the binder to the adapter
     */
    private void assignBinder() {
        if (mBinder != null && mBinder.isLoaded()) {
            String errorMessage = mBinder.getErrorMessage();
            if (errorMessage == null) {
                mAdapter.setBinder(mBinder);
            } else {
                // TODO show error message
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

        // Determine if two-pane mode is active
        if (findViewById(R.id.question_detail_container) != null) {
            mTwoPane = true;
        }

        // Create the adapter
        mAdapter = new QuestionAdapter(this, new QuestionAdapter.Listener() {

            @Override
            public void onNavigate(int index) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(QuestionDetailFragment.ARG_QUESTION_INDEX, index);
                    QuestionDetailFragment fragment = new QuestionDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.question_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(QuestionListActivity.this, QuestionDetailActivity.class);
                    intent.putExtra(QuestionDetailFragment.ARG_QUESTION_INDEX, index);
                    startActivity(intent);
                }
            }
        });

        // Assign the adapter to the recycler view
        RecyclerView recyclerView = findViewById(R.id.question_list);
        recyclerView.setAdapter(mAdapter);

        // Add dividers
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}