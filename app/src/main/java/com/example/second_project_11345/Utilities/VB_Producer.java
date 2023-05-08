package com.example.second_project_11345.Utilities;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.widget.Toast;

public class VB_Producer {
    @SuppressLint({"StaticFieldLeak"})
    private static VB_Producer instance = null;
    private static Vibrator vibrator;
    private final Context context;

    private VB_Producer(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new VB_Producer(context);
            vibrator = (Vibrator)context.getSystemService("vibrator");
        }

    }

    public static VB_Producer getInstance() {
        return instance;
    }

    public void toast(String text, int length) {
        Toast.makeText(this.context, text, length).show();
    }

    @SuppressLint({"ObsoleteSdkInt"})
    public void vibrate(long length) {
        if (VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(length, -1));
        } else {
            vibrator.vibrate(length);
        }

    }
}

