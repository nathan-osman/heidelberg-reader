package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

@Dao
public interface VerseDao {

    @Insert
    long insert(Verse verse);

    @Query("SELECT * FROM " + Verse.TABLE_NAME + " WHERE " + Verse.COLUMN_REFERENCE + " = :reference")
    Cursor selectById(String reference);
}
