package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

@Dao
public interface QuestionDao {

    @Insert
    void insert(Question question);

    @Query("SELECT * FROM " + Question.TABLE_NAME + " WHERE " + Question.COLUMN_NUMBER + " = :number")
    Cursor selectByNumber(int number);
}
