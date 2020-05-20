package com.example.autocompleteplacesdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText addressField;
    TextView addressText;
    
    public static final int AUTOCOMPLETE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressField = findViewById(R.id.editText);
        addressText = findViewById(R.id.textview);

        if (!Places.isInitialized()) {
            Places.initialize(this, API_KEY);
        }


        addressField.setFocusable(false);

        addressField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchCalled();
            }
        });
    }

    private void onSearchCalled() {
        // Initialize the places list
        List<Place.Field> placesList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

        // Start the intent
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, placesList).setCountry("NG") //NIGERIA
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            LatLng latAndLon = place.getLatLng();

            addressField.setText(place.getAddress());
            addressText.setText("Latitude: " + latAndLon.latitude + "\n");
            addressText.append("Longitude: " + latAndLon.longitude + "\n");
        } else {
            Status status = Autocomplete.getStatusFromIntent(data);
            addressText.setText(status.getStatusMessage());
        }
    }
}
