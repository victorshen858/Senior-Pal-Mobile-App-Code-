package cs490.seniorpal;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;


public class SocialDanceTutorials extends AppCompatActivity {
    Spinner spinnerDance;
    ArrayAdapter<String> myAdapter;
    String spinnerTopic="";
    String danceString = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_dance_tutorials);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }

        //SPINNER OF BALLROOM DANCES SETUP
        spinnerDance = (Spinner) findViewById(R.id.spinnerOfDance);
        myAdapter = new ArrayAdapter(this, R.layout.spinner_layout, new ArrayList<String>());
        myAdapter.add("Cha-Cha-Cha");
        myAdapter.add("Waltz");
        myAdapter.add("Swing");
        myAdapter.add("Rumba");
        myAdapter.add("Quickstep");
        myAdapter.add("Salsa");
        myAdapter.add("Tango");
        myAdapter.add("Viennese Waltz");
        myAdapter.add("Foxtrot");
        myAdapter.add("Samba");
        myAdapter.add("Polka");
        spinnerDance.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        Button btnStartVideo = (Button) findViewById(R.id.btn_Dance);
        btnStartVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerTopic = spinnerDance.getSelectedItem().toString();
                switch(spinnerTopic){
                    case "Cha-Cha-Cha":
                        danceString = "http://www.howcast.com/videos/511252-how-to-dance-the-cha-cha-basic-cha-cha-dance/";
                        break;
                    case "Waltz":
                        danceString = "http://www.howcast.com/videos/513454-how-to-do-the-waltz-box-step-ballroom-dance/";
                        break;
                    case "Swing":
                        danceString = "https://www.youtube.com/watch?v=MA4iYUyWyXw";
                        break;
                    case "Rumba":
                        danceString = "https://www.youtube.com/watch?v=c85YThZEW6Y";
                        break;
                    case "Quickstep":
                        danceString = "https://www.youtube.com/watch?v=sA-5oJmlOfw";
                        break;
                    case "Salsa":
                        danceString = "http://www.howcast.com/videos/496860-how-to-do-basic-steps-salsa-dancing/";
                        break;
                    case "Tango":
                        danceString = "http://www.howcast.com/videos/113636-how-to-tango/";
                        break;
                    case "Viennese Waltz":
                        danceString = "https://www.youtube.com/watch?v=L5H0Zwwi7mk";
                        break;
                    case "Foxtrot":
                        danceString = "http://www.howcast.com/videos/513459-how-to-do-basic-foxtrot-steps-ballroom-dance/";
                        break;
                    case "Samba":
                        danceString = "http://www.howcast.com/guides/909-how-to-samba/";
                        break;
                    case "Polka":
                        danceString = "http://www.howcast.com/videos/353445-how-to-dance-a-polka/";
                        break;
                    default:
                        danceString = "http://www.howcast.com/videos/511252-how-to-dance-the-cha-cha-basic-cha-cha-dance/";
                        break;
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(danceString));
                startActivity(i);
            }
        });

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

    @Override
    public void onBackPressed() {
        finish();
        Intent goMenu = new Intent(SocialDanceTutorials.this, MainActivity.class);
        startActivity(goMenu);
    }
}
