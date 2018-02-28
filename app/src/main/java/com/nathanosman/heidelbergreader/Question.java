package com.nathanosman.heidelbergreader;

/**
 * Data for an individual question in the catechism
 *
 * Question data is stored in a JSON file in the assets folder. This class mirrors the structure of
 * the file, allowing for easy deserialization with Gson.
 */
class Question {

    private int mNumber;
    private String mQuestion;
    private String mAnswer;
    private String[] mReferences;

    /**
     * Retrieve the question number
     */
    int getNumber() {
        return mNumber;
    }

    /**
     * Retrieve the question title
     */
    String getQuestion() {
        return mQuestion;
    }

    /**
     * Retrieve the question answer
     */
    String getAnswer() {
        return mAnswer;
    }

    /**
     * Retrieve the Scripture references for the question
     */
    String[] getReferences() {
        return mReferences;
    }
}
