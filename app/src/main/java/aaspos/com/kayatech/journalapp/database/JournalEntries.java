package aaspos.com.kayatech.journalapp.database;

public class JournalEntries {
    private String title;
    private String author;
    private String journalEntry;

    public void DiaryEntry(){

    }


    public JournalEntries(String title,String author,String journalEntry){
        this.title = title;
        this.journalEntry = journalEntry;
        this.author = author;

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
