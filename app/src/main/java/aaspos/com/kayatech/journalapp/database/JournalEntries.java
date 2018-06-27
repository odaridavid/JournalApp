package aaspos.com.kayatech.journalapp.database;

public class JournalEntries {
    private String title;
    private String author;
    private String journalEntry;
    private String date;
    public void DiaryEntry(){

    }


    public JournalEntries(String title,String author,String journalEntry,String date){
        this.title = title;
        this.journalEntry = journalEntry;
        this.author = author;
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(String text) {
        this.journalEntry = text;
    }


}
