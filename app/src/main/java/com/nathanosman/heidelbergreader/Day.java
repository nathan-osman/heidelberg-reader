package com.nathanosman.heidelbergreader;

import com.google.gson.annotations.SerializedName;

/**
 * Data for a group of questions for a specific day.
 */
class Day {

    @SerializedName("number")
    private int mNumber;

    @SerializedName("questions")
    private Question[] mQuestions;

    /**
     * Retrieve the day number
     */
    int getNumber() {
        return mNumber;
    }

    /**
     * Retrieve the questions for the day
     */
    Question[] getQuestions() {
        return mQuestions;
    }
}
