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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    //Constants
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String DATABASE_DOCUMENT = "Entry";
    private static final String DATABASE_COLLECTION = "Journal";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String TEXT = "text";
    private static final String TIMESTAMP = "timestamp";

    private FirebaseFirestore db;

    TextView tvTitle;
    TextView tvAuthor;
    TextView tvEntry;

    boolean boolUpdate = false;
    FloatingActionButton fabSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.text_title_view);
        tvAuthor = findViewById(R.id.text_author_view);
        tvEntry = findViewById(R.id.text_entry_view);
        fabSetView = findViewById(R.id.fab_edit_entry);




            String title = getIntent().getStringExtra("TITLE");
            String author = getIntent().getStringExtra("AUTHOR");
            String entry = getIntent().getStringExtra("TEXT");


            tvTitle.setText(title);
            tvAuthor.setText(author);
            tvEntry.setText(entry);







        fabSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modify
                String titleUpdate =   tvTitle.getText().toString();
                String authorUpdate =  tvAuthor.getText().toString();
                String entryUpdate =   tvEntry.getText().toString();

              //  Toast.makeText(DetailActivity.this,"Update Me",Toast.LENGTH_LONG).show();
                Map<String, Object> updateEntry = new HashMap<>();
                updateEntry.put(TITLE, titleUpdate);
                updateEntry.put(AUTHOR,authorUpdate );
                updateEntry.put(TEXT,entryUpdate );
                updateEntry.put(TIMESTAMP, FieldValue.serverTimestamp());

                db.collection("cities").document("LA")
                        .set(updateEntry)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });

}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


}
