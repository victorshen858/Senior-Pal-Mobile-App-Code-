package cs490.seniorpal;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhatToDo extends AppCompatActivity  {
    EditText elem;
    ListView listView;
    ArrayAdapter myAdapter;
    String itemText;
    AlertDialog actions;
    int currentPos;
    ArrayList<String> entries;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_do);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get a support ActionBar corresponding to this toolbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if(ab!=null){ ab.setDisplayHomeAsUpEnabled(true); }

        Button btnGoBack = (Button)findViewById(R.id.back_button);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        finish();
            }
        });

        listView = (ListView) findViewById(R.id.mylist);


        entries = new ArrayList<String>();
        //ArrayList<String> entries = new ArrayList<String>(Arrays.asList("groceries", "wash car", "pickup drycleaning", "library", "clean basement"));
        // Param1 - context
        // Param2 - layout for the row
        // Param3 - ID of the view associated with the row
        // Param4 - the Array of data

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String sList = sharedPref.getString("savedList","Add stuff here");
        String[] items = sList.split(",");
        entries = new ArrayList<String>();
        for(int i=0; i < items.length; i++){
            if(!(items[i].equals(""))){
                entries.add(items[i]);
            }
        }

        if(entries.size()==0){
            entries = new ArrayList<String>(Arrays.asList("Ex: Read Novel", "Ex: Watch Television", "Ex: Play Chess", "Ex: Wash Dentures", "Ex: Take a Walk"));
        }

        myAdapter = new ArrayAdapter<String>(this, R.layout.line,entries);

        listView.setAdapter(myAdapter);
        listView.setBackgroundColor(Color.GREEN);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast;
                itemText = (String)parent.getItemAtPosition(position);
                //ListView item selected contains the prefix 'Done:', so remove it and add the new item to the top of the list
                if(itemText.contains("Done:")){
                    toast = Toast.makeText(getApplicationContext(), "Status of '" + ((TextView) view).getText()+"' is now marked as 'Undone'.", Toast.LENGTH_SHORT);

                    String itemTextWithoutDone = itemText.replace("Done:","");
                    myAdapter.insert(itemTextWithoutDone,0);
                    myAdapter.remove(itemText);
                    myAdapter.notifyDataSetChanged();
                }
                //ListView item selected DOES NOT CONTAIN the prefix 'Done:', so add the prefix and move it to the bottom of the list
                else{
                    toast = Toast.makeText(getApplicationContext(), "Status of '" + ((TextView) view).getText()+"' is now marked as 'Done'.", Toast.LENGTH_SHORT);
                    String itemTextWithDone = "Done: "+itemText;
                    myAdapter.add(itemTextWithDone);
                    myAdapter.remove(itemText);
                    myAdapter.notifyDataSetChanged();
                }
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(58);
                toastTV.setWidth(20500);
                toastTV.setHeight(800);
                toastTV.setGravity(Gravity.CENTER_HORIZONTAL);
                toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                toast.show();

            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                    currentPos = position;
                                                    actions.show();
                                                    return true;
                                                }
                                            }
        );

        elem =  (EditText)findViewById(R.id.input);

        final TextView.OnEditorActionListener mReturnListener =
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                        // If the action is a key-up event on the return key, send the message
                        if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                            String input = view.getText().toString();
                            myAdapter.add(input);

                            Toast.makeText(getApplicationContext(), "Adding " + input, Toast.LENGTH_SHORT).show();
                            view.setText("");
                        }
                        return true;
                    }
                };
        elem.setOnEditorActionListener(mReturnListener);

        //CODE TO BUILD DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this item?");
        String[] options = {"Delete"};
        builder.setItems(options, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();

    }

    DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0: // Delete
                    myAdapter.remove(myAdapter.getItem(currentPos));
                    break;
                default:
                    break;
            }
        }
    };


    public void clearEdit(View v) {
        String check = "";
        for(int i = (myAdapter.getCount() - 1); i>=0; i--){
            check = myAdapter.getItem(i).toString();
            if(check.contains("Done:")) {
                myAdapter.remove(myAdapter.getItem(i));
                myAdapter.notifyDataSetChanged();
            }
            else{
                //Do nothing
            }
        }
    }

    public void addElem(View v) {
        String input = elem.getText().toString();
        //If the string is not empty then insert it at the top of the ListView
        if(input.length()>0) {
            myAdapter.insert(input, 0);
            myAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please type in an actual String.", Toast.LENGTH_SHORT).show();
        }
        elem.setText("");
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
    protected void onPause(){
        super.onPause();
        StringBuilder saveListStr = new StringBuilder();
        for(String s : entries){
            saveListStr.append(s);
            saveListStr.append(",");
        }
        String strList = saveListStr.toString();

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("savedList", strList);
        editor.commit();
    }


}
