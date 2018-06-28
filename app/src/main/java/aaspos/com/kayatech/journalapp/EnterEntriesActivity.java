package aaspos.com.kayatech.journalapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import aaspos.com.kayatech.journalapp.database.JournalEntries;

public class EnterEntriesActivity extends AppCompatActivity {

    private static final String TAG = EnterEntriesActivity.class.getSimpleName();

    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String TEXT = "text";

    private static final String DATABASE_DOCUMENT = "Entry";
    private static final String DATABASE_COLLECTION = "Journal";
    private static final String TIMESTAMP = "timestamp";
    public static final String VALUES = "text_value";

    private FirebaseFirestore db;
    private JournalEntry journalEntry;
    DocumentReference dbReference;
    String getID;

    EditText etTitle;
    EditText etAuthor;
    EditText etTextEntry;
    boolean boolUpdate = false;
    FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_entries);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        db = FirebaseFirestore.getInstance();
        dbReference = db.collection(DATABASE_COLLECTION).document(DATABASE_DOCUMENT);

        fabAddNote = findViewById(R.id.fab_add_notes);

        etTitle = findViewById(R.id.edit_title);
        etAuthor = findViewById(R.id.edit_author);
        etTextEntry = findViewById(R.id.edit_text_entry);

        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra(VALUES)) {
            boolUpdate = true;
            journalEntry = getIntent().getParcelableExtra(VALUES);
            etTitle.setText(journalEntry.getTitle());
            etAuthor.setText(journalEntry.getAuthor());
            etTextEntry.setText(journalEntry.getText());
            getID = journalEntry.getId();

        }
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boolUpdate) {
                    updateJournalEntry();
                } else {
                    addJournalEntry();
                }
            }
        });
//        Toast toast = Toast.makeText(context, message, duration);
//        View view = toast.getView();
//
////Gets the actual oval background of the Toast then sets the colour filter
//        view.getBackground().setColorFilter(yourBackgroundColour, PorterDuff.Mode.SRC_IN);
//
////Gets the TextView from the Toast so it can be editted
//        TextView text = view.findViewById(android.R.id.message);
//        text.setTextColor(yourTextColour);
//
//        toast.show();
    }

    private void addJournalEntry() {

        Map< String, Object > newJournalEntry = new HashMap< >();
        newJournalEntry.put(TITLE, etTitle.getText().toString());
        newJournalEntry.put(AUTHOR, etAuthor.getText().toString());
        newJournalEntry.put(TEXT,etTextEntry.getText().toString());
        newJournalEntry.put("timestamp", FieldValue.serverTimestamp());
        db.collection(DATABASE_COLLECTION).add(newJournalEntry)
                .addOnSuccessListener(new OnSuccessListener < DocumentReference > () {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast toast = Toast.makeText(EnterEntriesActivity.this,getString(R.string.entry_created), Toast.LENGTH_SHORT);
                        View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                        view.getBackground().setColorFilter(Color.rgb(156,204,101), PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.BLACK);

                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(EnterEntriesActivity.this, getString(R.string.error_title) + e.toString(),
                                                      Toast.LENGTH_SHORT).show();
                                              Log.d(TAG, e.toString());
                                          }
                                      });}












//        dbReference = db.collection(DATABASE_COLLECTION).document(DATABASE_DOCUMENT);
//        dbReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });


    private void updateJournalEntry() {

        dbReference = db.collection(DATABASE_COLLECTION).document(DATABASE_COLLECTION);
        dbReference.update(TITLE, etTitle.getText().toString());
        dbReference.update(AUTHOR, etAuthor.getText().toString());
        dbReference.update(TIMESTAMP, FieldValue.serverTimestamp());
        dbReference.update(TEXT, etTextEntry.getText().toString())
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast toast = Toast.makeText(EnterEntriesActivity.this,getString(R.string.entry_update_msg), Toast.LENGTH_SHORT);
                        View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
                        view.getBackground().setColorFilter(Color.rgb(156,204,101), PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.BLACK);

                        toast.show();

                        Intent intent = new Intent(EnterEntriesActivity.this, MainActivity.class);
                        startActivity(intent);
    }
    });
        finish();
}
private JournalEntry getData() {
    journalEntry = new JournalEntry();
    journalEntry.setTitle(etTitle.getText().toString());
    journalEntry.setAuthor(etAuthor.getText().toString());
    journalEntry.setText(etTextEntry.getText().toString());
    journalEntry.setId(getID);
    return journalEntry;
}}
