package com.example.second_project_11345.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.second_project_11345.Adapters.ScoreAdapter;
import com.example.second_project_11345.Interfaces.RecordCallback;
import com.example.second_project_11345.R;

public class ScoreFragment extends Fragment {
    private final ScoreAdapter scoreAdapter;

    public ScoreFragment(ScoreAdapter scoreAdapter) {
        this.scoreAdapter = scoreAdapter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.score_Table_ListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.scoreAdapter);
        return view;
    }

    public void setListAdapterCallBack(RecordCallback recordCallback) {
        this.scoreAdapter.setRecordCallback(recordCallback);
    }
}
