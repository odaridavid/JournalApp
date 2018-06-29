package aaspos.com.kayatech.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

interface AdapterClickListener {
    void onEntryClicked(JournalEntry journalEntry);
}
public class JournalAdapter extends Adapter<JournalAdapter.TextCardViewHolder>implements AdapterClickListener {

    private AdapterClickListener clickListener;
    private List<JournalEntry> journalEntries;
    private Context context;

    public JournalAdapter(Context context, List<JournalEntry> journalEntries,AdapterClickListener clickListerner) {
        this.context = context;
        this.clickListener = clickListerner;
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


    public class  TextCardViewHolder extends RecyclerView.ViewHolder{

        TextView textViewAuthor,textViewTitle,textViewText;
        private TextCardViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.text_author_recycler_list);
            textViewTitle = itemView.findViewById(R.id.text_title_recycler_list);
            textViewTitle = itemView.findViewById(R.id.text_date_recycler_list);

    }
        void bind(final JournalEntry journalEntry, final int position) {
            textViewTitle.setText(journalEntry.getTitle());
            textViewText.setText(journalEntry.getText());
            textViewAuthor.setText(journalEntry.getAuthor());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onEntryClicked(journalEntries.get(position));
                }
            });
        }
    }
}
