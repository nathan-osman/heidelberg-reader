package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.NonNull;

@Entity(tableName = Verse.TABLE_NAME)
public class Verse {

    public static final String TABLE_NAME = "verses";
    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_TEXT = "text";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = COLUMN_REFERENCE)
    private final String mReference;

    @ColumnInfo(name = COLUMN_TEXT)
    private final String mText;

    public Verse(@NonNull String reference, @NonNull String text) {
        mReference = reference;
        mText = text;
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
                values.getAsString(COLUMN_REFERENCE),
                values.getAsString(COLUMN_TEXT)
        );
    }
}
