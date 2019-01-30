package cs490.seniorpal;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GoodFoods extends AppCompatActivity {
    Spinner spinner;
    String spinnerTopic = "";
    ArrayAdapter<String> myAdapter;
    WebView foodChoiceView;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_foods);


        //Setup for Web View, will change article depending on what body part is selected.
        foodChoiceView = (WebView) findViewById(R.id.food_webview);
        foodChoiceView.getSettings().setJavaScriptEnabled(true);
        foodChoiceView.getSettings().setBuiltInZoomControls(true);
        foodChoiceView.getSettings().setDisplayZoomControls(true);
        foodChoiceView.setWebViewClient(new WebViewClient());
        foodChoiceView.setVisibility(View.VISIBLE);
        foodChoiceView.loadUrl("http://tinyurl.com/seniorpaleyearticle");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_good_foods, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                if (id == R.id.eyes) {
                    foodChoiceView.reload();
                    foodChoiceView.loadUrl("http://tinyurl.com/seniorpaleyearticle");
                    toast = Toast.makeText(GoodFoods.this,"Eyes was selected. Loading Article...", Toast.LENGTH_LONG);
                } else if (id == R.id.heart){
                    foodChoiceView.reload();
                    foodChoiceView.loadUrl("http://tinyurl.com/seniorpalheartarticle");
                    toast = Toast.makeText(GoodFoods.this,"Heart was selected. Loading Article...", Toast.LENGTH_LONG);
                } else if (id == R.id.brain){
                    foodChoiceView.reload();
                    foodChoiceView.loadUrl("http://tinyurl.com/seniorpalbrainarticle");
                    toast = Toast.makeText(GoodFoods.this,"Brain was selected. Loading Article...", Toast.LENGTH_LONG);
                } else if (id == R.id.skin){
                    foodChoiceView.reload();
                    foodChoiceView.loadUrl("http://tinyurl.com/seniorpalskinarticle");
                    toast = Toast.makeText(GoodFoods.this,"Skin was selected. Loading Article...", Toast.LENGTH_LONG);
                } else if (id == R.id.stomach){
                    foodChoiceView.reload();
                    foodChoiceView.loadUrl("http://tinyurl.com/seniorpalstomacharticle");
                    toast = Toast.makeText(GoodFoods.this,"Stomach was selected. Loading Article...", Toast.LENGTH_LONG);
                } else if (id == R.id.kidney){
                    foodChoiceView.reload();
                        foodChoiceView.loadUrl("http://tinyurl.com/seniorpalkidneyarticle");
                toast = Toast.makeText(GoodFoods.this,"Kidney was selected. Loading Article...", Toast.LENGTH_LONG);
            } else if (id == R.id.liver){
                foodChoiceView.reload();
                foodChoiceView.loadUrl("http://tinyurl.com/seniorpalliverarticle");
                toast = Toast.makeText(GoodFoods.this,"Liver was selected. Loading Article...", Toast.LENGTH_LONG);
            } else if (id == R.id.lungs){
                foodChoiceView.reload();
                foodChoiceView.loadUrl("http://tinyurl.com/seniorpallungsarticle");
                toast = Toast.makeText(GoodFoods.this,"Lungs was selected. Loading Article...", Toast.LENGTH_LONG);
            } else if (id == R.id.bladder){
                foodChoiceView.reload();
                foodChoiceView.loadUrl("http://tinyurl.com/seniorpalbladderarticle");
                toast = Toast.makeText(GoodFoods.this,"Bladder was selected. Loading Article...", Toast.LENGTH_LONG);
                foodChoiceView.reload();
                foodChoiceView.loadUrl("http://tinyurl.com/seniorpalpancreasarticle");
                toast = Toast.makeText(GoodFoods.this,"Pancreas was selected. Loading Article...", Toast.LENGTH_LONG);
            } else if (id == R.id.ears){
                    foodChoiceView.reload();
                    foodChoiceView.loadUrl("http://tinyurl.com/seniorpalhearingarticle");
                    toast = Toast.makeText(GoodFoods.this,"Ears was selected. Loading Article...", Toast.LENGTH_LONG);
            } else if (id == R.id.thyroid){
                foodChoiceView.reload();
                foodChoiceView.loadUrl("http://tinyurl.com/seniorpalthyroidarticle");
                toast = Toast.makeText(GoodFoods.this,"Thyroid was selected. Loading Article...", Toast.LENGTH_LONG);
            } else{ //Default is eyes
                foodChoiceView.reload();
                foodChoiceView.loadUrl("http://tinyurl.com/seniorpaleyesarticle");
            }
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(58);
            toastTV.setWidth(20500);
            toastTV.setHeight(800);
            toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
            toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
            toast.show();
                return super.onOptionsItemSelected(item);
        }
    }



    public void onBackPressed() {
        finish();
    }
}
