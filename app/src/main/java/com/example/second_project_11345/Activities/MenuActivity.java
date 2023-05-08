package com.example.second_project_11345.Activities;

import static com.example.second_project_11345.Activities.MainActivity.DELAY_DURATION;
import static com.example.second_project_11345.Activities.MainActivity.LATLNG_VALUE;
import static com.example.second_project_11345.Activities.MainActivity.SAVE_RECORD;
import static com.example.second_project_11345.Activities.MainActivity.SENSOR_MODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.second_project_11345.R;
import com.example.second_project_11345.Utilities.RecordManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {
    private RadioButton radioButton_Slow;
    private RadioButton radioButton_Fast;
    private RadioButton radioButton_Sensor;
    private Button button_start;
    private LatLng latLng;
    public TextView userDirectiveTextView;
    private MaterialButton materialButton_Score;
    private int delay = 700;
    private boolean sensorModeBoolean = false;
    private boolean hasGrantedLocation;

    public MenuActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_menu);
        this.initAllValues();
        this.setButtonListeners();
        this.getUserLocationOnStart();
    }

    @SuppressLint({"SetTextI18n"})
    private void initAllValues() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Menu");
        this.materialButton_Score = this.findViewById(R.id.materialButton_game_start);
        this.radioButton_Slow =this.findViewById(R.id.radioButton_Slow);
        this.radioButton_Fast =this.findViewById(R.id.radioButton_Fast);
        this.radioButton_Sensor =this.findViewById(R.id.radioButton_Sensor);
        this.button_start = this.findViewById(R.id.button_start);
        userDirectiveTextView = this.findViewById(R.id.text_user_directive);
        userDirectiveTextView.setText("Please select the preferred settings:");
        this.radioButton_Slow.setText("Button mode -Slow");
        this.radioButton_Fast.setText("Button mode -Fast");
        this.radioButton_Sensor.setText("Sensor mode");
        this.button_start.setText("Start");
        this.materialButton_Score.setText("Score Table");

        LatLng STOCKHOLM = new LatLng(59.3251172, 18.0710935);
        LatLng SANDI = new LatLng(32.715736, -117.161087);
        LatLng MACAO = new LatLng(22.1899448, 113.5380454);
        LatLng PARIS = new LatLng(48.8588897, 2.320041);
        LatLng BERLIN = new LatLng(52.5170365, 13.3888599);
        LatLng LONDON = new LatLng(51.5073359, -0.12765);
        RecordManager.getInstance().saveRecord(98, 76, STOCKHOLM);
        RecordManager.getInstance().saveRecord(83, 60, SANDI);
        RecordManager.getInstance().saveRecord(73, 15, MACAO);
        RecordManager.getInstance().saveRecord(72, 36, PARIS);
        RecordManager.getInstance().saveRecord(61, 22, BERLIN);
        RecordManager.getInstance().saveRecord(44, 20, LONDON);

        for(int i = 0; i < RecordManager.getInstance().recordList.size(); ++i) {
            Log.d("Log_Print_Record", "" + RecordManager.getInstance().recordList.get(i).toString());
        }

        Log.d("Log_Print_Record_Size", "" + RecordManager.getInstance().recordList.size());
    }

    private void setButtonListeners() {
        this.materialButton_Score.setOnClickListener((v) -> this.startScoreActivity());
        this.radioButton_Slow.setOnClickListener((v) -> this.setDelay(700));
        this.radioButton_Fast.setOnClickListener((v) -> this.setDelay(400));
        this.radioButton_Sensor.setOnClickListener((v) -> this.setSensorModeBoolean());
        this.button_start.setOnClickListener((v) -> this.startGameActivity());
    }

    private void setDelay(int delay) {
        this.delay = delay;
        Log.d("Log_Delay", "" + delay);
    }

    private void setSensorModeBoolean() {
        this.sensorModeBoolean = true;
        this.setDelay(900);
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(DELAY_DURATION, this.delay);
        intent.putExtra(SENSOR_MODE, this.sensorModeBoolean);
        intent.putExtra(SAVE_RECORD, this.hasGrantedLocation);
        if (this.hasGrantedLocation) {
            intent.putExtra(LATLNG_VALUE, this.latLng);
        }

        this.startActivity(intent);
    }

    private void startScoreActivity() {
        Intent intent = new Intent(this, ScoreBoardActivity.class);
        this.startActivity(intent);
    }

    private void getUserLocationOnStart() {
        this.hasGrantedLocation = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0;
        if (!this.hasGrantedLocation) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            client.getLastLocation().addOnSuccessListener((location) -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    this.latLng = new LatLng(latitude, longitude);
                    Log.d("LOCATION", "getUserLocationOnStart: " + latitude + longitude);
                } else {
                    this.hasGrantedLocation = false;
                    Log.d("Game Record won't be save", " No location found ");
                }

            });
        }

    }
}
