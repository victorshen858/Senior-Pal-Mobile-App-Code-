package cs490.seniorpal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button btnShowMenu;
    NavigationView navigationView;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialization of Navigation Menu
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //open menu faster and automatically
        startMenu();

        //TextView to links to Contacts Setup
        TextView mTextView = (TextView) findViewById(R.id.setup_string);
        if(mTextView != null) {
            mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mTextView.setTypeface(null, Typeface.ITALIC);
            mTextView.setClickable(true);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setUpIntent = new Intent(MainActivity.this, AutomaticEmailActivity.class);
                    startActivity(setUpIntent);
                }
            });
        }



        TextView helpText = (TextView) findViewById(R.id.ask_string);
        helpText.setClickable(true);
        helpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler openMenu = new Handler();
                openMenu.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(MainActivity.this, "Choose a feature that you'd like to use.", Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(58);
                        toastTV.setWidth(20500);
                        toastTV.setHeight(500);
                        toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                        toast.show();
                        startMenu();
                    }
                }, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        Intent navIntent;
        if (id == R.id.emergency_button) {
            navIntent = new Intent(MainActivity.this, EmergencyButton.class);
            startActivity(navIntent);
        } else if (id == R.id.bath_sauna_timer) {
            navIntent = new Intent(MainActivity.this, BathSaunaWalkTimer.class);
            startActivity(navIntent);
        } else if (id == R.id.social_dance_tutorials) {
            navIntent = new Intent(MainActivity.this, SocialDanceTutorials.class);
            startActivity(navIntent);
        } else if (id == R.id.symptom_checker) {
            navIntent = new Intent(MainActivity.this, SymptomChecker.class);
            startActivity(navIntent);
        } else if (id == R.id.food_list) {
            navIntent = new Intent(MainActivity.this, GoodFoods.class);
            startActivity(navIntent);
        } else if (id == R.id.what_to_do_list) {
            navIntent = new Intent(MainActivity.this, WhatToDo.class);
            startActivity(navIntent);
        } else if (id == R.id.vision_and_hearing_tests) {
            navIntent = new Intent(MainActivity.this, VisionHearingTests.class);
            startActivity(navIntent);
        } else if (id == R.id.nap_playlist) {
            navIntent = new Intent(MainActivity.this, MeditationNapTime.class);
            startActivity(navIntent);
        } else if (id == R.id.settings) {
            navIntent = new Intent(MainActivity.this, AutomaticEmailActivity.class);
            startActivity(navIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume(); //Always call the superclass method first
        //restart menu
        startMenu();
    }

    @Override
    public void onRestart() {
        super.onRestart(); //Always call the superclass method first
        //restart menu
        startMenu();
    }

    public void startMenu(){
        //open menu faster and automatically
        Handler openMenu = new Handler();
        openMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        }, 988);
    }

}
