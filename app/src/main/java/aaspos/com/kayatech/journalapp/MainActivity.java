package aaspos.com.kayatech.journalapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


import java.util.List;


public class MainActivity extends AppCompatActivity {
    //Constants
    private static final String TITLE = "title";
    private static final String DATABASE_COLLECTION = "Journal";

    JournalAdapter jadapter;
    List<JournalEntry> journalEntryList;
    FirebaseFirestore db;
    RecyclerView mRecyclerView;
    JournalEntry journalEntry;


    //Firestore adapter
    private FirestoreRecyclerAdapter<JournalEntry, journalViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        activityBegin();

        //Retrieval Query  order
        Query query = db.collection(DATABASE_COLLECTION)
                .orderBy(TITLE, Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<JournalEntry> options = new FirestoreRecyclerOptions.Builder<JournalEntry>()
                .setQuery(query, JournalEntry.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<JournalEntry, journalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(journalViewHolder holder, int position, JournalEntry model) {
                holder.setProductName(model.getTitle());
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
        jadapter = new JournalAdapter(this, journalEntryList, new AdapterClickListener() {
            @Override
            public void onEntryClicked(JournalEntry journalEntry) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.VALUES, journalEntry);
                startActivity(i);
            }
        });
    }


    class journalViewHolder extends RecyclerView.ViewHolder {
        private View view;

        journalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setProductName(String productName) {
            TextView textView = view.findViewById(R.id.text_title_recycler_list);
            textView.setText(productName);
        }
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


//         query = FirebaseFirestore.getInstance()
//                .collection(DATABASE_COLLECTION)
//                .orderBy(TIMESTAMP)
//                .limit(50);
//
//
//        db.collection(DATABASE_COLLECTION )
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//
//
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    // Handle error
//                    Log.e(TAG,"ERROR",e);
//                    //...
//                    return ;
//                }

//                List<JournalEntry> chats = queryDocumentSnapshots.toObjects(JournalEntry.class);
//                PagedList.Config config = new PagedList.Config.Builder()
//                        .setEnablePlaceholders(false)
//                        .setPrefetchDistance(10)
//                        .setPageSize(20)
//                        .build();





}
