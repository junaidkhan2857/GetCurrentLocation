package co.proglabs.sampleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import co.proglabs.getcurrentlocation.GetCurrentLocation;


public class SampleActivity extends AppCompatActivity {

    double currentLatitude, currentLongitude;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

                currentLatitude = locationResult.getLastLocation().getLatitude();
                currentLongitude = locationResult.getLastLocation().getLongitude();


                Log.i("MainActivity", "latitude : " + currentLatitude + " longitude : " + currentLongitude);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetCurrentLocation getCurrentLocation = new GetCurrentLocation(SampleActivity.this);

        getCurrentLocation.initFusedProvider(SampleActivity.this);
        getCurrentLocation.getCurrentLocation( 0, 1000, 1000, locationCallback);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2857) {
            switch (resultCode) {
                case Activity.RESULT_OK:

                    GetCurrentLocation.requestNewLocation();

                    break;
                case Activity.RESULT_CANCELED:

                    Toast.makeText(this, "You need to enable the location services", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 44) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (GetCurrentLocation.isLocationEnabled()) {

                    GetCurrentLocation.requestNewLocation();

                } else {

                    GetCurrentLocation.enableLocationSettings();

                }

            }
        }
    }
}