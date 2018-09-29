package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = Question.TABLE_NAME)
public class Question {

    public static final String TABLE_NAME = "questions";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_DAY_NUMBER = "day_number";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_NUMBER)
    private final int mNumber;

    @NonNull
    @ColumnInfo(name = COLUMN_QUESTION)
    private final String mQuestion;

    @NonNull
    @ColumnInfo(name = COLUMN_ANSWER)
    private final String mAnswer;

    @ColumnInfo(name = COLUMN_DAY_NUMBER)
    private final int mDayNumber;

    public Question(int number, @NonNull String question, @NonNull String answer, int dayNumber) {
        mNumber = number;
        mQuestion = question;
        mAnswer = answer;
        mDayNumber = dayNumber;
    }

    public int getNumber() {
        return mNumber;
    }

    @NonNull
    public String getQuestion() {
        return mQuestion;
    }

    @NonNull
    public String getAnswer() {
        return mAnswer;
    }

    public int getDayNumber() {
        return mDayNumber;
    }
}
