package cs490.seniorpal;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

public class VisionHearingTests extends AppCompatActivity {

    LinearLayout ll;
    ViewGroup.LayoutParams lpView;
    Button btnVision, btnHearing;
    TextView tvTopic, tvQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_hearing_tests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }

        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(ll, llParam);
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundResource(R.drawable.visionhearing_background);
        lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        tvTopic = new TextView(this);
        tvTopic.setText("Which test would you like to take?\n");
        tvTopic.setLayoutParams(lpView);
        tvTopic.setTextSize((float) 58.0);
        tvTopic.setTextColor(Color.BLACK);
        tvTopic.setGravity(Gravity.CENTER);
        ll.addView(tvTopic);

        btnVision = new Button(this);
        btnVision.setText("Vision Test");
        btnVision.setLayoutParams(lpView);
        btnVision.setTextSize((float) 35.0);
        btnVision.setGravity(Gravity.CENTER_HORIZONTAL);
        //OPEN VISION TEST WEBVIEW
        btnVision.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openVisionTest();
            }
        });
        ll.addView(btnVision);

        tvQuestion = new TextView(this);
        tvQuestion.setText("\nOR\n");
        tvQuestion.setLayoutParams(lpView);
        tvQuestion.setTextSize((float) 25.0);
        tvQuestion.setGravity(Gravity.CENTER_HORIZONTAL);
        tvQuestion.setTextColor(Color.BLACK);
        ll.addView(tvQuestion);

        btnHearing = new Button(this);
        btnHearing.setText("Hearing Test");
        btnHearing.setLayoutParams(lpView);
        btnHearing.setTextSize((float) 35.0);
        btnHearing.setGravity(Gravity.CENTER_HORIZONTAL);
        //OPEN HEARING TEST WEBVIEW
        btnHearing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openHearingTest();
            }
        });
        ll.addView(btnHearing);


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

    public void openVisionTest(){
        WebView openVision = new WebView(this);
        setContentView(openVision);
        openVision.loadUrl("http://tinyurl.com/seniorpalvisiontest");
    }

    public void openHearingTest(){
        WebView openHearing = new WebView(this);
        setContentView(openHearing);
        openHearing.loadUrl("http://tinyurl.com/seniorpalhearingtest");
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent goMenu = new Intent(VisionHearingTests.this, MainActivity.class);
        startActivity(goMenu);

    }

    @Override
    public void onRestart() {
        super.onRestart(); //Always call the superclass method first
        finish();
        Intent goMenu = new Intent(VisionHearingTests.this, MainActivity.class);
        startActivity(goMenu);
    }

}
