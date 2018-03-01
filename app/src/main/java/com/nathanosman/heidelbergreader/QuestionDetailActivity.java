package com.nathanosman.heidelbergreader;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * Display data for an individual question
 */
public class QuestionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        // Load the typeface for the action bar
        Util.applyTypeface(this, R.font.eb_garamond, R.id.toolbar_layout);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Create the fragment for displaying question data if it does not exist
        if (savedInstanceState == null &&
                getIntent().hasExtra(QuestionDetailFragment.ARG_QUESTION_INDEX)) {
            Bundle arguments = new Bundle();
            arguments.putInt(
                    QuestionDetailFragment.ARG_QUESTION_INDEX,
                    getIntent().getIntExtra(QuestionDetailFragment.ARG_QUESTION_INDEX, 0)
            );
            QuestionDetailFragment fragment = new QuestionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.question_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, QuestionListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
