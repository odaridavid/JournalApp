package aaspos.com.kayatech.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aaspos.com.kayatech.journalapp.database.JournalEntries;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.TextCardViewHolder> {
    private static final String TAG = JournalAdapter.class.getSimpleName();



    private List<JournalEntries> Entries;
    private Context context;

    public JournalAdapter(Context context, List<JournalEntries> Entries) {
        this.context = context;
        this.Entries=Entries;
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
            JournalEntries journalEntries = Entries.get(position);
            holder.textViewTitle.setText(journalEntries.getTitle());
            holder.textViewAuthor.setText(journalEntries.getAuthor());

    }

    @Override
    public int getItemCount() {
        return Entries.size();
    }

    public static class  TextCardViewHolder extends RecyclerView.ViewHolder{

        TextView textViewAuthor,textViewTitle;
        private TextCardViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.tvAuthor);
            textViewTitle = itemView.findViewById(R.id.tvTitle);
    }
    }
}
