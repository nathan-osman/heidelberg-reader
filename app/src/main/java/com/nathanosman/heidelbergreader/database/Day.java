package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = Day.TABLE_NAME)
public class Day {

    public static final String TABLE_NAME = "days";
    public static final String COLUMN_NUMBER = "number";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_NUMBER)
    private final int mNumber;

    public Day(int number) {
        mNumber = number;
    }

    public int getNumber() {
        return mNumber;
    }
}
