package com.nathanosman.heidelbergreader;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Display the data for a question
 */
public class QuestionDetailFragment extends Fragment {

    public static final String ARG_QUESTION = "com.nathanosman.heidelbergreader.ARG_QUESTION";

    /**
     * Mandatory constructor
     */
    public QuestionDetailFragment() {
    }

    /**
     * Attempt to open YouVersion to the specified reference
     * @param reference reference to open in the app
     * @return true if YouVersion was launched without error
     */
    private boolean launchYouVersion(String reference) {

        // Attempt to convert the reference to OSIS format
        String osisRef = OsisConverter.convert(reference);
        if (osisRef == null) {
            return false;
        }

        // Build the URI for opening YouVersion
        Uri osisUri = new Uri.Builder()
                .scheme("youversion")
                .authority("bible")
                .appendQueryParameter("reference", osisRef)
                .build();

        // Attempt to launch the intent
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, osisUri));
        } catch (ActivityNotFoundException e) {
            return false;
        }

        // Success!
        return true;
    }

    /**
     * Open Bible Gateway to a passage
     * @param reference reference to display
     */
    private void launchBibleGateway(String reference) {

        // Build the URL for showing the passage
        Uri refUri = new Uri.Builder()
                .scheme("http")
                .authority("www.biblegateway.com")
                .appendPath("passage")
                .appendQueryParameter("search", reference)
                .build();

        // Launch an activity to show the reference
        startActivity(new Intent(Intent.ACTION_VIEW, refUri));
    }

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String reference = (String) view.getTag();
            if (!launchYouVersion(reference)) {
                launchBibleGateway(reference);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.question_detail, container, false);

        // Quit initialization if the question is missing
        if (!getArguments().containsKey(ARG_QUESTION)) {
            return null;
        }

        // Retrieve the question's data
        Question question = getArguments().getParcelable(ARG_QUESTION);
        assert question != null;

        // Show the question title
        CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getActivity().getString(R.string.adapter_question_number, question.getNumber()));
        }

        // Show the question content
        ((TextView) rootView.findViewById(R.id.question_detail_question)).setText(question.getQuestion());
        ((TextView) rootView.findViewById(R.id.question_detail_answer)).setText(question.getAnswer());

        // Find the layout that will contain the references
        LinearLayout layout = rootView.findViewById(R.id.question_detail_layout);
        int padding = getActivity().getResources().getDimensionPixelSize(R.dimen.text_margin);

        // Create a text view for each of the references
        for (String reference : question.getReferences()) {
            TextView refView = new TextView(getActivity());
            refView.setTag(reference);
            refView.setOnClickListener(mOnClickListener);
            refView.setPadding(padding, 0, padding, padding);
            refView.setPaintFlags(refView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            refView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            refView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    getActivity().getResources().getDimension(R.dimen.reference_size)
            );
            refView.setText(reference);
            layout.addView(refView);
        }

        return rootView;
    }
}
