By using this library, you can get the current location of the user in an easier way.

USAGE: 

Step 1:

Add jitpack in your build.gradle(Project: Example)

allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
  
  Add this line in your build.gradle(Module: app)
  dependencies {
	        implementation 'com.github.junaidkhan2857:GetCurrentLocation:Tag'
	}
  
  After that press sync now so you can download the library in your project.
  



Step 2:
Now you need to download the google places library for getting the location
implementation 'com.google.android.libraries.places:places:2.3.0'

Again press sync now

Step 3:
Now you need to create a location callback so you can check get the location of the user
private LocationCallback locationCallback = new LocationCallback() {
    @Override
    public void onLocationResult(LocationResult locationResult) {

            currentLatitude = locationResult.getLastLocation().getLatitude();
            currentLongitude = locationResult.getLastLocation().getLongitude();


            Log.i("MainActivity", "latitude : " + currentLatitude + " longitude : " + currentLongitude);

    }
};


Step 4:
You need to override two methods onActivityResult and onRequestPermissions like this

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


Step 5:

You need to type these two lines in the onCreate method.

GetCurrentLocation.initFusedProvider(this);
GetCurrentLocation.getCurrentLocation(this, this, 0, 1000, 1000, locationCallback);


The parameters are as follows: 
GetCurrentLocation.initFusedProvider(YourActivityName.this);
GetCurrentLocation.getCurrentLocation(YOUR_CONTEXT_HERE..this, YOUR_ACTIVITY_NAME_HERE.this, NUM_OF_UPDATES, SET_INTERVAL, SET_FASTEST_INTERVAL, locationCallback);

Set NUM_OF_UPDATES = 0 if you want to get undefined updates of the location.


