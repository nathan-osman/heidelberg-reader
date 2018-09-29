package com.nathanosman.heidelbergreader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;

@Database(entities = {Reference.class, Question.class}, version = 1)
public abstract class CatechismDatabase extends RoomDatabase {

    public abstract ReferenceDao referenceDao();
    public abstract QuestionDao questionDao();
    public abstract DayDao dayDao();

    private static CatechismDatabase sInstance;

    public static synchronized CatechismDatabase getDatabase(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context,
                    CatechismDatabase.class,
                    "catechism.db"
            ).build();
        }
        return sInstance;
    }

    private class ReferenceObj {
        String reference;
        String text;
    }

    private class QuestionObj {
        int number;
        String question;
        String text;
        ReferenceObj[] references;
    }

    private class DayObj {
        int number;
        QuestionObj[] questions;
    }

    public void initialize(Context context) throws IOException {
        beginTransaction();
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(
                    new InputStreamReader(
                            context.getAssets().open("catechism.json")
                    )
            );
            for (DayObj d : (DayObj[]) gson.fromJson(reader, new TypeToken<DayObj[]>() {}.getType())) {
                dayDao().insert(new Day(d.number));
                for (QuestionObj q : d.questions) {
                    questionDao().insert(new Question(q.number, q.question, q.text, d.number));
                    for (ReferenceObj r: q.references) {
                        referenceDao().insert(new Reference(r.reference, r.text, q.number));
                    }
                }
            }
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }
}
