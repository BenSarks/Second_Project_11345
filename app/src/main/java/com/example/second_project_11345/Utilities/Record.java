package com.example.second_project_11345.Utilities;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.util.Comparator;

public class Record {
    public static Comparator<Record> recordComparator = (first, second) -> Integer.compare(second.getTime(), first.getTime());
    private final int time;
    private final int coin_Count;
    private final LatLng latLng;

    public Record(int distance, int coin_Count, LatLng latLng) {
        this.time = distance;
        this.coin_Count = coin_Count;
        this.latLng = latLng;
    }

    public int getCoin_Count() {
        return this.coin_Count;
    }

    public int getTime() {
        return this.time;
    }

    @NonNull
    public String toString() {
        return "Record{time=" + this.time + ", coin_Count=" + this.coin_Count + ", latLng=" + this.latLng.toString() + '}';
    }

    public LatLng getLatLng() {
        return this.latLng;
    }
}
