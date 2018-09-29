package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface DayDao {

    @Insert
    void insert(Day day);

    @Query("SELECT * FROM " + Day.TABLE_NAME + " WHERE " + Day.COLUMN_NUMBER + " = :number")
    Day selectByNumber(int number);
}
