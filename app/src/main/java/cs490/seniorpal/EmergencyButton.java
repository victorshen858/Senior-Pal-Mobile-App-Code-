package cs490.seniorpal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EmergencyButton extends AppCompatActivity {
    ImageView btnEmergency;
    ToneGenerator toneG;
    Vibrator vibrator;
    // GPSTracker class
    GPSTracker gps;
    TextView loc, emergencyText;
    public double latitude,longitude=0.0;
    //userName of person using device, can be changed in settings tab
    String userName = "";
    String gpsLocationString="";
    CountDownTimer tenSecondsTimer;
    Toast toast;
    LinearLayout toastLayout;
    TextView toastTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null) { ab.setDisplayHomeAsUpEnabled(true); }

        //GET GPS LOCATION CODE
        emergencyText = (TextView) findViewById(R.id.emergency_text);
        loc = (TextView) findViewById(R.id.current_location_text);
        gps = new GPSTracker(EmergencyButton.this);
        userName = ""+ AutomaticEmailActivity.getUserFirstName() + " " + AutomaticEmailActivity.getUserLastName();
        updateLocation();




        //HELP BUTTON CODE
        btnEmergency = (ImageView) findViewById(R.id.help_button);
        btnEmergency.setEnabled(true);
        btnEmergency.setClickable(true);
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateLocation();
                //if there are no contacts the feature will not work
                if(AutomaticEmailActivity.getNumberOfEmergencyContacts() <= 0){
                    toast = Toast.makeText(EmergencyButton.this, "No emergency contacts have been set up yet. Redirecting you to 'Settings'.", Toast.LENGTH_LONG);
                    toastLayout = (LinearLayout) toast.getView();
                    toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(800);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();
                    Intent goSettings = new Intent(EmergencyButton.this, AutomaticEmailActivity.class);
                    startActivity(goSettings);
                }
                else{
                    btnEmergency.setEnabled(false);
                    btnEmergency.setClickable(false);
                    //large toast text
                    toast = Toast.makeText(EmergencyButton.this, "Emergency! " + userName +
                            " is in trouble!\nSending distress signal to all listed contacts.", Toast.LENGTH_LONG);
                    toastLayout = (LinearLayout) toast.getView();
                    toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(800);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();


                //tone generator
                toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 120000);
                toneG.startTone(ToneGenerator.TONE_DTMF_8, 120000);
                vibrator = (Vibrator) EmergencyButton.this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(120000);
                tenSecondsTimer = new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        emergencyText.setText("           " + millisUntilFinished / 1000 + " seconds remaining until distress signal sent:"
                                        + "\nHit 'Back' button on device within 10 seconds to cancel signal"
                        );
                    }

                    public void onFinish() {
                        Intent emergencyIntent = new Intent(EmergencyButton.this, AutomaticEmailActivity.class);
                        emergencyIntent.putExtra("gpsLocation", gpsLocationString);
                        emergencyIntent.putExtra("emergencyFlag", "true");
                        startActivity(emergencyIntent);
                    }
                }.start();
            }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(toneG!=null){
                    toneG.stopTone();
                }
                if(vibrator!=null) {
                    vibrator.cancel();
                }
                if(tenSecondsTimer!=null) {
                    tenSecondsTimer.cancel();
                }
                if(btnEmergency!=null){
                    btnEmergency.setClickable(true);
                }
                finish();
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void onBackPressed() {
        if(toneG!=null){
            toneG.stopTone();
        }
        if(vibrator!=null) {
            vibrator.cancel();
        }
        if(tenSecondsTimer!=null) {
            tenSecondsTimer.cancel();
        }
        if(btnEmergency!=null){
             btnEmergency.setClickable(true);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(btnEmergency!=null){
            btnEmergency.setClickable(true);
            btnEmergency.setEnabled(true);
        }
        if(toneG!=null){
            toneG.stopTone();
        }
        if(vibrator!=null) {
            vibrator.cancel();
        }
    }

    public void updateLocation(){
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if (latitude != 0.0 && longitude != 0.0) {
                // \n is for new line
                loc.setText("Your current GPS Location is at: \nLatitude: " + latitude + "\nLongitude: " + longitude);
                gpsLocationString="current GPS Location is\n" + "Latitude: " + latitude + "\nLongitude: " + longitude;
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }
    }

}
