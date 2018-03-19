package com.nathanosman.heidelbergreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Display a list of questions
 *
 * This activity shows a list of questions and launches another activity to display a question on
 * smaller screens. For larger devices, the detail fragment is shown in this activity. This
 * activity binds to the question service as long as it is active.
 */
public class QuestionListActivity extends AppCompatActivity implements
        QuestionAdapter.Listener,
        QuestionLoaderTask.Listener,
        SearchDialogFragment.Listener {

    private boolean mTwoPane;

    private Question[] mQuestions;
    private Menu mActions;

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

        // Create a task to load the questions
        new QuestionLoaderTask(this, this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_list, menu);
        mActions = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchDialogFragment dialog = new SearchDialogFragment();
                dialog.show(getSupportFragmentManager(), "search");
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onError(String message) {
        TextView errorView = findViewById(R.id.error_message);
        errorView.setText(getString(R.string.list_error_message, message));
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaded(Question[] questions) {

        // Hold a reference to the questions and enable the search option
        mQuestions = questions;
        mActions.findItem(R.id.action_search).setVisible(true);

        // Create the adapter
        QuestionAdapter adapter = new QuestionAdapter(
                QuestionListActivity.this,
                QuestionListActivity.this,
                questions
        );

        // Assign the adapter to the recycler view
        RecyclerView recyclerView = findViewById(R.id.question_list);
        recyclerView.setAdapter(adapter);

        // Add dividers
        DividerItemDecoration decoration = new DividerItemDecoration(
                QuestionListActivity.this,
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void onFinished() {
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    @Override
    public void onNavigate(Question question) {
        if (mTwoPane) {

            // Create a bundle with the question passed as an argument
            Bundle arguments = new Bundle();
            arguments.putParcelable(QuestionDetailFragment.ARG_QUESTION, question);

            // Create the fragment for the question and inject it into the activity
            QuestionDetailFragment fragment = new QuestionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.question_detail_container, fragment)
                    .commit();

        } else {
            Intent intent = new Intent(QuestionListActivity.this, QuestionDetailActivity.class);
            intent.putExtra(QuestionDetailFragment.ARG_QUESTION, question);
            startActivity(intent);
        }
    }

    @Override
    public void onSearch(String query) {
        //...
    }
}