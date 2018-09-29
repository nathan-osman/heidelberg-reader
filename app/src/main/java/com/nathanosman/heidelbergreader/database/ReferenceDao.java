package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface ReferenceDao {

    @Insert
    void insert(Reference reference);

    @Query("SELECT * FROM '" + Reference.TABLE_NAME + "' WHERE " + Reference.COLUMN_QUESTION_NUMBER + " = :number")
    Reference[] selectByQuestionNumber(final int number);
}
