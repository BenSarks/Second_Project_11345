package com.example.second_project_11345.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.second_project_11345.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapFragment extends Fragment {
    MarkerOptions markerOptions;
    SupportMapFragment supportMapFragment;

    public GoogleMapFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        this.supportMapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        this.markerOptions = new MarkerOptions();
        return view;
    }

    public void showLocation(LatLng latLng) {
        this.markerOptions.position(latLng);
        if (this.supportMapFragment != null) {
            this.supportMapFragment.getMapAsync((googleMap) -> {
                googleMap.clear();
                googleMap.addMarker(this.markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0F));
            });
        }

    }
}
