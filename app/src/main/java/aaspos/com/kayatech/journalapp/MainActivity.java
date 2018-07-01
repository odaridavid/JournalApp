package aaspos.com.kayatech.journalapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;


import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;



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
    SharedPreferenceManager sharedPreferenceManager;

    ClickListener clickListener;
    private FirebaseAuth mFirebaseAuthentication;

    //Firestore Recycleradapter
    private FirestoreRecyclerAdapter<JournalEntry, journalViewHolder> adapter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        //Get current user
        mFirebaseAuthentication = FirebaseAuth.getInstance();


        //methods
        activityBegin();
        if (mFirebaseAuthentication.getCurrentUser() != null) {
            firestoreRecycler();
        }else{
            Toast.makeText(MainActivity.this,"Sign Up",Toast.LENGTH_LONG).show();
            Intent Login = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(Login);
        }

    }


    private void firestoreRecycler() {
        mFirebaseAuthentication = FirebaseAuth.getInstance();
        //GET Current User Id
        userId = mFirebaseAuthentication.getCurrentUser().getUid();
        //Retrieval Query  order
        Query query = db.collection(DATABASE_COLLECTION).whereEqualTo(USER_ID,userId)
                .orderBy(TITLE, Query.Direction.ASCENDING);


        //Journal Entry -model containing data
        //query - how data will be retrieved,no sql database
        FirestoreRecyclerOptions<JournalEntry> options = new FirestoreRecyclerOptions.Builder<JournalEntry>()
                .setQuery(query, JournalEntry.class)
                .build();

        //Adapter takes in model and view holder which is bound to layout
        adapter = new FirestoreRecyclerAdapter<JournalEntry, journalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(journalViewHolder holder, final int position, JournalEntry model) {
                holder.setIsRecyclable(false);


                holder.setJournalEntryTitle(model.getTitle());
                holder.setJournalEntryAuthor(model.getAuthor());
                holder.setJournalEntryText(model.getText());

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Passing Data To Details
                        //Database Id
                        String idBefore = db.collection(DATABASE_COLLECTION).document().getId();
                        JournalEntry journalE = new JournalEntry();
                        journalE.setId(idBefore);
                        String idAfter = journalE.getId();
//                        SharedPreferences pref = getApplicationContext().getSharedPreferences("keyPref", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = pref.edit();
//                        editor.putString("Name_Key", idAfter);
//                        editor.commit();
                        Intent yt = new Intent(MainActivity.this, DetailActivity.class);

                         yt.putExtra(idAfter,"author");
                         yt.putExtra(idAfter,"text");
                         yt.putExtra(idAfter,"title");
//                        yt.putExtra(Intent.EXTRA_KEY_EVENT,journalEntryList.get(position).getAuthor());
//                        yt.putExtra(Intent.EXTRA_REFERRER,journalEntryList.get(position).getTitle());

                       MainActivity.this.startActivity(yt);
                        //Toast.makeText(MainActivity.this,g,Toast.LENGTH_SHORT).show();
                    }
                });


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

        //opens add entry activity
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

    }


    //viewholder class - inner class for use by recycler view adapter to hold views
    class journalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;

        journalViewHolder(View itemView) {
            super(itemView);
            view = itemView;


        }

        void setJournalEntryTitle(String journalEntryTitle) {
            if (!journalEntryTitle.isEmpty()) {
                TextView tvTitleRecyclerList = view.findViewById(R.id.text_title_recycler_list);
                tvTitleRecyclerList.setText(journalEntryTitle);
            }

        }

        void setJournalEntryAuthor(String journalEntryAuthor) {
            if (!journalEntryAuthor.isEmpty()) {
                TextView tvAuthorRecyclerList = view.findViewById(R.id.text_author_recycler_list);
                tvAuthorRecyclerList.setText(journalEntryAuthor);
            }
        }

        void setJournalEntryText(String journalEntryText) {
            if (!journalEntryText.isEmpty()) {
                TextView tvTextRecyclerList = view.findViewById(R.id.text_entry_recycler_list);
                tvTextRecyclerList.setText(journalEntryText);
            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
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
        //sign out
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent out = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(out);
        }

        @Override
        protected void onStart() {
            super.onStart();
            //firestore listening starts from database
            if (mFirebaseAuthentication.getCurrentUser() != null) {
                adapter.startListening();
            }else{
                Intent Login = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(Login);
            }

        }

        @Override
        protected void onStop() {
            super.onStop();
            //firestore adapter listening stops from database
            if (mFirebaseAuthentication.getCurrentUser() != null) {
                adapter.stopListening();
            }
        }
//
    }


//}


