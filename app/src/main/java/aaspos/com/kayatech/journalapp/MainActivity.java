package aaspos.com.kayatech.journalapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TITLE = "title";
    private static final String DATABASE_COLLECTION = "Journal";
    private static final String USER_ID = "current_user";
    JournalAdapter jadapter;
    List<JournalEntry> journalEntryList;
    FirebaseFirestore db;
    RecyclerView mRecyclerView;
    JournalEntry journalEntry;

    ClickListener clickListener;
    private FirebaseAuth mFirebaseAuthentication;

    //Firestore Recycleradapter
    private FirestoreRecyclerAdapter<JournalEntry, journalViewHolder> adapter;
    String userId;
    List<JournalEntry> documents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        //Get current user
        mFirebaseAuthentication = FirebaseAuth.getInstance();
        userId = mFirebaseAuthentication.getCurrentUser().getUid();
        //methods
        activityBegin();
        firestoreRecycler();

    }


    private void firestoreRecycler() {
        //Retrieval Query  order
        Query query = db.collection(DATABASE_COLLECTION).whereEqualTo(USER_ID, userId)
                .orderBy(TITLE, Query.Direction.ASCENDING);


        //Journal Entry -model containing data
        //query - how data will be retrieved,no sql database
        FirestoreRecyclerOptions<JournalEntry> options = new FirestoreRecyclerOptions.Builder<JournalEntry>()
                .setQuery(query, JournalEntry.class)
                .build();

        //Adapter takes in model and view holder which is bound to layout
        adapter = new FirestoreRecyclerAdapter<JournalEntry, journalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(journalViewHolder holder, int position, JournalEntry model) {
                holder.setIsRecyclable(false);


                holder.setJournalEntryTitle(model.getTitle());
                holder.setJournalEntryAuthor(model.getAuthor());
                holder.setJournalEntryText(model.getText());
            }


            @NonNull
            @Override
            public journalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflated with recycler_view layout
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler_items, parent, false);
                return new journalViewHolder(view);
            }
        };
        //set Firestore adapter
        mRecyclerView.setAdapter(adapter);
    }


    private void activityBegin() {

        //Linear Layout Manager
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);


        journalEntryList = new ArrayList<>();
        journalEntry = new JournalEntry();


        //RecyclerView of Journal Adapter
        mRecyclerView = findViewById(R.id.recycler_journal_items);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayout);

        addEntryStart();

    }


    private void addEntryStart() {
        FloatingActionButton fabAddCard;
        fabAddCard = findViewById(R.id.floatingbutton_add_entry);
        fabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starts the add activity
                Intent enterEntries = new Intent(MainActivity.this, EnterEntriesActivity.class);
                startActivity(enterEntries);
            }
        });
        jadapter = new JournalAdapter(this, journalEntryList, db, new AdapterClickListener() {
            @Override
            public void onEntryClicked(JournalEntry journalEntry) {
                getDocumentsFromCollection();
            }
        });
    }


    //viewholder class - inner class for use by recycler view adapter to hold views
    class journalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;

        journalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setJournalEntryTitle(String journalEntryTitle) {
            TextView tvTitleRecyclerList = view.findViewById(R.id.text_title_recycler_list);
            tvTitleRecyclerList.setText(journalEntryTitle);

        }

        void setJournalEntryAuthor(String journalEntryAuthor) {
            TextView tvAuthorRecyclerList = view.findViewById(R.id.text_author_recycler_list);
            tvAuthorRecyclerList.setText(journalEntryAuthor);
        }

        void setJournalEntryText(String journalEntryText) {
            TextView tvTextRecyclerList = view.findViewById(R.id.text_entry_recycler_list);
            tvTextRecyclerList.setText(journalEntryText);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signout) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        Intent out = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    public void getDocumentsFromCollection() {
//        db.collection(DATABASE_COLLECTION).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot snap : task.getResult()) {
//                                JournalEntry model = snap.toObject(JournalEntry.class).withId(snap.getId());
//
//                                documents.add(model);
//
//
//                                JournalAdapter recyclerViewAdapter = new JournalAdapter(documents, getActivity(), );
//
//
//                                recyclerViewAdapter.setOnItemClickListener(new JournalAdapter().ClickListener()
//                                {
//                                    @Override
//                                    public void onItemClick ( int position, View v){
//                                    Log.d(TAG, "onItemClick position: " + position);
//
//                                    // Go to the details page for the selected       product
//                                    Intent i = new Intent(MainActivity.this, DetailActivity.class);
//                                    i.putExtra(DetailActivity.VALUES, journalEntry);
//                                    startActivity(i);
//
//                                }
//                                }
//
//                                mRecyclerView.setAdapter(recyclerViewAdapter);
//                                mRecyclerView.notifyDataSetChanged();
//                            }
//
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//        db.collection(DATABASE_COLLECTION)
//                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//
//
//                    }
//                });
    }


}
