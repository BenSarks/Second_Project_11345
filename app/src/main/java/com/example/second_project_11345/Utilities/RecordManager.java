package com.example.second_project_11345.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecordManager {
    private static final String RECORD_FILE = "RECORD_FILE";
    private static RecordManager instance = null;
    public ArrayList<Record> recordList;
    private final SharedPreferences sharedPreferences;

    private RecordManager(@NonNull Context context) {
        this.sharedPreferences = context.getSharedPreferences(RECORD_FILE, 0);
    }

    public static RecordManager getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new RecordManager(context);
        }
    }

    public void saveRecord(int time, int coinsNum, LatLng latlang) {
        Record record = new Record(time, coinsNum, latlang);
        boolean toAdd = true;
        if (this.recordList == null) {
            this.recordList = new ArrayList();
            this.recordList.add(record);
        } else if (this.recordList.size() <= 10) {
            for(int i = 0; i < this.recordList.size(); ++i) {
                if ((this.recordList.get(i)).getLatLng().latitude == latlang.latitude && (this.recordList.get(i)).getLatLng().longitude == latlang.longitude) {
                    toAdd = false;
                    if ((this.recordList.get(i)).getTime() < time) {
                        this.recordList.remove(i);
                        i = this.recordList.size();
                        this.recordList.add(record);
                        this.recordList.sort(Record.recordComparator);
                    }
                }
            }
            if (toAdd) {
                if (this.recordList.size() == 10) {
                    if ((this.recordList.get(9)).getTime() < time) {
                        this.recordList.remove(9);
                        this.recordList.add(record);
                        this.recordList.sort(Record.recordComparator);
                    }
                } else if (this.recordList.size() < 10) {
                    this.recordList.add(record);
                    this.recordList.sort(Record.recordComparator);
                }
            }
        }

        this.saveData();
    }

    public void saveData() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.recordList);
        editor.putString("key", json);
        editor.apply();
    }

    public void loadData() {
        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("key", null);
        Type type = (new TypeToken<ArrayList<Record>>() {
        }).getType();
        this.recordList = gson.fromJson(json, type);
    }
}
