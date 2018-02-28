package com.nathanosman.heidelbergreader;

import com.google.gson.annotations.SerializedName;

/**
 * Data for an individual question in the catechism
 *
 * Question data is stored in a JSON file in the assets folder. This class mirrors the structure of
 * the file, allowing for easy deserialization with Gson.
 */
class Question {

    @SerializedName("number")
    private int mNumber;

    @SerializedName("question")
    private String mQuestion;

    @SerializedName("answer")
    private String mAnswer;

    @SerializedName("references")
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
