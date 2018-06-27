package aaspos.com.kayatech.journalapp;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import aaspos.com.kayatech.journalapp.database.JournalEntries;

public class MainActivity extends AppCompatActivity  {
    //Constants
    private static String  TAG=MainActivity.class.getSimpleName();
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String TEXT= "text";
    private static final String DATE= "date";
    private static final String DATABASE_COLLECTION = "Journal";
    private static final String DATABASE_DOCUMENT = "Entry";



    JournalAdapter AdapterClass;
    List<JournalEntries> entryList;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

      //  CollectionReference usersCollectionRef = db.collection("users");
        entryList = new ArrayList<>();

//        //RecyclerView
//        RecyclerView rvRecyclerView;
//        rvRecyclerView = findViewById(R.id.recycler_journal_items);
//        rvRecyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rvRecyclerView.setLayoutManager(layoutManager);
//
//        AdapterClass = new JournalAdapter(this,entryList);
//        rvRecyclerView.setAdapter(AdapterClass);
//
//        FloatingActionButton fabAddCard;
//        fabAddCard = findViewById(R.id.floatingbutton_add_entry);
//        fabAddCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Starts the add activity
//                addNoteCard();
//            }
//        });

    }
    protected void addNoteCard(){
        Map<String, Object> textEntry = new HashMap<>();
        textEntry.put(TITLE,getString(R.string.entry_title));
        textEntry.put(TEXT, getString(R.string.entry_text));
        textEntry.put(AUTHOR, getString(R.string.entry_author));
        textEntry.put(DATE,new Date());

        db.collection(DATABASE_COLLECTION)
                .document(DATABASE_DOCUMENT)
                .set(textEntry)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, getString(R.string.entry_created_log));
                        Toast.makeText(MainActivity.this,getString(R.string.entry_created),Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, getString(R.string.error_document_failed), e);
                        Toast.makeText(MainActivity.this,getString(R.string.error_title)+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void realtimeUpdate(){
        DocumentReference journalDbReference =
                db.collection(DATABASE_COLLECTION)
                .document(DATABASE_DOCUMENT);
        journalDbReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.d(getString(R.string.error_title), e.getMessage());
                    return;
                }
                if(documentSnapshot != null && documentSnapshot.exists()){

                }
            }
        });
    }
    private void deleteEntry(){
              db.collection(DATABASE_COLLECTION)
                .document(DATABASE_DOCUMENT)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,getString(R.string.entry_delete_msg),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEntry(){
        DocumentReference journalDbReference = db.collection(DATABASE_COLLECTION).document(DATABASE_DOCUMENT);
        journalDbReference.update(TITLE,getString(R.string.entry_title));
        journalDbReference.update(AUTHOR,getString(R.string.entry_author));
        journalDbReference.update(TEXT,getString(R.string.entry_text)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,getString(R.string.entry_update_msg),Toast.LENGTH_SHORT).show();
            }
        });
        journalDbReference.update(DATE,new Date());
    }

    private  void getDiaryEntries(){
        Query query = db.collection(DATABASE_COLLECTION);


    }
}
