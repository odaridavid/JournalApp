package aaspos.com.kayatech.journalapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

interface AdapterClickListener {
    void onEntryClicked(JournalEntry journalEntry);
}
public class JournalAdapter extends Adapter<JournalAdapter.TextCardViewHolder>implements AdapterClickListener {

    private AdapterClickListener clickListener;
    private List<JournalEntry> journalEntries;
    private Context context;
    private FirebaseFirestore firestoreDB;
    ClickListener clickListenerInterfac;

    public JournalAdapter(Context context, List<JournalEntry> journalEntries, FirebaseFirestore firestoreDB, AdapterClickListener clickListerner) {
        this.context = context;
        this.clickListener = clickListerner;
        this.firestoreDB = firestoreDB;
        swapJournalInformation(journalEntries);

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

    @Override
    public void onEntryClicked(JournalEntry journalEntry) {

    }
    public void removeItem(int position) {
        journalEntries.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(JournalEntry item, int position) {
        journalEntries.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onEntryClicked(journalEntries.get(position));
                    Intent openNewActivity = new Intent(context, DetailActivity.class);
                    openNewActivity.putExtra("title", journalEntry.getTitle());
                    openNewActivity.putExtra("author", journalEntry.getAuthor());
                    openNewActivity.putExtra("text", journalEntry.getText());
                    context.startActivity(openNewActivity);
                }
            });
        }


        @Override
        public void onClick(View v) {
            clickListenerInterfac.onItemClick(getAdapterPosition(), v);
        }

    }

}
