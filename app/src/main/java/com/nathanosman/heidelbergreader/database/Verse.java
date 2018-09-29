package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

@Entity(tableName = Verse.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Question.class,
                parentColumns = Question.COLUMN_NUMBER,
                childColumns = Verse.COLUMN_QUESTION_ID,
                onDelete = ForeignKey.CASCADE
        ))
public class Verse {

    public static final String TABLE_NAME = "verses";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_QUESTION_ID = "question_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    private final int mId;

    @NonNull
    @ColumnInfo(name = COLUMN_REFERENCE)
    private final String mReference;

    @NonNull
    @ColumnInfo(name = COLUMN_TEXT)
    private final String mText;

    @ColumnInfo(name = COLUMN_QUESTION_ID)
    private final int mQuestionId;

    public Verse(int id,
                 @NonNull String reference,
                 @NonNull String text,
                 int questionId) {
        mId = id;
        mReference = reference;
        mText = text;
        mQuestionId = questionId;
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

    public static Verse fromContentValues(ContentValues values) {
        return new Verse(
                values.getAsInteger(COLUMN_ID),
                values.getAsString(COLUMN_REFERENCE),
                values.getAsString(COLUMN_TEXT),
                values.getAsInteger(COLUMN_QUESTION_ID)
        );
    }
}
