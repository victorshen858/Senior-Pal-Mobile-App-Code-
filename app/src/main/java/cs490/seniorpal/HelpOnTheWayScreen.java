package cs490.seniorpal;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class HelpOnTheWayScreen extends AppCompatActivity {
    ToneGenerator toneG;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_on_the_way_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }

        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 120000);
        toneG.startTone(ToneGenerator.TONE_DTMF_8, 1000000);
        vibrator = (Vibrator) HelpOnTheWayScreen.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
        Intent menuIntent = new Intent(HelpOnTheWayScreen.this,MainActivity.class);
        startActivity(menuIntent);
    }
}
