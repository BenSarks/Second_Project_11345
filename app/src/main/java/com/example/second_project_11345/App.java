package com.example.second_project_11345;

import android.app.Application;
import com.example.second_project_11345.Utilities.RecordManager;
import com.example.second_project_11345.Utilities.VB_Producer;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        RecordManager.init(this);
        RecordManager.getInstance().loadData();
        VB_Producer.init(this);
    }
}
