package aaspos.com.kayatech.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {
    //Constants
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String DATABASE_DOCUMENT = "Entry";
    private static final String DATABASE_COLLECTION = "Journal";
    public static final String VALUES = "text_value";

    private FirebaseFirestore db;
    private JournalEntry journalEntry;

    DocumentReference dbReference;
    String getID;

    TextView tvTitle;
    TextView tvAuthor;
    TextView tvEntry;
    TextView timeTextView;

    FloatingActionButton fabSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();
        journalEntry = getIntent().getParcelableExtra(VALUES);
        final String id = db.collection(DATABASE_COLLECTION).document().getId();

        Intent intent = getIntent();
        if (intent != null) {
            String Title = intent.getStringExtra(Intent.EXTRA_TEXT);
            String Author= intent.getStringExtra( Intent.EXTRA_TEXT);
            String Text = intent.getStringExtra(Intent.EXTRA_TEXT);
            tvTitle = findViewById(R.id.text_title_view);
            tvAuthor = findViewById(R.id.text_author_view);
            tvEntry = findViewById(R.id.edit_text_entry);

            tvTitle.setText(Title);
            tvAuthor.setText(Author);
            tvEntry.setText(Text);

        }
        //add timestamp later

         fabSetView = findViewById(R.id.fab_edit_entry);

        fabSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, EnterEntriesActivity.class);
            //    if (id == FirebaseFirestore.getInstance().toString().){}
               // intent.putExtra(EnterEntriesActivity.VALUES, journalEntry);
                startActivity(intent);
            }
        });

}

//void updateUI(final JournalEntry journalEntry){
//        tvTitle.setText(journalEntry.getTitle());
//        tvAuthor.setText(journalEntry.getAuthor());
//        tvEntry.setText(journalEntry.getText());
//        getID = journalEntry.getId();
//

//
//}

private void DeleteData(){
    DocumentReference ref = db.collection(DATABASE_COLLECTION).document();
    String myId = ref.getId();
    db.collection(DATABASE_COLLECTION)
            .document(myId)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, getString(R.string.entry_delete_snapshot));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, getString(R.string.error_document_delete_snapshot), e);
                }
            });
        finish();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            DeleteData();
        }
        return super.onOptionsItemSelected(item);
    }

}
