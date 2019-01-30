package cs490.seniorpal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class BathSaunaWalkTimer extends AppCompatActivity {
    NumberPicker hr,min,sec;
    TextView mTextField;
    ToneGenerator toneG;
    Vibrator vibrator;
    int timerInSeconds, timerInMilliseconds;
    Button btnCancelTimer, btnStartTimer;
    // GPSTracker class
    GPSTracker gps;
    TextView loc;
    public double latitude,longitude=0.0;
    //userName of person using device, can be changed in settings tab
    String userName ="";
    //Timer variables
    int hours=0,minutes=0,seconds=0;
    Boolean sendSignal = false;
    String gpsLocationString="";
    CountDownTimer twoMinCounter;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bath_sauna_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }


        //INITIALIZATION
        hr = (NumberPicker) findViewById(R.id.hourspicker);
        min = (NumberPicker) findViewById(R.id.minutespicker);
        sec = (NumberPicker) findViewById(R.id.secondspicker);
        hr.setMaxValue(24);
        hr.setMinValue(0);
        min.setMaxValue(60);
        min.setMinValue(0);
        sec.setMaxValue(60);
        sec.setMinValue(0);
        loc = (TextView) findViewById(R.id.location_text);
        gps = new GPSTracker(BathSaunaWalkTimer.this);
        mTextField = (TextView) findViewById(R.id.seconds_remaining_text);
        userName = getResources().getString(R.string.pref_default_display_name);
        btnStartTimer = (Button) findViewById(R.id.btn_start_timer);
        btnCancelTimer = (Button) findViewById(R.id.button_cancel_bsw_timer);
        btnStartTimer.setEnabled(true);
        btnCancelTimer.setEnabled(false);
        updateLocation();


        //Start the Timer and run for however long user chose + 2 min
        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(AutomaticEmailActivity.getNumberOfEmergencyContacts() <= 0){
                    toast = Toast.makeText(BathSaunaWalkTimer.this, "No emergency contacts have been set up yet. Redirecting you to 'Settings'.", Toast.LENGTH_LONG);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(58);
                    toastTV.setWidth(20500);
                    toastTV.setHeight(800);
                    toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();
                    Intent goSettings = new Intent(BathSaunaWalkTimer.this, AutomaticEmailActivity.class);
                    startActivity(goSettings);
                }
                else {
                    btnStartTimer.setEnabled(false);
                    btnCancelTimer.setEnabled(true);
                    hours = hr.getValue();
                    minutes = min.getValue();
                    seconds = sec.getValue();
                    timerInSeconds = (60 * 60 * hours) + (60 * minutes) + (seconds);
                    timerInMilliseconds = timerInSeconds * 1000;
                    //Timer for counting down
                    new CountDownTimer(timerInMilliseconds, 1000) {
                        public void onTick(long millisUntilFinished) {
                            mTextField.setText("Seconds Until Timer Sounds: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            mTextField.setText("Timer is Done!");
                            btnCancelTimer.setEnabled(true);
                            Toast toast = Toast.makeText(BathSaunaWalkTimer.this, userName +
                                    ", you have 2 minutes to" +
                                    " turn off the timer, otherwise a distress signal will be sent to all emergency contacts listed.", Toast.LENGTH_LONG);
                            LinearLayout toastLayout = (LinearLayout) toast.getView();
                            TextView toastTV = (TextView) toastLayout.getChildAt(0);
                            toastTV.setTextSize(58);
                            toastTV.setWidth(20500);
                            toastTV.setHeight(800);
                            toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                            toast.show();
                            LinearLayout toastLayoutv2 = (LinearLayout) toast.getView();
                            TextView toastTVv2 = (TextView) toastLayoutv2.getChildAt(0);
                            toastTVv2.setTextSize(58);
                            toastTVv2.setWidth(20500);
                            toastTVv2.setHeight(800);
                            toastTVv2.setGravity(Gravity.CENTER_HORIZONTAL);
                            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                            toast.show();

                            displayTwoMinTimer();
                            toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 12000);
                            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 120000);
                            vibrator = (Vibrator) BathSaunaWalkTimer.this.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(120000);
                            sendSignal = true;
                            updateLocation();
                            Handler twoMin = new Handler();
                            twoMin.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateLocation();
                                    if (sendSignal == true) {
                                        //SEND DISTRESS SIGNAL!!!
                                        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 120000);
                                        toneG.startTone(ToneGenerator.TONE_DTMF_8, 120000);
                                        Intent emergencyIntent = new Intent(BathSaunaWalkTimer.this, AutomaticEmailActivity.class);
                                        emergencyIntent.putExtra("gpsLocation", gpsLocationString);
                                        emergencyIntent.putExtra("emergencyFlag", "true");
                                        startActivity(emergencyIntent);
                                    }

                                }
                            }, 120000); //2 minutes in milliseconds = 120000

                        }
                    }.start();
                }
            }
        });

        //Cancel the Timer and reset everything, but first ask with dialog for confirmation
        btnCancelTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //CODE TO BUILD DIALOG
                AlertDialog.Builder builder = new AlertDialog.Builder(BathSaunaWalkTimer.this);
                builder.setMessage("Are you sure you want to cancel the timer?");
                builder.setCancelable(true);
                //CANCEL TIMER CODE
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        btnStartTimer.setEnabled(true);
                        btnCancelTimer.setEnabled(false);
                        mTextField.setText("You have not set a timer.");
                        if(toneG != null){
                            toneG.stopTone();
                        }
                        if(vibrator != null) {
                            vibrator.cancel();
                        }
                        if(twoMinCounter != null ){
                            twoMinCounter.cancel();
                        }
                        hours = 0;
                        minutes = 0;
                        seconds = 0;
                        hr.setValue(0);
                        min.setValue(0);
                        sec.setValue(0);
                        sendSignal = false;
                    }
                });
                //DO NOT CANCEL TIMER
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toneG.stopTone();
                        vibrator.cancel();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


    }//end of onCreate() method


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
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                loc.setText("Your current GPS Location: \nLatitude: " + latitude + "\nLongitude: " + longitude);
                gpsLocationString="current GPS Location is\n" + "Latitude: " + latitude + "\nLongitude: " + longitude;
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }
    }


    public void displayTwoMinTimer(){
        twoMinCounter = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText(millisUntilFinished/1000 +" Seconds to Cancel Timer.");
            }
            public void onFinish() {
                mTextField.setText("Timer done, sending signal");
            }
        }.start();
    }



}
