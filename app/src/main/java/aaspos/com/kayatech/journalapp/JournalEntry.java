package aaspos.com.kayatech.journalapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class JournalEntry implements Parcelable {


    public JournalEntry(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }

    String title;
    String author;
    String text;
    String id;
    private Date mTimestamp;


    protected JournalEntry(Parcel parcel){

        title = parcel.readString();
        author = parcel.readString();
        text = parcel.readString();
        id = parcel.readString();
    }

    public static final Creator<JournalEntry> CREATOR = new Creator<JournalEntry>() {
        @Override
        public JournalEntry createFromParcel(Parcel in) {
            return new JournalEntry(in);
        }

        @Override
        public JournalEntry[] newArray(int size) {
            return new JournalEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(text);
        dest.writeString(id);
    }
}
