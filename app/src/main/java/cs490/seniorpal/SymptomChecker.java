package cs490.seniorpal;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class SymptomChecker extends AppCompatActivity {
    Button btnProceed;
    WebView openSymptomChecker;
    TextView disclaimerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }

        disclaimerText = (TextView) findViewById(R.id.textViewSymptomChecker);

        openSymptomChecker = (WebView) findViewById(R.id.webViewSymptomChecker);
        openSymptomChecker.setVisibility(View.INVISIBLE);

        btnProceed = (Button) findViewById(R.id.button_proceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSymptomChecker.setVisibility(View.VISIBLE);
                btnProceed.setVisibility(View.INVISIBLE);
                openSymptomChecker.getSettings().setJavaScriptEnabled(true);
                openSymptomChecker.getSettings().setBuiltInZoomControls(true);
                openSymptomChecker.getSettings().setDisplayZoomControls(true);
                openSymptomChecker.setWebViewClient(new MyWebViewClient());
                openSymptomChecker.setVisibility(View.VISIBLE);
                openSymptomChecker.loadUrl("http://tinyurl.com/seniorpalsymptomchecker");
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
        Intent goMenu = new Intent(SymptomChecker.this, MainActivity.class);
        startActivity(goMenu);

    }

    @Override
    public void onRestart() {
        super.onRestart(); //Always call the superclass method first
        finish();
        Intent goMenu = new Intent(SymptomChecker.this, MainActivity.class);
        startActivity(goMenu);
    }
}
