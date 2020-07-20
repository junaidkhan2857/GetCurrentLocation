package co.proglabs.getcurrentlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GetCurrentLocation extends AppCompatActivity {

    private static int PERMISSION_ID = 44;
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static int userDefinedNumberOfUpdates; //set 0 if you want infinite number of updates
    private static int userDefinedSetInterval; //define the interval after each time you want to get the users location
    private static int userDefinedSetFastestInterval;  //define the fastest interval after each time you want to get the users location

    private static Activity userDefinedActivity;
    private static Context userDefinedContext;

    private static LocationCallback userDefinedLocationCallback;

    public static double currentLatitude = 0.0, currentLongitude = 0.0;

    public static void initFusedProvider(Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static void getCurrentLocation(Context context, Activity activity, int numberOfUpdates, int setInterval, int setFastestInterval,
                                          LocationCallback locationCallback) {

        userDefinedActivity = activity;
        userDefinedContext = context;
        userDefinedNumberOfUpdates = numberOfUpdates;
        userDefinedSetInterval = setInterval;
        userDefinedSetFastestInterval = setFastestInterval;
        userDefinedLocationCallback = locationCallback;

        if (checkPermissions()) {

            enableLocationSettings();

        } else {

            requestPermission();

        }

    }

    public static void enableLocationSettings() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(userDefinedSetInterval);
        locationRequest.setFastestInterval(userDefinedSetFastestInterval);

        if (userDefinedNumberOfUpdates > 0) {
            locationRequest.setNumUpdates(userDefinedNumberOfUpdates);
        }

        if (!isLocationEnabled()) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(userDefinedActivity);

            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(userDefinedActivity, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // All location settings are satisfied. The client can initialize
                    // location requests here.
                    // ...

                    requestNewLocation();
                }
            });

            task.addOnFailureListener(userDefinedActivity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(userDefinedActivity,
                                    2857);

                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }
            });


        } else {

            requestNewLocation();

        }


    }

    public static boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) userDefinedContext.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }

    public static boolean checkPermissions() {

        return ActivityCompat.checkSelfPermission(userDefinedContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && (ActivityCompat.checkSelfPermission(userDefinedContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }

    public static void requestPermission() {

        ActivityCompat.requestPermissions(userDefinedActivity,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                },
                PERMISSION_ID);

    }


    public static void requestNewLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(userDefinedSetInterval);
        locationRequest.setFastestInterval(userDefinedSetFastestInterval);

        if (userDefinedNumberOfUpdates > 0) {
            locationRequest.setNumUpdates(userDefinedNumberOfUpdates);
        }

        if (ActivityCompat.checkSelfPermission(userDefinedContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(userDefinedContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, userDefinedLocationCallback, Looper.myLooper());


    }


    public static void stopLocationUpdater() {

        fusedLocationProviderClient.removeLocationUpdates(userDefinedLocationCallback);

    }


}
