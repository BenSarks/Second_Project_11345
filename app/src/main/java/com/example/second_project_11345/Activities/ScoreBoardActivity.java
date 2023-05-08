package com.example.second_project_11345.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.second_project_11345.Adapters.ScoreAdapter;
import com.example.second_project_11345.Fragments.GoogleMapFragment;
import com.example.second_project_11345.Fragments.ScoreFragment;
import com.example.second_project_11345.Interfaces.RecordCallback;
import com.example.second_project_11345.R;
import com.example.second_project_11345.Utilities.RecordManager;
import com.google.android.gms.maps.model.LatLng;

public class ScoreBoardActivity extends AppCompatActivity {
    private GoogleMapFragment mapFragment;
    RecordCallback recordCallback = (record) -> this.showUserLocation(record.getLatLng());

    public ScoreBoardActivity() {
    }

    private void showUserLocation(LatLng latLng) {
        this.mapFragment.showLocation(latLng);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_score);
        this.mapFragment = new GoogleMapFragment();
        ScoreAdapter scoreAdapter = new ScoreAdapter(RecordManager.getInstance().recordList);
        ScoreFragment scoreFragment = new ScoreFragment(scoreAdapter);
        scoreFragment.setListAdapterCallBack(this.recordCallback);
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_score_table, scoreFragment).commit();
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_mapview, this.mapFragment).commit();
    }
}
