package com.nathanosman.heidelbergreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Arrays;

/**
 * Display a list of questions
 *
 * This activity shows a list of questions and launches another activity to display a question on
 * smaller screens. For larger devices, the detail fragment is shown in this activity.
 */
public class QuestionListActivity extends AppCompatActivity implements
        QuestionListFragment.Listener,
        QuestionLoaderTask.Listener,
        SearchDialogFragment.Listener,
        SearchTask.Listener {

    private static final String ARG_QUESTIONS = "com.nathanosman.heidelbergreader.ARG_QUESTIONS";

    private boolean mTwoPane;

    private Question[] mQuestions;
    private QuestionListFragment mFragment;

    private Menu mActions;

    private SearchTask mSearchTask;

    /**
     * Create a new fragment
     * @param progressText text shown while the spinner is displayed
     * @param progressCancel true to allow the operation to be cancelled
     * @param headerTitle title shown in the header (or null for none)
     * @param headerSubtitle subtitle shown in the header (or null for none)
     * @param questions array of questions to initialize the fragment with
     */
    private void createFragment(String progressText,
                                boolean progressCancel,
                                String headerTitle,
                                String headerSubtitle,
                                Question[] questions) {

        // Build the arguments for the fragment
        Bundle arguments = new Bundle();
        arguments.putBoolean(QuestionListFragment.ARG_TWO_PANE, mTwoPane);
        arguments.putString(QuestionListFragment.ARG_PROGRESS_TEXT, progressText);
        arguments.putBoolean(QuestionListFragment.ARG_PROGRESS_CANCEL, progressCancel);
        arguments.putString(QuestionListFragment.ARG_HEADER_TITLE, headerTitle);
        arguments.putString(QuestionListFragment.ARG_HEADER_SUBTITLE, headerSubtitle);
        arguments.putParcelableArray(QuestionListFragment.ARG_QUESTIONS, questions);

        // Create the fragment
        mFragment = new QuestionListFragment();
        mFragment.setArguments(arguments);

        // Insert the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.question_list_container, mFragment)
                .commit();
    }

    /**
     * Create a fragment with the existing set of questions
     */
    private void createFragmentWithExistingQuestions() {
        createFragment(null, false, null, null, mQuestions);
    }

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

        // If the questions were already loaded, use the stored value
        if (savedInstanceState != null) {
            Parcelable[] parcelable = savedInstanceState.getParcelableArray(ARG_QUESTIONS);
            if (parcelable != null) {
                mQuestions = Arrays.copyOf(parcelable, parcelable.length, Question[].class);
                return;
            }
        }

        // Otherwise, create a new fragment and load the questions
        createFragmentWithExistingQuestions();
        new QuestionLoaderTask(this, this).execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(ARG_QUESTIONS, mQuestions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_list, menu);

        // Enable the search menu item if questions are loaded
        if (mQuestions != null) {
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            mActions = menu;
        }

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
    public void onLoadError(String message) {
        mFragment.setMessage(getString(R.string.list_error_message, message));
    }

    @Override
    public void onLoadSucceeded(Question[] questions) {
        mQuestions = questions;
        mFragment.setQuestions(questions);

        if (mActions != null) {
            mActions.findItem(R.id.action_search).setVisible(true);
        }
    }

    @Override
    public void onClose() {
        createFragmentWithExistingQuestions();
    }

    @Override
    public void onCancel() {
        mSearchTask.cancel(false);
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

        // Create a new fragment for the questions
        createFragment(
                getString(R.string.list_search_progress),
                true,
                getString(R.string.list_search_header_title),
                getString(R.string.list_search_header_subtitle, query),
                null
        );

        // Create and start the search task
        mSearchTask = new SearchTask(
                this,
                new SearchTask.Parameters(mQuestions, query)
        );
        mSearchTask.execute();
    }

    @Override
    public void onSearchCancelled() {
        createFragmentWithExistingQuestions();
    }

    @Override
    public void onSearchResults(Question[] questions) {
        mFragment.setQuestions(questions);
    }
}
