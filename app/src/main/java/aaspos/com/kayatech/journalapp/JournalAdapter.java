package aaspos.com.kayatech.journalapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class JournalAdapter extends Adapter<JournalAdapter.TextCardViewHolder> {
    public static ClickListener clickListenerInterface;
    private Activity context;
    private FirebaseFirestore firestoreDB;
    private List<JournalEntry> journalEntries;


    public JournalAdapter(List<JournalEntry> list, Activity ctx,   FirebaseFirestore firestore) {
        journalEntries = list;
        context = ctx;
        firestoreDB = firestore;
    }

    @NonNull
    @Override
    public TextCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdForListItem = R.layout.list_recycler_items;
        View view = inflater.inflate(layoutIdForListItem, parent,false);

        return new TextCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextCardViewHolder holder, int position) {
        final int itemPos = position;
        final JournalEntry model = journalEntries.get(position);

        holder.bind(journalEntries.get(position),position);



    }

    @Override
    public int getItemCount() {
        if(journalEntries == null){
            return 0;
        }
        return journalEntries.size();
    }

    public void swapJournalInformation(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
        this.notifyDataSetChanged();
    }


    public class  TextCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewAuthor,textViewTitle,textViewText;

        private TextCardViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_title_recycler_list);
            textViewTitle = itemView.findViewById(R.id.text_author_recycler_list);



    }
        void bind(final JournalEntry journalEntry, final int position) {
            textViewTitle.setText(journalEntry.getTitle());
            textViewText.setText(journalEntry.getText());
            textViewAuthor.setText(journalEntry.getAuthor());

        }


        @Override
        public void onClick(View v) {
            clickListenerInterface.onItemClick(getAdapterPosition(), v);
        }


    }

    public void setOnItemClickListener(ClickListener clickListener) {
        JournalAdapter.clickListenerInterface = clickListener;
        }

    public interface ClickListener {
        void onItemClick(int position ,View v);
    }
}
