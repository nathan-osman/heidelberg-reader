package com.nathanosman.heidelbergreader;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Data for an individual question in the catechism
 *
 * Question data is stored in a JSON file in the assets folder. This class mirrors the structure of
 * the file, allowing for easy deserialization with Gson. The class is also parcelable, making it
 * easy for passing from one activity to another.
 */
class Question implements Parcelable {

    @SerializedName("number")
    private int mNumber;

    @SerializedName("question")
    private String mQuestion;

    @SerializedName("answer")
    private String mAnswer;

    @SerializedName("references")
    private String[] mReferences;

    /**
     * Retrieve the question number
     */
    int getNumber() {
        return mNumber;
    }

    /**
     * Retrieve the question title
     */
    String getQuestion() {
        return mQuestion;
    }

    /**
     * Retrieve the question answer
     */
    String getAnswer() {
        return mAnswer;
    }

    /**
     * Retrieve the Scripture references for the question
     */
    String[] getReferences() {
        return mReferences;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mNumber);
        out.writeString(mQuestion);
        out.writeString(mAnswer);
        out.writeStringArray(mReferences);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     * Create a Question instance from a parcel
     */
    private Question(Parcel in) {
        mNumber = in.readInt();
        mQuestion = in.readString();
        mAnswer = in.readString();
        mReferences = in.createStringArray();
    }
}
