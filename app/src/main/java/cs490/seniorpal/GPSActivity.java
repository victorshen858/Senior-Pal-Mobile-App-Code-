package cs490.seniorpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GPSActivity extends ActionBarActivity {
    Button btnShowLocation;
    Button btnMapIt;
    public double latitude,longitude=0.0;
    // GPSTracker class
    GPSTracker gps;
    TextView loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        loc = (TextView) findViewById(R.id.textLocation);
        gps = new GPSTracker(GPSActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if (latitude != 0.0 && longitude != 0.0) {
                // \n is for new line
                loc.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }

        btnMapIt= (Button) findViewById(R.id.map_it_button);
        // show location button click event
        btnMapIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    Intent startMap = new Intent(GPSActivity.this, MapsActivity.class);
                    startMap.putExtra("lat", latitude);
                    startMap.putExtra("long", longitude);
                    startActivity(startMap);
                }

        });

    }//end of onCreate method

    public void onBackPressed() {
        finish();
    }

}//end of activity

