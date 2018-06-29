package aaspos.com.kayatech.journalapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    //Constants
    private static String  TAG=MainActivity.class.getSimpleName();
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String TIMESTAMP = "timestamp";

    private static final String TEXT= "text";
    private static final String DATABASE_COLLECTION = "Journal";
    private static final String DATABASE_DOCUMENT = "Entry";


    //No Progress Bar
    String client;
    int count = 0;
    JournalAdapter jadapter;
    List<JournalEntry> journalEntryList;
    FirebaseFirestore db;
    private FirebaseAuth mAuthFirebase;
    RecyclerView mRecyclerView;
    JournalEntry journalEntry;

    DocumentReference dcRef;
     Query query;
    FirestoreRecyclerAdapter fireAdapter;


    @BindView(R.id.recycler_journal_items)
    RecyclerView journalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        activityBegin();
    }

    private void activityBegin(){

        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);

        //  CollectionReference usersCollectionRef = db.collection("users");
        journalEntryList = new ArrayList<>();
        journalEntry = new JournalEntry();

        // mAdapter = new JournalAdapter(this,entryList);
        //RecyclerView
        mRecyclerView = findViewById(R.id.recycler_journal_items);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(jadapter);
        mRecyclerView.setLayoutManager(mLinearLayout);
        // use a linear layout manager

      //  mAdapter = new JournalAdapter(this,entryList);
        //methods
        noOfClicks();
        getEntries();
    }
    private void noOfClicks(){
        FloatingActionButton fabAddCard;
        fabAddCard = findViewById(R.id.floatingbutton_add_entry);
        fabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starts the add activity
                Intent enterEntries = new Intent(MainActivity.this,EnterEntriesActivity.class);
                startActivity(enterEntries);
            }
        });
        jadapter = new JournalAdapter(this, journalEntryList, new AdapterClickListener() {
            @Override
            public void onEntryClicked(JournalEntry journalEntry) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.VALUES, journalEntry);
                startActivity(i);
            }
        });
    }
    private void getEntries() {


         query = FirebaseFirestore.getInstance()
                .collection(DATABASE_COLLECTION)
                .orderBy(TIMESTAMP)
                .limit(50);


        db.collection(DATABASE_COLLECTION )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    Log.e(TAG,"ERROR",e);
                    //...
                    return ;
                }
                updateList();
//                List<JournalEntry> chats = queryDocumentSnapshots.toObjects(JournalEntry.class);
//                PagedList.Config config = new PagedList.Config.Builder()
//                        .setEnablePlaceholders(false)
//                        .setPrefetchDistance(10)
//                        .setPageSize(20)
//                        .build();



            }
        }

        );

    }

    private void updateList(){

        final Query query = FirebaseFirestore.getInstance()
                .collection(DATABASE_COLLECTION)
                .orderBy(TIMESTAMP)
                .limit(50);
        FirestoreRecyclerOptions<JournalEntry> options = new FirestoreRecyclerOptions.Builder<JournalEntry>().setLifecycleOwner(this)
                .setQuery(query, JournalEntry.class)
                .build();

        fireAdapter = new FirestoreRecyclerAdapter<JournalEntry, JournalHolder>(options) {
            @Override
            public void onBindViewHolder(JournalHolder holder, int position, final JournalEntry model) {




                holder.textAuthor.setText(model.getAuthor());
                holder.textTitle.setText(model.getTitle());
                //    holder.timeStamp.setTime(model.getTimestamp().getTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(journalList, model.getTitle()+",by "+model.getAuthor()+" at "+model.getTimestamp(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();}
                });
            } {

            }

            @Override
            public JournalHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_recycler_items, group, false);

                return new JournalHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };

    }

    @Override
    protected void onStart() {
        super.onStart();
   //  fireAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
    // fireAdapter.stopListening();
    }

    public class JournalHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView textTitle;
        @BindView(R.id.tvAuthor)
        TextView textAuthor;

//        @ServerTimestamp
//        @BindView(R.id.tvDate)
//         Date timeStamp;

        public JournalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
