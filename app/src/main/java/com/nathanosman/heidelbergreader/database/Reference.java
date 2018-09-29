package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = Reference.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Question.class,
                parentColumns = Question.COLUMN_NUMBER,
                childColumns = Reference.COLUMN_QUESTION_NUMBER,
                onDelete = ForeignKey.CASCADE
        ))
public class Reference {

    public static final String TABLE_NAME = "references";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_QUESTION_NUMBER = "question_number";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    private int mId;

    @NonNull
    @ColumnInfo(name = COLUMN_REFERENCE)
    private final String mReference;

    @NonNull
    @ColumnInfo(name = COLUMN_TEXT)
    private final String mText;

    @ColumnInfo(name = COLUMN_QUESTION_NUMBER)
    private final int mQuestionNumber;

    public Reference(@NonNull String reference,
                     @NonNull String text,
                     int questionNumber) {
        mReference = reference;
        mText = text;
        mQuestionNumber = questionNumber;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getReference() {
        return mReference;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    public int getQuestionNumber() {
        return mQuestionNumber;
    }
}
