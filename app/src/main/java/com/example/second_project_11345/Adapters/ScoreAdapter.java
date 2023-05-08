package com.example.second_project_11345.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.second_project_11345.Interfaces.RecordCallback;
import com.example.second_project_11345.R;
import com.example.second_project_11345.Utilities.Record;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.RecordViewHolder> {
    private final ArrayList<Record> records;
    private RecordCallback recordCallback;

    public ScoreAdapter(ArrayList<Record> records) {
        this.records = records;
    }

    public void setRecordCallback(RecordCallback recordCallback) {
        this.recordCallback = recordCallback;
    }

    @NonNull
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("passed VT:", "" + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecordViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = this.getItem(position);
        holder.record_LBL_position.setText("Place: " + String.format("%d", position + 1));
        holder.record_LBL_time.setText("Time: " + String.format("%d", record.getTime()));
        holder.record_LBL_coins.setText("Coins: " + String.format("%d", record.getCoin_Count()));
    }

    public int getItemCount() {
        return this.records == null ? 0 : this.records.size();
    }

    private Record getItem(int position) {
        return this.records.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView record_LBL_position;
        private final MaterialTextView record_LBL_time;
        private final MaterialTextView record_LBL_coins;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            this.record_LBL_position = itemView.findViewById(R.id.place_textview);
            this.record_LBL_time = itemView.findViewById(R.id.time_count_textview);
            this.record_LBL_coins = itemView.findViewById(R.id.coin_count_textview);
            itemView.setOnClickListener((v) -> {
                if (ScoreAdapter.this.recordCallback != null) {
                    ScoreAdapter.this.recordCallback.itemClicked(ScoreAdapter.this.getItem(this.getAdapterPosition()));
                }

            });
        }
    }
}
